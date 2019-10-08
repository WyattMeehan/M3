package DataAnalysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import Anonymization.*;

public class Analysis {

    static ArrayList<Data> local = new ArrayList<Data>();
    static ArrayList<Data> global = new ArrayList<Data>();
    static RandomAccessFile localFile, globalFile;

    // saves the data to file
    public static void save(Data data, RandomAccessFile write) throws IOException {
        write.writeBytes(data.getDate() + "\t" + data.getMac() + "\t" + data.getLocation() + "\r\n");
    }

    // checks if a hex number has second least significant bit is 1
    public static boolean checkBit(char hex) {
        if (hex == '2' || hex == '3')
            return true;
        if (hex == '6' || hex == '7')
            return true;
        if (hex == 'a' || hex == 'b')
            return true;
        if (hex == 'e' || hex == 'f')
            return true;
        return false;
    }

    // looks up vendor
    // returns response code + content
    public static String[] lookup(String MAC) throws IOException {

        String result[] = new String[2];

        // sets url
        String url = "https://mac2vendor.com/api/v4/mac/" + MAC;
        URL api = new URL(url);

        // initiates connection
        HttpURLConnection connect = (HttpURLConnection)api.openConnection();

        // gets response code
        int code = connect.getResponseCode();
        result[0] = "" + code;

        // gets response
        BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);
        result[1] = response.toString();
        in.close();

        return result;
    }

    // seperates globally unique MAC addresses and locally randomized MAC addresses
    public static void randomSeperate(ArrayList<Data> data) throws IOException {

        // loops through all probe requests
        for (Data request : data){

            if (checkBit(request.getMac().charAt(1)))
                save(request, localFile);
            else
                save(request, globalFile);
            
        }
    }

    public static void main(String[] args) throws IOException {

        // output files
        localFile = new RandomAccessFile("local.txt", "rw");
        globalFile = new RandomAccessFile("global.txt", "rw");
        
        // reads file
        ArrayList<Data> data = RandomDataGenerator.readFile("data.txt");

        randomSeperate(data);
        
        localFile.close();
        globalFile.close();
        
    }

}
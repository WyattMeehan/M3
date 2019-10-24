package DataAnalysis;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Hashtable;

import Anonymization.Data;
import Anonymization.RandomDataGenerator;

public class Analysis {

    static ArrayList<Data> local = new ArrayList<Data>();
    static ArrayList<Data> global = new ArrayList<Data>();
    static RandomAccessFile localFile, globalFile;
    static Hashtable<String, String> table;

    // formats MAC address
    public static String format(String MAC){
        return MAC.replaceAll(":", "").toUpperCase();
    }

    // saves the data to file
    public static void save(Data data) throws IOException {

        // interprets MAC addressesS
        String MAC = format(data.getMac());

        if (checkBit(MAC.charAt(1)))
            localFile.writeBytes(data.getDate() + "\t" + MAC + "\t" + data.getLocation() + "\r\n");
        else {
            String vendor = table.get(MAC.substring(0, 6));
            globalFile.writeBytes(data.getDate() + "\t" + MAC + "\t" + data.getLocation() + "\t" + vendor + "\r\n");
        }

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

    /*
     * // looks up vendor // returns response code + content public static String[]
     * lookup(String MAC) throws IOException {
     * 
     * String result[] = new String[2];
     * 
     * // sets url String url = "https://mac2vendor.com/api/v4/mac/" + MAC; URL api
     * = new URL(url);
     * 
     * // initiates connection HttpURLConnection connect =
     * (HttpURLConnection)api.openConnection();
     * connect.addRequestProperty("User-Agent",
     * "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
     * 
     * // gets response code int code = connect.getResponseCode(); result[0] = "" +
     * code;
     * 
     * // gets response BufferedReader in = new BufferedReader(new
     * InputStreamReader(connect.getInputStream())); String inputLine; StringBuffer
     * response = new StringBuffer(); while ((inputLine = in.readLine()) != null)
     * response.append(inputLine); result[1] = response.toString(); in.close();
     * 
     * return result; }
     * 
     * // gets vendor name from API response public static String getVendor(String
     * response){
     * 
     * // trims the response then gets the vendor name using location in response
     * return response.substring(response.indexOf("vendor") + 10,
     * response.indexOf("mac") - 15);
     * 
     * // NOTE: LAA means Locally Administered Addresses
     * 
     * }
     * 
     * // seperates globally unique MAC addresses and locally randomized MAC
     * addresses public static void randomSeperate(ArrayList<Data> data) throws
     * IOException {
     * 
     * // loops through all probe requests for (Data request : data){
     * 
     * if (checkBit(request.getMac().charAt(1))) save(request, localFile); else
     * save(request, globalFile);
     * 
     * } }
     */

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        // loads hash table
        FileInputStream fis = new FileInputStream("data/oui.tmp");
        ObjectInputStream ois = new ObjectInputStream(fis);
        table = (Hashtable<String, String>) ois.readObject();

        // output files
        localFile = new RandomAccessFile("data/local.txt", "rw");
        globalFile = new RandomAccessFile("data/global.txt", "rw");
        
        // reads file
        ArrayList<Data> data = RandomDataGenerator.readFile("data/data.txt");

        for (Data d : data)
            save(d);

        
        localFile.close();
        globalFile.close();
        ois.close();
        
    }

}
package DataAnalysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public static boolean checkBit(char hex){
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
package DataAnalysis;

import java.util.ArrayList;
import Anonymization.*;

public class Analysis{

    static ArrayList<Data> local = new ArrayList<Data>();
    static ArrayList<Data> global = new ArrayList<Data>();

    // saves the data to file
    public static void save(ArrayList<Data> data, String fileName){
        RandomAccessFile write = new RandomAccessFile(fileName, "rw");
        for(int i = 0; i < data.size(); i++) {
			write.writeBytes(data.get(i).getDate() + "\t" + data.get(i).getMac() + "\t" +
			data.get(i).getLocation() + "\r\n");
		}
    }

    // seperates globally unique MAC addresses and locally randomized MAC addresses
    public static void randomSeperate(ArrayList<Data> data){

        // array of characters that have Universally/locally administered address bit is 1
        char temp[] = {'2', '3', '6', '7', 'A', 'B', 'E', 'F'};

        for (Data a : data){

            // gets the second byte of the MAC address
            char secondByte = a.getMac().charAt(1);
            if (Arrays.asList
            
        }
    }

    public static void main(String[] args){
        
        // reads file
        ArrayList<Data> data = RandomDataGenerator.readFile("data.txt");

    }

}
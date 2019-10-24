package Read;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.Hashtable;

public class Reader {
    public static void main(String[] args) throws IOException {

        // oui - vendor hash table
        Hashtable<String, String> ouiVendor = new Hashtable<String, String>();

        // reads from file
        // original file was modified to contain only the lines with oui and vendor
        RandomAccessFile oui = new RandomAccessFile("data/shorten_oui.txt", "r");

        // runs through every lines
        while (oui.getFilePointer() < oui.length()){

            // reads line
            String line = oui.readLine();

            // gets oui
            String key = line.substring(0, 6);

            // gets vendor
            String value = line.substring(line.indexOf(")") + 1);

            // adds to hash table
            ouiVendor.put(key, value);
        }
        
        // saves hash table
        FileOutputStream fos = new FileOutputStream("data/oui.tmp");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(ouiVendor);
        oos.close();

        oui.close();
    }
    
    
}
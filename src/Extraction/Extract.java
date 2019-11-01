// Extracts data from pis
// doesn't work
package Extraction;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Hashtable;

import Anonymization.Data;

public class Extract {

    // TO CHANGED
    //
    // data file names
    static String[] files = { "data/pi1", "data/pi1", "data/pi1", "data/pi4", "data/pi4" };
    //
    // frequency (how long a timestamp in seconds)
    static int frequency = 60;

    static Hashtable<String, Data>[] day;

    /*
     * // copy data public static void scp(String ip){
     * 
     * try{
     * 
     * // file name String file = "~/Desktop/M3-Project/scans/m3Scans.log"; file =
     * file.replace("'", "'\"'\"'"); file = "'" + file + "'";
     * 
     * // connects JSch jsch = new JSch(); Session session = jsch.getSession("root",
     * ip, 22); UserInfo info = new Info(); session.setUserInfo(info);
     * session.connect();
     * 
     * // scp Channel channel = session.openChannel("exec"); String command =
     * "scp -f " + file; ((ChannelExec)channel).setCommand(command);
     * channel.connect();
     * 
     * session.disconnect(); } catch (Exception e){ System.out.println(e); } }
     */

    // reads data from file to the array timestamps
    public static void read(String name) throws IOException {
        RandomAccessFile file = new RandomAccessFile(name, "r");
        while (file.getFilePointer() < file.length()){

            // splits a line of data into parts
            String[] parts = file.readLine().split("\t");

            // finds the value of time data
            int location = parts[0].indexOf(":");
            int hour = Integer.parseInt(parts[0].substring(location - 2, location));
            int minute = Integer.parseInt(parts[0].substring(location + 1, location + 3));
            int second = Integer.parseInt(parts[0].substring(location + 4, location + 6));

            // computes time in seconds
            int time = Integer.parseInt(parts[0].substring(hour, hour + 2)) + 
        }
        file.close();
    }

    public static void main(String[] args) throws FileNotFoundException {

        // computes the number of timestamps
        int no = 24 * 60 * 60 / frequency;

        // initializes an array of timestamps
        day = new Hashtable[no];
        for (int i = 0; i < no; i ++){
            day[i] = new Hashtable<String, Data>();
        }

        

    }
    
}
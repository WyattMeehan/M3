// Extracts data from pis
// appends output file (does not rewrite)
package Extraction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Anonymization.Data;

public class Extract {

    // TO CHANGED
    //
    // data file names
    static final String[] files = { "data/pi1.log", "data/pi1.log", "data/pi1.log", "data/pi4.log", "data/pi4.log" };
    //
    // frequency (how long a timestamp in seconds)
    static final int frequency = 60;
    // output file name
    static final String output = "data/extract.txt";
    //

    static HashMap<String, Data>[] day;

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

    // converts from seconds to date
    public static String sec2Date(int sec){
        int hour = sec / 3600;

        // spare seconds
        int left = sec - hour * 3600;

        int minute = left / 60;
        int second = left - minute * 60;
        String h = "" + hour;
        String m = "" + minute;
        String s = "" + second;

        // formats data
        if (hour < 10)
            h = "0" + h;
        if (minute < 10)
            m = "0" + m;
        if (second < 10)
            s = "0" + s;

        return "" + h + ":" + m + ":" + s + ".000000";
    }

    // reads data from file to the array timestamps
    public static void read(String name, int pi) throws IOException {
        RandomAccessFile file = new RandomAccessFile(name, "r");
        while (file.getFilePointer() < file.length()){

            // splits a line of data into parts
            String[] parts = file.readLine().split("\t");
            String date = parts[0];
            String mac = parts[1];
            int signal = Integer.parseInt(parts[2]);

            // values of time data
            int location = date.indexOf(":");
            int hour = Integer.parseInt(date.substring(location - 2, location));
            int minute = Integer.parseInt(date.substring(location + 1, location + 3));
            int second = Integer.parseInt(date.substring(location + 4, location + 6));
            
            // timeslot to put the data into
            int timeslot = (hour * 3600 + minute * 60 + second) / frequency;

            // updates time so all request in a timeslot have the same date
            int time = timeslot * frequency;

            Data data;
            if (day[timeslot].containsKey(mac))
                data = day[timeslot].get(mac);
            else {

                // updates date to fit with time slot
                date = date.substring(0, 11) + sec2Date(time);

                data = new Data(date, mac);
            }
            data.setSignal(signal, pi);
            day[timeslot].put(mac, data);
        }
        file.close();
    }

    public static void main(String[] args) throws IOException {

        // computes the number of timestamps
        int no = 86400 / frequency;

        // initializes an array of timestamps
        day = new HashMap[no];
        for (int i = 0; i < no; i ++){
            day[i] = new HashMap<String, Data>();
        }

        for (int i = 0; i < files.length; i++)
            read(files[i], i);
        BufferedWriter writer = new BufferedWriter(new FileWriter(output, true));
        for (HashMap table : day){

            // writes all data in a timeslot
            Iterator iterator = table.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry request = (Map.Entry)iterator.next();
                writer.write(request.getValue().toString());
                writer.newLine();
            }
            
        }
        writer.close();
    }
    
}
// reads files from folder data/Extraction/Pis
// excludes the files that have been read (in data/Extraction/Output/Exclusion.txt)
// combines data from all pis
// save data to files in folder data/Extraction/Output
// extracted file list is saved in data/Extraction/Output/Exclusion.txt

package Extraction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Anonymization.Data;

public class Extract {

    //// PARAMETERS (can be changed when used)

    // frequency (how long a timestamp in seconds)
    static final int frequency = 60;


    //// VARIABLES (don't change when used)
    
    // data folder path
    static final String path = "data/Extraction/";

    // data for a day
    static HashMap<String, Data>[] day;


    public static void main(String[] args) throws IOException {

        // number of timestamps
        final int no = 86400 / frequency;

        // initializes an array of timestamps
        day = new HashMap[no];  
        for (int i = 0; i < no; i ++)
            day[i] = new HashMap<String, Data>();

        // path for pis' data folder
        final String pis = "data/Extraction/Pis/";

        // gets files' names (dates)
        final File pi1 = new File(pis + "1");  // assumes that all pis collected data all the time
                                                // samples only pi 1
        ArrayList<String> names = new ArrayList<String>();
        for (File file : pi1.listFiles())
            names.add(file.getName());
        System.out.println(names.size() + " files/pi");
        
        // output folder path
        final String folder = path + "Output/";
        
        // reads excluding files' names
        RandomAccessFile file = new RandomAccessFile(folder + "Exclusion.txt", "rw");
        ArrayList<String> excluded = new ArrayList<String>();
        while (file.getFilePointer() < file.length())
            excluded.add(file.readLine());
        names.removeAll(excluded);
        for (String name : names)
            file.writeBytes(name + "\r\n");
        file.close();
        
        // loops through all the files to extract data
        for (String name : names){
            for (int i = 1; i < 6; i++)
                read(pis + i + "/" + name, i - 1);

            // output file name (date)
            String output = folder + name + ".txt";

            // writes output to file
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

    // converts from seconds to date
    // public static String sec2Date(int sec){
    //     int hour = sec / 3600;

    //     // spare seconds
    //     int left = sec - hour * 3600;

    //     int minute = left / 60;
    //     int second = left - minute * 60;
    //     String h = "" + hour;
    //     String m = "" + minute;
    //     String s = "" + second;

    //     // formats data
    //     if (hour < 10)
    //         h = "0" + h;
    //     if (minute < 10)
    //         m = "0" + m;
    //     if (second < 10)
    //         s = "0" + s;

    //     return "" + h + ":" + m + ":" + s + ".000000";
    // }

    // reads data from file to the array timestamps
    public static void read(String name, int pi) throws IOException {
        System.out.println("reading " + name);
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

            // saves data
            Data data;
            if (day[timeslot].containsKey(mac))
                data = day[timeslot].get(mac);
            else {
                data = new Data(date, mac);
            }
            data.setSignal(signal, pi);
            day[timeslot].put(mac, data);

        }
        file.close();
    }

}
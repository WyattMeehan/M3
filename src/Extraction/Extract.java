// reads files from folder data/pidata
// excludes the files that have been read (in data/Extraction/Output/Exclusion.txt)
// combines data from all pis
// save data to files in folder data/Extraction/Output
// extracted file list is saved in data/Extraction/Output/Exclusion.txt

package Extraction;

import java.io.BufferedWriter;
//import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
/*
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
*/
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Anonymization.Data;

public class Extract {

    //// PARAMETERS (can be changed when used)

    // frequency (how long a timestamp in seconds)
    static final int frequency = 10;

    //// VARIABLES (don't change when used)

    // data folder path
    static final String path = "data/Extraction/";

    /*
    // data for all days
    static ArrayList<HashMap<String, Data>[]> days = new ArrayList<HashMap<String, Data>[]>();
    */

    // hashmap to store data
    static HashMap<String, Data>[] map;

    public static void main(String[] args) throws IOException, ParseException {

        // number of timestamps
        final int no = 86400 / frequency;

        // path for pis' data folder
        final String pis = "data/pidata/pi";

        /*
        // gets files' names (dates)
        final File pi1 = new File(pis + "1");  // assumes that all pis collected data all the time
                                                // samples only pi 1
        ArrayList<String> names = new ArrayList<String>();
        for (File file : pi1.listFiles())
            names.add(file.getName());
        */
        
        // output folder path
        final String folder = path + "Output/";
        
        /*
        // reads excluding files' names
        RandomAccessFile file = new RandomAccessFile(folder + "Exclusion.txt", "rw");
        ArrayList<String> excluded = new ArrayList<String>();
        while (file.getFilePointer() < file.length())
            excluded.add(file.readLine());
        names.removeAll(excluded);
        for (String name : names)
            file.writeBytes(name + "\r\n");
        file.close();
        

        // initializes hash mapss
        days.add(initialize(no));
        */

        map = initialize(no);
        /*
        // records the date
        Date date = new Date();

        // pi and AP date formats
        SimpleDateFormat piFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat APFormat = new SimpleDateFormat("dd-MM-yyyy");
        
        
        // loops through all the files to extract data
        for (String name : names){
        
            // initializes hash maps array for next day
            // since a file contains data of 2 days
            days.add(initialize(no));
        
            // removes hash maps array for previous day to avoid memory leak
            days.remove(0);
        */
            // reads 5 folder of 5 pis
            for (int i = 1; i < 6; i++)
                read(pis + i + ".txt", i - 1);

            // output file name (date)
            // changes date format to match with AP data file name
            //date = piFormat.parse(name.substring(12));
            String output = folder + "pi.txt";

            // writes output to file
            write(output);
        //}

        /*
        // finds last day (which is the last output file name)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        date = calendar.getTime();
        String output = folder + APFormat.format(date) + ".txt";

        // writes output to last file
        write(output, 1);
        */

    }

    // writes output to file
    public static void write(String output) throws IOException {
        // finds correct day array
        BufferedWriter writer = new BufferedWriter(new FileWriter(output, true));
        for (HashMap table : map){

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

    // initializes an array of timestamps
    public static HashMap<String, Data>[] initialize(int no){
        HashMap<String, Data>[] day = new HashMap[no];  
        for (int i = 0; i < no; i ++)
            day[i] = new HashMap<String, Data>();
        return day;
    }

    // saves data to day array
    public static void save(int timeslot, String mac, String date, int signal, int pi){
        Data data;

        // retrives saved Data instance in specific timeslot
        if (map[timeslot].containsKey(mac))
            data = map[timeslot].get(mac);

        // or make new Data instance
        else
            data = new Data(date, mac);

        data.setSignal(signal, pi);
        map[timeslot].put(mac, data);
    }

    // reads data from file to the array timestamps
    public static void read(String name, int pi) throws IOException {
        System.out.println("reading " + name);
        RandomAccessFile file = new RandomAccessFile(name, "r");

        /*
        // first of the 2 days in a file
        int firstDay = 0;
        */

        while (file.getFilePointer() < file.length()){

            // splits a line of data into parts
            String[] parts = file.readLine().replace("T", " ").split("\t");
            String date = parts[0];
            String mac = parts[1];
            int signal = Integer.parseInt(parts[2]);

            // values of time data
            int location = date.indexOf(":");
            int hour = Integer.parseInt(date.substring(location - 2, location));
            int minute = Integer.parseInt(date.substring(location + 1, location + 3));
            int second = Integer.parseInt(date.substring(location + 4, location + 6));
            /*
            int currentDay = Integer.parseInt(date.substring(location - 5, location - 3));

            // records first date
            if (firstDay == 0)
                firstDay = currentDay;
            */

            // timeslot to put the data into
            int timeslot = (hour * 3600 + minute * 60 + second) / frequency;

            /*
            // checks date of the probe request
            if (currentDay == firstDay)
                save(timeslot, mac, date, signal, pi, 0);
            else
            */
                save(timeslot, mac, date, signal, pi);
        }
        file.close();
    }

}
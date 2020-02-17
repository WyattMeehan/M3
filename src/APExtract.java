// stores data from the AP

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class APExtract {

    // gets data from AP
    public static String[] get(String id) {
        String result[] = new String[2];

        // sets url
        URL api;
        try {
            api = new URL(id);

            // initiates connection
            HttpURLConnection connect = (HttpURLConnection) api.openConnection();
            connect.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

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

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    // writes to file whose name is current date
    public static void write(String address, String path) {

        // date format
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(path + format.format(new Date()) + ".txt", true));
            String response[] = get(address);
            if (response[1].length() > 2) {
                writer.write(response[1]);
                writer.newLine();
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        for (int i = 0; i < 5; i++)
            System.out.println("Getting data from AP");

        // makes directories
        new File("data/AP/our").mkdir();
        for (int i = 0; i < 3; i++) {
            new File("data/AP/Benton/" + i).mkdir();
            new File("data/AP/Engineering/" + i).mkdir();
            new File("data/AP/King/" + i).mkdir();
        }
        new File("data/AP/King/3").mkdir();

        // ids
        String ids[] = { "lean2", "purayia2", "hatajm", "raychov", "kraftjk", "mohamem", "campbest" };

        // API addresses for ids
        ArrayList<String> addresses = new ArrayList<String>();
        for (String id : ids)
            addresses.add("http://csebu.csi.miamioh.edu/cmx/v1/FFEERE/location/user/" + id + "/");

        // second API
        String API2 = "http://csebu.csi.miamioh.edu/cmx/v1/AABFG/activeCount/Oxford%3E";

        // buildings (Benton, Engineering Building, King)
        String[] buildings = { "Benton%20Hall%3E", "Engineering%20Building%3E", "King%20Library3E" };

        // Benton
        // 0: basement
        // 1: 1st floor
        // 2: 2nd floor
        String[] Benton = new String[3];
        Benton[0] = API2 + buildings[0] + "Basement";
        Benton[1] = API2 + buildings[0] + "1st%20Floor";
        Benton[2] = API2 + buildings[0] + "2nd%20Floor";

        // Engineering Building
        // 0: basement
        // 1: 1st floor
        // 2: 2nd floor
        String[] Engineering = new String[3];
        Engineering[0] = API2 + buildings[1] + "Basement";
        Engineering[1] = API2 + buildings[1] + "1st%20Floor";
        Engineering[2] = API2 + buildings[0] + "2nd%20Floor";

        // King
        // 0: ground floor
        // 1: 1st floor
        // 2: 2nd floor
        // 3: 3rd floor
        String[] King = new String[4];
        King[0] = API2 + buildings[2] + "King%20Library%20Ground%20Floor";
        King[1] = API2 + buildings[2] + "King%20Library%201st%20Floor";
        King[2] = API2 + buildings[2] + "King%20Library%202nd%20Floor";
        King[3] = API2 + buildings[2] + "King%20Library%203rd%20Floor";

        while (true) {

            // path to store data
            String path = "data/AP/";

            // writes data for ids
            // subfolder name is "our"
            for (String address : addresses) {
                write(address, path + "our/");
            }

            // writes data for the 2nd API
            for (int i = 0; i < 3; i++) {
                write(Benton[i], path + "Benton/" + i + "/");
                write(Engineering[i], path + "Engineering/" + i + "/");
                write(King[i], path + "King/" + i + "/");
            }
            write(King[3], path + "King/3/");

            // delay in seconds
            try {
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
    }
}
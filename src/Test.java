import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// package for testing

public class Test {

    public static void main(String[] args) throws IOException, ParseException {

        // gets response
        URL url = new URL("http://csebu.csi.miamioh.edu/cmx/v1/AABFG/activeCount/Oxford%3EBenton%20Hall%3EBasement");
        HttpURLConnection connect = (HttpURLConnection)url.openConnection();
        connect.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream())); 
        String inputLine;
        StringBuffer response = new StringBuffer(); 
        while ((inputLine = in.readLine()) != null)
            response.append(inputLine); 
        String result = response.toString(); 
        in.close();

        // current time
        long time = System.currentTimeMillis();

        // date formatter
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        int index = 0;
        int count = 0;
        int range = 60;

        // finds all "lastSeen" in response
        while (result.indexOf("lastSeen", index + 1) > 0){
            index = result.indexOf("lastSeen", index + 1);
            int vendor = result.indexOf("apMacAddress", index);

            // finds date data
            String date = result.substring(index + 11, index + 30).replace("T", " ");

            if (date.charAt(0) == '2'){

                // converts date to ms
                Date d = format.parse(date);
                long ms = d.getTime();

                if (time - ms < 1000 * range){
                    count++;
                    System.out.println(result.substring(vendor + 15, result.indexOf("\"", vendor + 15)));
                }
            }
            
        }

        System.out.println("\n\n" + count + " people in " + range + " s");
    }
    
}
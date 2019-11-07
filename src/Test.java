import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

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
        in.close();
        String result = response.toString();

        // Processes as JSon object
        JSONArray list = new JSONArray(result);
  
        // current time
        long time = System.currentTimeMillis();

        // date formatter
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        int count = 0;
        int range = 60;

        // goes through all devices
        for (int i = 0; i < list.length(); i++){
            JSONObject object = list.getJSONObject(i);
            
            // finds date data
            String date = object.getString("lastSeen").substring(0, 19).replace("T", " ");

            // finds vendor data
            // sometimes shown as NOT APPLICABLE
            String vendor;
            try{
                vendor = object.getString("manufacturer");
            }
            catch (Exception e){
                vendor = "N/A";
            }
            

            // AP address
            JSONObject maxRSSI = object.getJSONObject("maxDetectedRssi");
            String address = maxRSSI.getString("apMacAddress");

            // converts date to ms
            Date d = format.parse(date);
            long ms = d.getTime();

            if (time - ms < 1000 * range){
                count++;
                System.out.println(date + "\t" + address + "\t" + vendor);
            }
            
            
        }

        System.out.println("\n\n" + count + " people in " + range + " s");
    }
    
}
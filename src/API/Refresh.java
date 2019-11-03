package API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// checks how often does API refresh

public class Refresh {

    // gets data from the API
    public static String get(String api) throws IOException {
        URL url = new URL(api);
        HttpURLConnection connect = (HttpURLConnection)url.openConnection();
        connect.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream())); 
        String inputLine;
        StringBuffer response = new StringBuffer(); 
        while ((inputLine = in.readLine()) != null)
            response.append(inputLine); 
        String result = response.toString(); 
        in.close();
        return result;
    }

    public static void main(String[] args) throws IOException {

        // using Benton 2nd floor for testing
        String api = "http://csebu.csi.miamioh.edu/cmx/v1/AABFG/activeCount/Oxford%3EBenton%20Hall%3EBasement";

        // initial response
        String initial = get(api);

        // 1st new response
        String firstResponse = initial;
        long firstTime = 0;

        // finds first new reponse 
        while (initial.equals(firstResponse)){
            System.out.println("getting another response..");
            firstResponse = get(api);
            firstTime = System.currentTimeMillis();
        }

        System.out.println("got first one");
        
        // 2nd new response
        String secondResponse = firstResponse;
        long secondTime = 0;
        
        // finds second new reponse 
        while (secondResponse.equals(firstResponse)){
            System.out.println("getting another response..");
            secondResponse = get(api);
            secondTime = System.currentTimeMillis();
        }

        // how long in sec
        long time = secondTime - firstTime;
        System.out.println("refreshes each " + time + " ms");
    }
    
}
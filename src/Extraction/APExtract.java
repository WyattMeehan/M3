package Extraction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APExtract {

    // gets data from AP
    public static String[] get(String id) throws IOException{
        String result[] = new String[2];

        // sets url
        String url = "http://csebu.csi.miamioh.edu/cmx/v1/FFEERE/location/user/" + id + "/";
        URL api = new URL(url);

        // initiates connection
        HttpURLConnection connect = (HttpURLConnection)api.openConnection();
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

        return result;
    }

    public static void main(String[] args) throws IOException {
        String result[] = get("lean2");
        System.out.println(result[0]);
        System.out.println(result[1]);
    }
}
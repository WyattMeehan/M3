import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

    public static void main(String[] args) throws IOException, InterruptedException {

        // ids
        String ids[] = {"lean2", "purayia2", "hatajm", "raychov", "kraftjk", "mohamem", "campbest"};

        // date format
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        while (true){

            // writes to file whose name is current date
            // specifies path
            BufferedWriter writer = new BufferedWriter(new FileWriter("data/" + format.format(new Date()) + ".txt", true));

            for (String id : ids){
                String response[] = get(id);
                if (response[1].length() > 2){
                    writer.write(response[1]);
                    writer.newLine();
                    writer.newLine();
                }
            }
            writer.close();
            TimeUnit.SECONDS.sleep(10);
        }
        

    }
}
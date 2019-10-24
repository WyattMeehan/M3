// Extracts data from pis
// doesn't work
package Extraction;

public class Extract {

    /*
    // copy data
    public static void scp(String ip){

        try{

            // file name
            String file = "~/Desktop/M3-Project/scans/m3Scans.log";
            file = file.replace("'", "'\"'\"'");
            file = "'" +  file + "'";

            // connects
            JSch jsch = new JSch();
            Session session = jsch.getSession("root", ip, 22);
            UserInfo info = new Info();
            session.setUserInfo(info);
            session.connect();

            // scp
            Channel channel = session.openChannel("exec");
            String command = "scp -f " + file;
            ((ChannelExec)channel).setCommand(command);
            channel.connect();

            session.disconnect();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    */

    

    public static void main(String[] args) {
        
    }
    
}
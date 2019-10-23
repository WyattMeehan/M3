// User information
package Extraction;

import com.jcraft.jsch.UserInfo;

public class Info implements UserInfo{

    String pass = "jhnjnw2019";

    @Override
    public String getPassphrase() {
        return null;
    }

    @Override
    public String getPassword() {
        return pass;
    }

    @Override
    public boolean promptPassphrase(String mess) {
        return true;
    }

    @Override
    public boolean promptPassword(String mess) {
        return true;
    }

    @Override
    public boolean promptYesNo(String arg0) {
        return true;
    }

    @Override
    public void showMessage(String arg0) {
    }


    
}
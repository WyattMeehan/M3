// package for testing

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

    public static void main(String[] args) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        System.out.println(format.format(new Date()));
    }
    
}
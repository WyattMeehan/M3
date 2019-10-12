// package for testing

import java.io.IOException;
import DataAnalysis.*;

public class Test {

    public static void main(String[] args) throws IOException {
        String result[] = Analysis.lookup("de:0d:90:53:4e:2c");
        String vendor = Analysis.getVendor(result[1]);
        System.out.println(result[1]);
        System.out.println(vendor.trim());
        System.out.println(vendor.length());
    }
    
}
// package for testing

import java.io.IOException;
import DataAnalysis.*;

public class Test {

    public static void main(String[] args) throws IOException {
        String result[] = Analysis.lookup("FC:FB:FB:01:FA:21");
        System.out.println(result[0]);
        System.out.println(result[1]);
    }
    
}
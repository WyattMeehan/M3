// localizes devices using Trilateral Localization

package TrilateralLocalization;

public class Localization {


    //// PARAMETERS

    // signal attenuation factor
    static int n = 3;

    public static void main(String[] args) {
        
    }

    // estimates distance from pi to device
    public static double estimate(double rssi, int TxPower){
        return Math.pow(10, ((double)TxPower - rssi) / (10 * n));
    }
    
}
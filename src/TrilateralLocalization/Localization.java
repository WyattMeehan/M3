// localizes devices using Trilateral Localization
// source: https://www.researchgate.net/publication/276493313_RSSI-based_Algorithm_for_Indoor_Localization

package TrilateralLocalization;

public class Localization {


    //// PARAMETERS

    static int TxPower = 20;

    // signal attenuation factor
    static int n = 3;

    public static void main(String[] args) {
        
    }

    // estimates distance from pi to device
    public static double estimate(double rssi){
        return Math.pow(10, ((double)TxPower - rssi) / (10 * n));
    }
    
}
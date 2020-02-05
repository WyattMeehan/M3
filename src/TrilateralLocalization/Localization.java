// localizes devices using Trilateral Localization

package TrilateralLocalization;

import java.util.ArrayList;

public class Localization {


    //// PARAMETERS

    // signal attenuation factor
    static int n = 3;

    //// VARIABLES

    // locations of the pis
    static int x1 = 0, y1 = 0, z1 = 0;
    static int x2 = 0, y2 = 0, z2 = 0;
    static int x3 = 0, y3 = 0, z3 = 0;
    static int x4 = 0, y4 = 0, z4 = 0;
    static int x5 = 0, y5 = 0, z5 = 0;

    public static void main(String[] args) {
        //ArrayList<Integer> list = order(signals)
    }

    // estimates distance from pi to device
    public static double estimate(double rssi, int TxPower){
        return Math.pow(10, ((double)TxPower - rssi) / (10 * n));
    }

    // orders the signal power descending
    public static ArrayList<Integer> order(double[] signals){

        // order of signal power
        ArrayList<Integer> powerOrder = new ArrayList<Integer>();

        for (int i = 0; i < 5; i++)
            if (signals[i] != 0){
                int index = powerOrder.size();
                for(int j = index - 1; j >= 0; j--){
                    if (signals[i] < signals[powerOrder.get(j)])
                        break;
                    powerOrder.add(j + 1, powerOrder.get(j));
                    index --;
                }
                powerOrder.add(index, i);
            }

        return powerOrder;
    }

    // calculates the coordinate of the device based on distances from that device to the pis
    public static double[] localize(double[] signals, int[] power){
        
        // estimates the distances
        double[] distances = new double[5];
        for (int i = 0; i < 5; i++)
            if (signals[i] != 0)
                distances[i] = estimate(signals[i], power[i]);

        return signals;
    }
    
}
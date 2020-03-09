// localizes devices using Trilateral Localization
// Math logic is in README.pdf

package TrilateralLocalization;

import java.util.ArrayList;

public class Localization {


    //// PARAMETERS

    // signal attenuation factor
    static int n = 3;

    //// VARIABLES

    // locations of the pis
    static double[] x = {0, 0, 0, 0, 0};
    static double[] y = {0, 0, 0, 0 ,0};
    static double[] z = {0, 0, 0, 0 ,0};

    // distances between device and the pis
    static double[] distances;

    public static void main(String[] args) {
        double[] signals = {-79, 0, -4, -4, -2};
        ArrayList<Integer> list = order(signals);
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

            // excludes pis that do not receive signal
            if (signals[i] != 0){

                // basically bubble sort
                int index = powerOrder.size();
                if (index == 0)
                    powerOrder.add(i);
                else{
                    int j;
                    for(j = index; j > 0; j--)
                        if (signals[i] < signals[powerOrder.get(j - 1)])
                            break;
                    powerOrder.add(j, i);
                }

            }

        return powerOrder;
    }

    // does subtraction part
    public static double[] subtract(int no1, int no2){
        double[] abce = new double[4];
        abce[0] = 2 * (x[no1] - x[no2]);
        abce[1] = 2 * (y[no1] - y[no2]);
        abce[2] = 2 * (z[no1] - z[no2]);
        abce[3] = x[no1] * x[no1] + y[no1] * y[no1] + z[no1] * z[no1]
                    - x[no2] * x[no2] - y[no2] - y[no2] - z[no2] - z[no2]
                    - distances[no1] + distances[no2];
        return abce;
    }

    // calculates the coordinate of the device based on distances from that device to the pis
    public static double[] localize(double[] signals, int[] power){

        // sorts signal power
        ArrayList<Integer> signalPower = order(signals);
        
        // estimates the distances
        distances = new double[5];
        for (int i = 0; i < 5; i++)
            if (signals[i] != 0)
                distances[i] = estimate(signals[i], power[i]);

        

        return new double[0];
    }
    
}
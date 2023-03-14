package com.chuxu.BackTrackingAlgorithmWithReduction.part01_preHandleData.RandomCase;

import com.chuxu.entity.Facility;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class N10_2 {
    public static int dimension = 10;
    public static final double[] ci = {264.0, 260.0, 783.0, 539.0, 822.0, 956.0, 229.0, 546.0, 967.0, 421.0};
    public static final double[] pi = {271.0, 142.0, 709.0, 689.0, 112.0, 951.0, 261.0, 406.0, 325.0, 743.0};
    public static final LinkedHashSet<Facility> V = new LinkedHashSet<>();
    public static double[][] disMatrix = {
            {0.00, 80.69, 160.09, 118.89, 138.74, 127.08, 182.56, 171.51, 99.34, 90.39},
            {80.69, 0.00, 89.20, 122.96, 80.76, 143.59, 170.19, 96.17, 103.93, 116.21},
            {160.09, 89.20, 0.00, 192.05, 138.81, 87.76, 174.38, 143.41, 107.34, 131.66},
            {118.89, 122.96, 192.05, 0.00, 83.43, 128.12, 90.19, 111.19, 162.99, 174.78},
            {138.74, 80.76, 138.81, 83.43, 0.00, 91.18, 197.00, 149.89, 134.19, 101.33},
            {127.08, 143.59, 87.76, 128.12, 91.18, 0.00, 86.48, 83.74, 143.52, 109.07},
            {182.56, 170.19, 174.38, 90.19, 197.00, 86.48, 0.00, 161.53, 143.30, 105.83},
            {171.51, 96.17, 143.41, 111.19, 149.89, 83.74, 161.53, 0.00, 153.62, 132.85},
            {99.34, 103.93, 107.34, 162.99, 134.19, 143.52, 143.30, 153.62, 0.00, 128.90},
            {90.39, 116.21, 131.66, 174.78, 101.33, 109.07, 105.83, 132.85, 128.90, 0.00},
    };
    public static final double φp = 0.3;  //本例中都取0.2是会得到性质7疯狂降阶的情况，然后通过性质4直接得到了最优解
    public static final double φc = 0.3;
    public static double P;
    public static double C;

    public static void main(String[] args) {
        preHandle();
    }

    public static void preHandle() {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        P = Arrays.stream(pi).reduce(0.0, Double::sum) * φp;
        C = Arrays.stream(ci).reduce(0.0, Double::sum) * φc;
        P = Double.parseDouble(decimalFormat.format(P));
        C = Double.parseDouble(decimalFormat.format(C));
        System.out.println("P = " + P);
        System.out.println("C = " + C);
        for (int i = 0; i < pi.length; i++) {
            V.add(new Facility(i + 1, ci[i], pi[i], pi[i] / ci[i]));
        }
    }
}

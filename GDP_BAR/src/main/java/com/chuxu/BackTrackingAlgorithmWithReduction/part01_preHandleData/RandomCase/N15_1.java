package com.chuxu.BackTrackingAlgorithmWithReduction.part01_preHandleData.RandomCase;

import com.chuxu.entity.Facility;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class N15_1 {
    public static int dimension = 15;
    public static final double[] ci =
            {286.0, 237.0, 978.0, 358.0, 291.0, 504.0, 358.0, 363.0, 183.0, 468.0, 591.0, 303.0, 409.0, 736.0, 833.0};
    public static final double[] pi =
            {605.0, 603.0, 385.0, 251.0, 916.0, 548.0, 412.0, 660.0, 442.0, 279.0, 240.0, 811.0, 415.0, 648.0, 873.0};
    public static final LinkedHashSet<Facility> V = new LinkedHashSet<>();
    public static double[][] disMatrix = {
            {0.00, 111.04, 188.84, 137.00, 192.46, 157.59, 95.08, 91.52, 128.58, 139.52, 148.76, 129.99, 169.53, 98.92, 114.83},
            {111.04, 0.00, 192.56, 125.15, 99.66, 134.05, 139.68, 94.16, 109.02, 124.22, 153.63, 147.59, 144.22, 182.63, 187.27},
            {188.84, 192.56, 0.00, 149.99, 174.84, 189.92, 101.95, 139.31, 177.78, 178.20, 104.21, 146.64, 120.87, 93.34, 112.55},
            {137.00, 125.15, 149.99, 0.00, 160.11, 144.56, 111.52, 120.11, 124.77, 178.09, 104.35, 188.60, 141.31, 107.13, 104.92},
            {192.46, 99.66, 174.84, 160.11, 0.00, 170.90, 101.25, 164.65, 108.62, 96.52, 194.79, 164.88, 88.19, 83.89, 169.82},
            {157.59, 134.05, 189.92, 144.56, 170.90, 0.00, 102.09, 111.52, 147.66, 186.62, 171.83, 123.70, 186.45, 162.68, 81.80},
            {95.08, 139.68, 101.95, 111.52, 101.25, 102.09, 0.00, 131.51, 88.20, 195.49, 82.60, 128.85, 115.08, 148.52, 105.59},
            {91.52, 94.16, 139.31, 120.11, 164.65, 111.52, 131.51, 0.00, 166.05, 129.04, 146.72, 106.14, 173.64, 111.55, 192.51},
            {128.58, 109.02, 177.78, 124.77, 108.62, 147.66, 88.20, 166.05, 0.00, 174.66, 102.96, 137.39, 182.06, 116.17, 161.55},
            {139.52, 124.22, 178.20, 178.09, 96.52, 186.62, 195.49, 129.04, 174.66, 0.00, 122.81, 113.57, 91.39, 80.17, 121.68},
            {148.76, 153.63, 104.21, 104.35, 194.79, 171.83, 82.60, 146.72, 102.96, 122.81, 0.00, 115.82, 197.65, 131.48, 91.46},
            {129.99, 147.59, 146.64, 188.60, 164.88, 123.70, 128.85, 106.14, 137.39, 113.57, 115.82, 0.00, 124.52, 147.83, 146.91},
            {169.53, 144.22, 120.87, 141.31, 88.19, 186.45, 115.08, 173.64, 182.06, 91.39, 197.65, 124.52, 0.00, 131.73, 197.95},
            {98.92, 182.63, 93.34, 107.13, 83.89, 162.68, 148.52, 111.55, 116.17, 80.17, 131.48, 147.83, 131.73, 0.00, 158.50},
            {114.83, 187.27, 112.55, 104.92, 169.82, 81.80, 105.59, 192.51, 161.55, 121.68, 91.46, 146.91, 197.95, 158.50, 0.00},
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

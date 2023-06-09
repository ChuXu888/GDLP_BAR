package com.chuxu.BackTrackingAlgorithmWithReduction.part01_preHandleData.RandomCase;

import com.chuxu.entity.Facility;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class N18_3 {
    public static int dimension = 18;
    public static final double[] ci =
            {587.0, 574.0, 852.0, 762.0, 284.0, 932.0, 459.0, 682.0, 805.0, 440.0, 699.0, 769.0, 459.0, 723.0, 526.0, 421.0, 636.0, 803.0};
    public static final double[] pi =
            {469.0, 807.0, 501.0, 779.0, 237.0, 401.0, 428.0, 599.0, 850.0, 991.0, 207.0, 292.0, 470.0, 454.0, 144.0, 889.0, 748.0, 590.0};
    public static final LinkedHashSet<Facility> V = new LinkedHashSet<>();
    public static double[][] disMatrix = {
            {0.00, 131.43, 173.53, 104.06, 85.83, 85.22, 171.30, 141.39, 137.09, 186.73, 96.37, 199.78, 80.32, 170.11, 104.13, 85.96, 179.83, 172.67},
            {131.43, 0.00, 139.96, 172.05, 131.82, 174.26, 108.29, 111.93, 186.02, 133.78, 194.04, 180.97, 89.23, 81.18, 120.44, 154.74, 178.76, 196.04},
            {173.53, 139.96, 0.00, 151.64, 146.63, 185.98, 90.26, 117.33, 82.89, 124.98, 112.35, 128.80, 105.08, 80.99, 144.79, 131.90, 116.26, 96.08},
            {104.06, 172.05, 151.64, 0.00, 193.54, 82.20, 86.22, 155.95, 150.10, 91.23, 164.62, 112.73, 81.61, 99.97, 117.78, 112.09, 113.55, 166.72},
            {85.83, 131.82, 146.63, 193.54, 0.00, 145.61, 131.73, 131.87, 151.42, 149.63, 139.18, 188.09, 197.07, 131.42, 182.96, 120.81, 150.91, 82.58},
            {85.22, 174.26, 185.98, 82.20, 145.61, 0.00, 119.86, 151.06, 134.51, 163.32, 94.32, 106.37, 141.45, 115.99, 162.30, 160.08, 134.68, 83.36},
            {171.30, 108.29, 90.26, 86.22, 131.73, 119.86, 0.00, 192.78, 137.25, 193.55, 98.33, 143.97, 97.54, 140.69, 122.40, 199.26, 98.10, 124.65},
            {141.39, 111.93, 117.33, 155.95, 131.87, 151.06, 192.78, 0.00, 194.97, 179.75, 125.62, 161.86, 133.62, 165.55, 167.24, 134.69, 177.34, 117.12},
            {137.09, 186.02, 82.89, 150.10, 151.42, 134.51, 137.25, 194.97, 0.00, 183.41, 197.50, 190.93, 131.13, 113.69, 123.29, 187.70, 151.57, 147.95},
            {186.73, 133.78, 124.98, 91.23, 149.63, 163.32, 193.55, 179.75, 183.41, 0.00, 106.32, 141.24, 127.82, 130.09, 163.98, 103.87, 125.02, 130.83},
            {96.37, 194.04, 112.35, 164.62, 139.18, 94.32, 98.33, 125.62, 197.50, 106.32, 0.00, 111.84, 170.07, 150.34, 168.38, 80.90, 177.91, 140.44},
            {199.78, 180.97, 128.80, 112.73, 188.09, 106.37, 143.97, 161.86, 190.93, 141.24, 111.84, 0.00, 172.11, 144.33, 149.43, 100.61, 174.94, 183.61},
            {80.32, 89.23, 105.08, 81.61, 197.07, 141.45, 97.54, 133.62, 131.13, 127.82, 170.07, 172.11, 0.00, 133.16, 115.46, 183.28, 89.75, 127.54},
            {170.11, 81.18, 80.99, 99.97, 131.42, 115.99, 140.69, 165.55, 113.69, 130.09, 150.34, 144.33, 133.16, 0.00, 103.92, 171.79, 85.28, 173.75},
            {104.13, 120.44, 144.79, 117.78, 182.96, 162.30, 122.40, 167.24, 123.29, 163.98, 168.38, 149.43, 115.46, 103.92, 0.00, 127.95, 180.93, 166.10},
            {85.96, 154.74, 131.90, 112.09, 120.81, 160.08, 199.26, 134.69, 187.70, 103.87, 80.90, 100.61, 183.28, 171.79, 127.95, 0.00, 149.25, 184.78},
            {179.83, 178.76, 116.26, 113.55, 150.91, 134.68, 98.10, 177.34, 151.57, 125.02, 177.91, 174.94, 89.75, 85.28, 180.93, 149.25, 0.00, 120.71},
            {172.67, 196.04, 96.08, 166.72, 82.58, 83.36, 124.65, 117.12, 147.95, 130.83, 140.44, 183.61, 127.54, 173.75, 166.10, 184.78, 120.71, 0.00},
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

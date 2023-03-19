package com.chuxu.BackTrackingAlgorithmWithReduction.part01_preHandleData.RandomCase;

import com.chuxu.entity.Facility;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class N15_2 {
    public static int dimension = 15;
    public static final double[] ci =
            {912.0, 398.0, 129.0, 316.0, 497.0, 909.0, 139.0, 861.0, 936.0, 778.0, 603.0, 112.0, 484.0, 562.0, 175.0};
    public static final double[] pi =
            {203.0, 821.0, 142.0, 185.0, 626.0, 292.0, 784.0, 753.0, 448.0, 983.0, 890.0, 207.0, 772.0, 255.0, 765.0};
    public static final LinkedHashSet<Facility> V = new LinkedHashSet<>();
    public static double[][] disMatrix = {
            {0.00, 97.79, 175.00, 189.95, 176.24, 91.44, 85.14, 137.04, 128.44, 96.30, 115.42, 97.31, 169.15, 146.34, 124.57},
            {97.79, 0.00, 179.81, 168.34, 189.35, 119.59, 116.86, 143.41, 192.83, 128.56, 147.63, 175.52, 142.00, 193.39, 134.51},
            {175.00, 179.81, 0.00, 84.78, 183.97, 174.51, 141.74, 184.01, 110.76, 140.84, 115.91, 98.63, 126.76, 82.75, 113.10},
            {189.95, 168.34, 84.78, 0.00, 131.85, 132.44, 94.75, 115.05, 170.07, 96.18, 146.55, 189.15, 182.05, 160.14, 137.48},
            {176.24, 189.35, 183.97, 131.85, 0.00, 132.71, 128.98, 93.52, 108.80, 121.20, 168.64, 88.06, 152.30, 173.87, 127.84},
            {91.44, 119.59, 174.51, 132.44, 132.71, 0.00, 165.67, 180.60, 136.64, 170.88, 196.96, 115.74, 163.31, 170.50, 152.13},
            {85.14, 116.86, 141.74, 94.75, 128.98, 165.67, 0.00, 127.52, 110.31, 168.51, 81.90, 111.50, 128.69, 139.72, 136.43},
            {137.04, 143.41, 184.01, 115.05, 93.52, 180.60, 127.52, 0.00, 158.81, 119.01, 121.07, 154.00, 96.32, 130.21, 121.49},
            {128.44, 192.83, 110.76, 170.07, 108.80, 136.64, 110.31, 158.81, 0.00, 188.71, 185.00, 193.68, 164.24, 118.04, 198.31},
            {96.30, 128.56, 140.84, 96.18, 121.20, 170.88, 168.51, 119.01, 188.71, 0.00, 166.21, 134.13, 176.13, 159.55, 113.20},
            {115.42, 147.63, 115.91, 146.55, 168.64, 196.96, 81.90, 121.07, 185.00, 166.21, 0.00, 166.17, 137.38, 198.17, 193.78},
            {97.31, 175.52, 98.63, 189.15, 88.06, 115.74, 111.50, 154.00, 193.68, 134.13, 166.17, 0.00, 80.07, 87.87, 169.84},
            {169.15, 142.00, 126.76, 182.05, 152.30, 163.31, 128.69, 96.32, 164.24, 176.13, 137.38, 80.07, 0.00, 114.16, 196.75},
            {146.34, 193.39, 82.75, 160.14, 173.87, 170.50, 139.72, 130.21, 118.04, 159.55, 198.17, 87.87, 114.16, 0.00, 108.17},
            {124.57, 134.51, 113.10, 137.48, 127.84, 152.13, 136.43, 121.49, 198.31, 113.20, 193.78, 169.84, 196.75, 108.17, 0.00},
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

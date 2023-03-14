package com.chuxu.BackTrackingAlgorithmWithReduction.part01_preHandleData.RandomCase;

import com.chuxu.entity.Facility;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class N8_1 {
    public static int dimension = 8;
    public static final double[] ci = {184.0, 452.0, 113.0, 263.0, 617.0, 886.0, 710.0, 323.0};
    public static final double[] pi = {455.0, 753.0, 607.0, 693.0, 484.0, 343.0, 368.0, 238.0};
    public static final LinkedHashSet<Facility> V = new LinkedHashSet<>();
    public static double[][] disMatrix = {
            {0.00, 120.38, 97.78, 119.62, 94.90, 193.53, 89.99, 137.46},
            {120.38, 0.00, 149.61, 108.24, 168.74, 85.15, 181.13, 172.26},
            {97.78, 149.61, 0.00, 155.87, 192.68, 125.76, 109.34, 178.08},
            {119.62, 108.24, 155.87, 0.00, 116.93, 136.14, 109.99, 197.80},
            {94.90, 168.74, 192.68, 116.93, 0.00, 122.65, 131.80, 195.34},
            {193.53, 85.15, 125.76, 136.14, 122.65, 0.00, 155.53, 185.14},
            {89.99, 181.13, 109.34, 109.99, 131.80, 155.53, 0.00, 148.43},
            {137.46, 172.26, 178.08, 197.80, 195.34, 185.14, 148.43, 0.00},
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

package com.chuxu.BackTrackingAlgorithmWithReduction.part01_preHandleData.RandomCase;

import com.chuxu.entity.Facility;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class N10_1 {
    public static int dimension = 10;
    public static final double[] ci = {697.0, 274.0, 703.0, 127.0, 731.0, 858.0, 146.0, 888.0, 817.0, 456.0};
    public static final double[] pi = {207.0, 153.0, 610.0, 709.0, 577.0, 294.0, 630.0, 429.0, 454.0, 934.0};
    public static final LinkedHashSet<Facility> V = new LinkedHashSet<>();
    public static double[][] disMatrix = {
            {0.00, 162.36, 126.15, 171.45, 80.24, 113.17, 130.41, 90.76, 150.58, 175.64},
            {162.36, 0.00, 105.25, 126.13, 170.30, 155.69, 167.45, 104.89, 89.05, 191.55},
            {126.15, 105.25, 0.00, 138.03, 175.50, 195.65, 197.54, 99.85, 166.37, 136.09},
            {171.45, 126.13, 138.03, 0.00, 146.08, 96.18, 163.88, 180.08, 80.05, 89.85},
            {80.24, 170.30, 175.50, 146.08, 0.00, 177.58, 108.66, 97.81, 84.34, 122.59},
            {113.17, 155.69, 195.65, 96.18, 177.58, 0.00, 124.39, 197.00, 174.58, 184.91},
            {130.41, 167.45, 197.54, 163.88, 108.66, 124.39, 0.00, 119.01, 80.86, 165.03},
            {90.76, 104.89, 99.85, 180.08, 97.81, 197.00, 119.01, 0.00, 144.72, 149.56},
            {150.58, 89.05, 166.37, 80.05, 84.34, 174.58, 80.86, 144.72, 0.00, 182.06},
            {175.64, 191.55, 136.09, 89.85, 122.59, 184.91, 165.03, 149.56, 182.06, 0.00},
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

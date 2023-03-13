package com.chuxu.BackTrackingAlgorithmWithReduction.part01_preHandleData.RandomCase;

import com.chuxu.entity.Facility;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class N12_5 {
    public static int dimension = 12;
    public static final double[] ci =
            {128.0, 302.0, 854.0, 278.0, 796.0, 190.0, 865.0, 442.0, 823.0, 440.0, 153.0, 700.0};
    public static final double[] pi =
            {639.0, 464.0, 594.0, 685.0, 888.0, 617.0, 190.0, 387.0, 963.0, 580.0, 847.0, 526.0};
    public static final LinkedHashSet<Facility> V = new LinkedHashSet<>();
    public static double[][] disMatrix = {
            {0.00, 83.01, 86.95, 90.87, 197.64, 138.69, 144.77, 125.28, 81.94, 98.96, 114.02, 102.38},
            {83.01, 0.00, 141.41, 182.38, 159.87, 133.91, 107.62, 151.93, 123.00, 158.78, 100.74, 124.69},
            {86.95, 141.41, 0.00, 134.21, 104.63, 190.91, 157.96, 92.32, 90.66, 88.16, 138.85, 95.32},
            {90.87, 182.38, 134.21, 0.00, 104.08, 128.88, 143.46, 136.22, 133.25, 94.35, 164.06, 199.57},
            {197.64, 159.87, 104.63, 104.08, 0.00, 85.03, 188.87, 169.41, 163.40, 192.13, 160.27, 143.11},
            {138.69, 133.91, 190.91, 128.88, 85.03, 0.00, 183.36, 188.28, 104.67, 199.04, 152.19, 94.77},
            {144.77, 107.62, 157.96, 143.46, 188.87, 183.36, 0.00, 80.62, 100.45, 147.24, 101.31, 104.09},
            {125.28, 151.93, 92.32, 136.22, 169.41, 188.28, 80.62, 0.00, 190.53, 142.41, 97.49, 126.33},
            {81.94, 123.00, 90.66, 133.25, 163.40, 104.67, 100.45, 190.53, 0.00, 117.58, 108.21, 118.18},
            {98.96, 158.78, 88.16, 94.35, 192.13, 199.04, 147.24, 142.41, 117.58, 0.00, 90.93, 126.66},
            {114.02, 100.74, 138.85, 164.06, 160.27, 152.19, 101.31, 97.49, 108.21, 90.93, 0.00, 154.56},
            {102.38, 124.69, 95.32, 199.57, 143.11, 94.77, 104.09, 126.33, 118.18, 126.66, 154.56, 0.00},
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

package com.chuxu.BackTrackingAlgorithmWithReduction.part01_preHandleData.RandomCase;

import com.chuxu.entity.Facility;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class N15_3 {
    public static int dimension = 15;
    public static final double[] ci =
            {810.0, 685.0, 504.0, 584.0, 774.0, 259.0, 205.0, 826.0, 921.0, 691.0, 219.0, 364.0, 731.0, 396.0, 360.0};
    public static final double[] pi =
            {409.0, 446.0, 153.0, 712.0, 309.0, 401.0, 123.0, 320.0, 929.0, 739.0, 770.0, 864.0, 955.0, 875.0, 943.0};
    public static final LinkedHashSet<Facility> V = new LinkedHashSet<>();
    public static double[][] disMatrix = {
            {0.00, 101.64, 93.21, 187.42, 184.95, 199.39, 169.83, 178.78, 178.01, 166.16, 161.60, 107.80, 146.29, 152.98, 102.73},
            {101.64, 0.00, 190.53, 105.92, 109.65, 82.50, 94.70, 135.71, 118.68, 92.43, 82.91, 108.04, 136.72, 163.69, 191.82},
            {93.21, 190.53, 0.00, 133.26, 152.16, 96.78, 94.37, 155.08, 162.43, 152.77, 178.87, 88.85, 143.56, 146.33, 139.54},
            {187.42, 105.92, 133.26, 0.00, 112.58, 87.27, 175.93, 127.68, 81.99, 176.90, 144.57, 180.81, 186.64, 184.01, 187.34},
            {184.95, 109.65, 152.16, 112.58, 0.00, 189.10, 133.07, 111.04, 87.69, 114.16, 174.02, 183.17, 134.36, 184.90, 199.27},
            {199.39, 82.50, 96.78, 87.27, 189.10, 0.00, 103.90, 164.27, 118.17, 104.46, 113.90, 194.29, 88.94, 197.81, 111.63},
            {169.83, 94.70, 94.37, 175.93, 133.07, 103.90, 0.00, 105.38, 165.82, 93.25, 106.88, 107.98, 111.69, 169.72, 144.48},
            {178.78, 135.71, 155.08, 127.68, 111.04, 164.27, 105.38, 0.00, 185.55, 187.95, 131.92, 116.08, 145.82, 92.53, 127.10},
            {178.01, 118.68, 162.43, 81.99, 87.69, 118.17, 165.82, 185.55, 0.00, 91.07, 185.17, 127.80, 95.20, 186.01, 151.55},
            {166.16, 92.43, 152.77, 176.90, 114.16, 104.46, 93.25, 187.95, 91.07, 0.00, 93.83, 116.63, 112.79, 94.60, 115.79},
            {161.60, 82.91, 178.87, 144.57, 174.02, 113.90, 106.88, 131.92, 185.17, 93.83, 0.00, 152.00, 121.15, 99.40, 161.22},
            {107.80, 108.04, 88.85, 180.81, 183.17, 194.29, 107.98, 116.08, 127.80, 116.63, 152.00, 0.00, 102.62, 120.01, 130.69},
            {146.29, 136.72, 143.56, 186.64, 134.36, 88.94, 111.69, 145.82, 95.20, 112.79, 121.15, 102.62, 0.00, 183.74, 118.55},
            {152.98, 163.69, 146.33, 184.01, 184.90, 197.81, 169.72, 92.53, 186.01, 94.60, 99.40, 120.01, 183.74, 0.00, 84.44},
            {102.73, 191.82, 139.54, 187.34, 199.27, 111.63, 144.48, 127.10, 151.55, 115.79, 161.22, 130.69, 118.55, 84.44, 0.00},
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

package com.chuxu.BackTrackingAlgorithmWithReduction.part01_preHandleData.RandomCase;

import com.chuxu.entity.Facility;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class N10_3 {
    public static int dimension = 10;
    public static final double[] ci = {738.0, 714.0, 835.0, 549.0, 424.0, 554.0, 222.0, 129.0, 754.0, 383.0};
    public static final double[] pi = {972.0, 873.0, 546.0, 340.0, 901.0, 441.0, 866.0, 898.0, 966.0, 982.0};
    public static final LinkedHashSet<Facility> V = new LinkedHashSet<>();
    public static double[][] disMatrix = {
            {0.00, 148.94, 157.99, 91.14, 147.38, 156.78, 85.33, 198.12, 174.31, 138.98},
            {148.94, 0.00, 183.20, 146.61, 150.01, 113.07, 106.88, 93.71, 81.81, 194.71},
            {157.99, 183.20, 0.00, 183.77, 129.02, 158.09, 92.28, 111.01, 120.67, 156.43},
            {91.14, 146.61, 183.77, 0.00, 115.40, 121.57, 142.34, 195.14, 87.14, 143.92},
            {147.38, 150.01, 129.02, 115.40, 0.00, 187.95, 117.73, 162.33, 104.91, 185.38},
            {156.78, 113.07, 158.09, 121.57, 187.95, 0.00, 191.34, 140.44, 154.48, 178.83},
            {85.33, 106.88, 92.28, 142.34, 117.73, 191.34, 0.00, 95.92, 102.18, 142.70},
            {198.12, 93.71, 111.01, 195.14, 162.33, 140.44, 95.92, 0.00, 161.19, 139.52},
            {174.31, 81.81, 120.67, 87.14, 104.91, 154.48, 102.18, 161.19, 0.00, 132.18},
            {138.98, 194.71, 156.43, 143.92, 185.38, 178.83, 142.70, 139.52, 132.18, 0.00},
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

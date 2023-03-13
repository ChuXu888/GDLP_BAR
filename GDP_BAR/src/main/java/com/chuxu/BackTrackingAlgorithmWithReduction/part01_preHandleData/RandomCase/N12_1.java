package com.chuxu.BackTrackingAlgorithmWithReduction.part01_preHandleData.RandomCase;

import com.chuxu.entity.Facility;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class N12_1 {
    public static int dimension = 12;
    public static final double[] ci =
            {108.0, 769.0, 885.0, 405.0, 458.0, 437.0, 662.0, 630.0, 406.0, 314.0, 530.0, 155.0};
    public static final double[] pi =
            {786.0, 785.0, 790.0, 995.0, 427.0, 343.0, 827.0, 649.0, 658.0, 580.0, 770.0, 302.0};
    public static final LinkedHashSet<Facility> V = new LinkedHashSet<>();
    public static double[][] disMatrix = {
            {0.00, 98.75, 92.70, 93.28, 170.87, 158.84, 148.74, 189.96, 102.10, 116.15, 188.90, 145.16},
            {98.75, 0.00, 153.04, 108.60, 89.96, 121.63, 108.88, 159.86, 199.16, 88.93, 120.69, 199.03},
            {92.70, 153.04, 0.00, 103.08, 165.06, 84.48, 90.83, 142.27, 83.00, 158.57, 109.51, 81.57},
            {93.28, 108.60, 103.08, 0.00, 81.73, 85.22, 105.21, 178.75, 158.34, 191.99, 181.25, 150.88},
            {170.87, 89.96, 165.06, 81.73, 0.00, 149.66, 162.16, 103.83, 98.37, 96.61, 120.49, 142.64},
            {158.84, 121.63, 84.48, 85.22, 149.66, 0.00, 110.63, 183.65, 121.98, 151.91, 125.29, 197.96},
            {148.74, 108.88, 90.83, 105.21, 162.16, 110.63, 0.00, 195.96, 85.45, 164.74, 159.75, 166.22},
            {189.96, 159.86, 142.27, 178.75, 103.83, 183.65, 195.96, 0.00, 157.93, 168.89, 174.96, 100.63},
            {102.10, 199.16, 83.00, 158.34, 98.37, 121.98, 85.45, 157.93, 0.00, 156.30, 82.52, 175.49},
            {116.15, 88.93, 158.57, 191.99, 96.61, 151.91, 164.74, 168.89, 156.30, 0.00, 159.31, 171.16},
            {188.90, 120.69, 109.51, 181.25, 120.49, 125.29, 159.75, 174.96, 82.52, 159.31, 0.00, 198.33},
            {145.16, 199.03, 81.57, 150.88, 142.64, 197.96, 166.22, 100.63, 175.49, 171.16, 198.33, 0.00},
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

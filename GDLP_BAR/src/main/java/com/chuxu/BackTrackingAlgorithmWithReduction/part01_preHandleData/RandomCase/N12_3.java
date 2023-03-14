package com.chuxu.BackTrackingAlgorithmWithReduction.part01_preHandleData.RandomCase;

import com.chuxu.entity.Facility;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class N12_3 {
    public static int dimension = 12;
    public static final double[] ci =
            {911.0, 231.0, 879.0, 278.0, 641.0, 617.0, 390.0, 271.0, 969.0, 978.0, 343.0, 120.0};
    public static final double[] pi =
            {745.0, 481.0, 213.0, 560.0, 253.0, 181.0, 699.0, 433.0, 595.0, 793.0, 696.0, 628.0};
    public static final LinkedHashSet<Facility> V = new LinkedHashSet<>();
    public static double[][] disMatrix = {
            {0.00, 166.69, 116.37, 137.29, 91.32, 119.56, 147.57, 163.71, 188.27, 155.36, 107.17, 169.67},
            {166.69, 0.00, 104.36, 86.65, 179.97, 161.47, 166.64, 141.41, 129.28, 107.49, 86.20, 86.01},
            {116.37, 104.36, 0.00, 184.15, 165.58, 110.75, 174.40, 89.09, 177.62, 194.48, 83.63, 123.92},
            {137.29, 86.65, 184.15, 0.00, 193.28, 162.97, 178.20, 136.54, 179.58, 91.58, 82.45, 109.81},
            {91.32, 179.97, 165.58, 193.28, 0.00, 101.21, 142.13, 189.96, 185.89, 178.31, 151.14, 162.99},
            {119.56, 161.47, 110.75, 162.97, 101.21, 0.00, 157.83, 144.48, 136.30, 152.73, 167.51, 176.92},
            {147.57, 166.64, 174.40, 178.20, 142.13, 157.83, 0.00, 104.17, 162.10, 83.12, 94.16, 102.90},
            {163.71, 141.41, 89.09, 136.54, 189.96, 144.48, 104.17, 0.00, 158.42, 126.08, 93.69, 127.56},
            {188.27, 129.28, 177.62, 179.58, 185.89, 136.30, 162.10, 158.42, 0.00, 141.79, 99.04, 172.37},
            {155.36, 107.49, 194.48, 91.58, 178.31, 152.73, 83.12, 126.08, 141.79, 0.00, 133.94, 147.84},
            {107.17, 86.20, 83.63, 82.45, 151.14, 167.51, 94.16, 93.69, 99.04, 133.94, 0.00, 150.67},
            {169.67, 86.01, 123.92, 109.81, 162.99, 176.92, 102.90, 127.56, 172.37, 147.84, 150.67, 0.00},
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

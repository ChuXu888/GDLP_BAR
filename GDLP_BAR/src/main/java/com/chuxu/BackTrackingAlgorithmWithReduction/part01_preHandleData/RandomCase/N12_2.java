package com.chuxu.BackTrackingAlgorithmWithReduction.part01_preHandleData.RandomCase;

import com.chuxu.entity.Facility;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class N12_2 {
    public static int dimension = 12;
    public static final double[] ci =
            {480.0, 773.0, 537.0, 176.0, 564.0, 564.0, 775.0, 507.0, 473.0, 461.0, 501.0, 223.0};
    public static final double[] pi =
            {316.0, 164.0, 343.0, 553.0, 504.0, 444.0, 914.0, 801.0, 301.0, 243.0, 183.0, 396.0};
    public static final LinkedHashSet<Facility> V = new LinkedHashSet<>();
    public static double[][] disMatrix = {
            {0.00, 123.55, 89.33, 185.54, 101.97, 143.70, 173.77, 88.22, 89.25, 136.66, 96.56, 185.80},
            {123.55, 0.00, 160.66, 160.36, 128.46, 88.77, 100.04, 184.00, 170.46, 157.84, 186.44, 169.29},
            {89.33, 160.66, 0.00, 139.57, 183.88, 135.03, 93.16, 163.55, 167.95, 170.49, 122.30, 175.43},
            {185.54, 160.36, 139.57, 0.00, 155.72, 97.63, 120.71, 166.75, 152.77, 158.19, 155.89, 129.86},
            {101.97, 128.46, 183.88, 155.72, 0.00, 154.12, 151.77, 187.79, 155.46, 140.86, 150.75, 156.11},
            {143.70, 88.77, 135.03, 97.63, 154.12, 0.00, 130.28, 171.32, 155.56, 187.94, 163.04, 101.07},
            {173.77, 100.04, 93.16, 120.71, 151.77, 130.28, 0.00, 135.94, 146.62, 173.52, 128.13, 127.96},
            {88.22, 184.00, 163.55, 166.75, 187.79, 171.32, 135.94, 0.00, 156.41, 89.66, 194.36, 83.34},
            {89.25, 170.46, 167.95, 152.77, 155.46, 155.56, 146.62, 156.41, 0.00, 184.13, 132.28, 163.12},
            {136.66, 157.84, 170.49, 158.19, 140.86, 187.94, 173.52, 89.66, 184.13, 0.00, 159.02, 80.20},
            {96.56, 186.44, 122.30, 155.89, 150.75, 163.04, 128.13, 194.36, 132.28, 159.02, 0.00, 84.52},
            {185.80, 169.29, 175.43, 129.86, 156.11, 101.07, 127.96, 83.34, 163.12, 80.20, 84.52, 0.00},
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

package com.chuxu.BackTrackingAlgorithmWithReduction.part01_preHandleData.RandomCase;

import com.chuxu.entity.Facility;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class N12_4 {
    public static int dimension = 12;
    public static final double[] ci =
            {828.0,379.0,990.0,878.0,455.0,569.0,463.0,958.0,759.0,495.0,991.0,292.0};
    public static final double[] pi =
            {336.0,225.0,404.0,557.0,195.0,180.0,947.0,239.0,508.0,130.0,183.0,619.0};
    public static final LinkedHashSet<Facility> V = new LinkedHashSet<>();
    public static double[][] disMatrix = {
            {   0.00, 111.85, 189.48, 165.52, 108.16, 181.11,  94.31, 118.81, 139.09, 144.50, 152.71,  95.38},
            { 111.85,   0.00, 186.99, 141.13, 145.35, 112.18,  87.38, 162.64, 124.04, 111.55, 151.04, 177.45},
            { 189.48, 186.99,   0.00, 115.86,  86.48, 127.62, 101.19, 195.07, 119.22,  90.36, 109.67, 104.82},
            { 165.52, 141.13, 115.86,   0.00, 136.33, 105.94,  97.60, 144.87, 132.13,  83.90, 106.98, 199.47},
            { 108.16, 145.35,  86.48, 136.33,   0.00, 135.01,  95.25, 123.87, 161.15,  87.53, 108.38, 130.01},
            { 181.11, 112.18, 127.62, 105.94, 135.01,   0.00, 171.88,  81.31, 112.53,  98.15,  91.70, 191.56},
            {  94.31,  87.38, 101.19,  97.60,  95.25, 171.88,   0.00, 145.37, 135.65,  98.55,  93.78,  90.81},
            { 118.81, 162.64, 195.07, 144.87, 123.87,  81.31, 145.37,   0.00,  90.03, 159.08, 156.51, 186.98},
            { 139.09, 124.04, 119.22, 132.13, 161.15, 112.53, 135.65,  90.03,   0.00, 193.98, 170.99, 109.03},
            { 144.50, 111.55,  90.36,  83.90,  87.53,  98.15,  98.55, 159.08, 193.98,   0.00, 181.29, 143.84},
            { 152.71, 151.04, 109.67, 106.98, 108.38,  91.70,  93.78, 156.51, 170.99, 181.29,   0.00, 101.63},
            {  95.38, 177.45, 104.82, 199.47, 130.01, 191.56,  90.81, 186.98, 109.03, 143.84, 101.63,   0.00},
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

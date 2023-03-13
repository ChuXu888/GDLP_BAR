package com.chuxu.BackTrackingAlgorithmWithReduction.part01_preHandleData.SmallCase;

import com.chuxu.entity.Facility;

import java.util.LinkedHashSet;

public class SmallCase {
    public static int dimension = 6;
    public static final double[] pi = {230, 82, 268, 273, 227, 127};
    public static final double[] ci = {520, 232, 89, 229, 262, 141};  //273
    public static final LinkedHashSet<Facility> V = new LinkedHashSet<>();
    public static double[][] disMatrix = {
            {0.0, 4.5, 7.4, 9.1, 5.1, 6.9},
            {4.5, 0.0, 7.8, 4.8, 8.2, 8.1},
            {7.4, 7.8, 0.0, 5.8, 9.8, 9.3},
            {9.1, 4.8, 5.8, 0.0, 7.2, 7.9},
            {5.1, 8.2, 9.8, 7.2, 0.0, 6.0},
            {6.9, 8.1, 9.3, 7.9, 6.0, 0.0},
    };
    public static double P = 500;  //540
    public static double C = 500;  //480需要贪心修复，500不需要贪心修复

    public static void preHandle() {
        for (int i = 0; i < pi.length; i++) {
            V.add(new Facility(i + 1, ci[i], pi[i], pi[i] / ci[i]));
        }
    }
}

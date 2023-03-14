package com.chuxu.BackTrackingAlgorithmWithReduction.part01_preHandleData.RandomCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomTool {

    public static int n = 21;  //问题规模
    public static double[][] disMatrix = new double[n][n];  //距离矩阵
    public static final List<Double> pi = new ArrayList<>();  //节点列表
    public static final List<Double> ci = new ArrayList<>();  //节点列表
    public static final Random random = new Random();  //随机数工具

    public static void main(String[] args) {
        initialCase();
        formatPrint(ci);
        formatPrint(pi);
        for (double[] matrix : disMatrix) {
            System.out.print("{");
            for (int j = 0; j < disMatrix[0].length; j++) {
                if (j == disMatrix[0].length - 1) {
                    System.out.printf("%7.2f", matrix[j]);
                } else {
                    System.out.printf("%7.2f,", matrix[j]);
                }
            }
            System.out.println("},");
        }
    }

    private static void formatPrint(List<Double> pi) {
        System.out.print("{");
        for (int i = 0; i < pi.size(); i++) {
            if (i == pi.size() - 1) {
                System.out.print(pi.get(i));
            } else {
                System.out.print(pi.get(i) + ",");
            }
        }
        System.out.println("};");
    }

    //1.初始化邻接矩阵
    public static void initialCase() {
        //1.生成随机的距离矩阵
//        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        for (int i = 0; i < disMatrix.length; i++) {
            for (int j = i + 1; j < disMatrix[0].length; j++) {
//                disMatrix[i][j] = disMatrix[j][i] = Double.parseDouble(decimalFormat.format(random.nextDouble() * 120 + 80));
                disMatrix[i][j] = disMatrix[j][i] = random.nextDouble() * 120 + 80;
            }
        }
        //2.生成随机的pi和ci
        for (int i = 0; i < n; i++) {
//            pi.add((double) (random.nextInt(1000) + 200));
//            ci.add((double) (random.nextInt(1000) + 200));
            ci.add((double) (random.nextInt(15) + 5));
            pi.add((double) (random.nextInt(400) + 100));
        }
    }

}
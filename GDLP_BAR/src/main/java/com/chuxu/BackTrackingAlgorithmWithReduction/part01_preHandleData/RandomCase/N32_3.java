package com.chuxu.BackTrackingAlgorithmWithReduction.part01_preHandleData.RandomCase;

import com.chuxu.entity.Facility;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class N32_3 {
    public static int dimension = 32;
    public static final double[] ci =
            {730.0, 1026.0, 716.0, 1095.0, 1083.0, 1078.0, 1014.0, 298.0, 1039.0, 995.0, 407.0, 1088.0, 469.0, 591.0, 555.0, 523.0, 1190.0, 1003.0, 1119.0, 1059.0, 499.0, 361.0, 675.0, 559.0, 665.0, 408.0, 528.0, 944.0, 494.0, 892.0, 321.0, 1051.0};
    public static final double[] pi =
            {565.0, 518.0, 1042.0, 1001.0, 753.0, 751.0, 1100.0, 552.0, 410.0, 613.0, 1116.0, 366.0, 229.0, 696.0, 458.0, 771.0, 928.0, 690.0, 1165.0, 1036.0, 1135.0, 623.0, 1161.0, 804.0, 780.0, 581.0, 562.0, 939.0, 566.0, 580.0, 239.0, 538.0};
    public static final LinkedHashSet<Facility> V = new LinkedHashSet<>();
    public static double[][] disMatrix = {
            {0.00, 195.40, 154.77, 137.05, 163.39, 146.08, 198.18, 149.29, 181.10, 197.76, 145.79, 147.44, 152.94, 180.32, 183.57, 107.78, 182.08, 115.81, 99.36, 175.26, 154.58, 150.77, 100.05, 183.64, 104.68, 112.67, 178.92, 149.73, 119.47, 93.91, 150.57, 80.32},
            {195.40, 0.00, 134.88, 120.43, 80.85, 145.38, 122.24, 128.74, 193.84, 160.14, 115.80, 115.94, 195.54, 101.13, 166.34, 81.98, 195.68, 137.78, 104.50, 131.40, 154.72, 191.14, 119.95, 83.96, 151.84, 175.26, 88.32, 199.23, 83.12, 194.38, 187.83, 189.68},
            {154.77, 134.88, 0.00, 135.44, 100.27, 121.08, 100.23, 182.32, 199.57, 194.56, 108.93, 191.83, 179.35, 129.82, 134.37, 184.92, 92.16, 184.75, 159.97, 193.93, 187.88, 112.26, 83.30, 126.76, 100.12, 195.57, 150.83, 160.98, 148.38, 155.81, 164.04, 195.48},
            {137.05, 120.43, 135.44, 0.00, 145.25, 87.14, 122.52, 133.71, 138.95, 98.22, 122.73, 83.02, 96.72, 106.23, 198.26, 168.84, 83.62, 120.65, 87.96, 100.36, 173.56, 128.75, 173.14, 85.97, 106.16, 113.46, 112.77, 102.29, 172.81, 116.43, 108.25, 189.68},
            {163.39, 80.85, 100.27, 145.25, 0.00, 139.29, 125.20, 164.46, 100.12, 190.10, 153.28, 103.60, 139.96, 150.73, 155.34, 191.65, 110.55, 129.31, 137.20, 93.97, 133.89, 98.16, 101.16, 167.57, 178.40, 128.04, 192.24, 182.00, 191.50, 194.47, 99.25, 158.93},
            {146.08, 145.38, 121.08, 87.14, 139.29, 0.00, 101.32, 95.66, 137.01, 132.69, 88.78, 175.20, 92.15, 139.59, 94.41, 190.60, 94.62, 163.50, 110.89, 129.89, 120.56, 138.22, 182.70, 133.68, 159.75, 154.36, 188.44, 89.03, 111.54, 139.10, 99.48, 84.03},
            {198.18, 122.24, 100.23, 122.52, 125.20, 101.32, 0.00, 188.21, 191.65, 169.88, 148.24, 123.30, 145.49, 178.50, 194.54, 84.48, 110.60, 172.90, 181.55, 116.92, 124.46, 157.87, 193.44, 152.72, 170.09, 107.93, 145.47, 155.51, 182.79, 163.77, 128.62, 146.78},
            {149.29, 128.74, 182.32, 133.71, 164.46, 95.66, 188.21, 0.00, 104.19, 136.51, 85.38, 161.36, 152.75, 131.86, 81.72, 124.50, 184.73, 137.73, 186.29, 191.35, 197.41, 194.40, 168.33, 121.98, 158.93, 125.51, 159.33, 181.35, 114.22, 150.99, 131.33, 133.11},
            {181.10, 193.84, 199.57, 138.95, 100.12, 137.01, 191.65, 104.19, 0.00, 116.47, 109.83, 166.03, 179.87, 86.57, 130.99, 143.94, 109.97, 170.13, 84.81, 111.82, 81.50, 142.15, 145.50, 149.14, 89.49, 184.30, 136.59, 170.64, 180.62, 167.56, 96.94, 132.38},
            {197.76, 160.14, 194.56, 98.22, 190.10, 132.69, 169.88, 136.51, 116.47, 0.00, 101.83, 95.44, 94.05, 152.32, 175.88, 198.27, 164.11, 154.13, 145.98, 198.92, 95.29, 154.49, 165.81, 133.29, 190.81, 198.98, 188.09, 86.20, 112.82, 170.67, 126.18, 93.55},
            {145.79, 115.80, 108.93, 122.73, 153.28, 88.78, 148.24, 85.38, 109.83, 101.83, 0.00, 95.88, 168.19, 167.90, 106.82, 110.49, 196.30, 149.37, 153.43, 112.75, 132.17, 148.79, 139.24, 135.10, 124.46, 156.50, 124.52, 176.34, 149.81, 176.99, 84.82, 155.59},
            {147.44, 115.94, 191.83, 83.02, 103.60, 175.20, 123.30, 161.36, 166.03, 95.44, 95.88, 0.00, 192.92, 102.54, 100.88, 85.34, 191.21, 165.21, 169.17, 92.95, 134.31, 164.94, 105.02, 177.89, 130.73, 146.48, 125.07, 153.61, 131.10, 196.36, 83.80, 128.50},
            {152.94, 195.54, 179.35, 96.72, 139.96, 92.15, 145.49, 152.75, 179.87, 94.05, 168.19, 192.92, 0.00, 166.81, 193.31, 81.14, 169.42, 107.14, 151.80, 93.48, 104.33, 135.59, 167.94, 130.56, 157.19, 157.33, 110.74, 183.12, 148.69, 174.77, 98.95, 144.02},
            {180.32, 101.13, 129.82, 106.23, 150.73, 139.59, 178.50, 131.86, 86.57, 152.32, 167.90, 102.54, 166.81, 0.00, 140.64, 147.44, 104.64, 115.60, 169.70, 184.58, 89.80, 101.05, 187.85, 145.47, 110.35, 175.42, 182.30, 122.58, 134.74, 164.12, 87.14, 136.47},
            {183.57, 166.34, 134.37, 198.26, 155.34, 94.41, 194.54, 81.72, 130.99, 175.88, 106.82, 100.88, 193.31, 140.64, 0.00, 119.91, 142.52, 121.24, 198.95, 127.58, 109.50, 154.36, 142.51, 107.13, 141.38, 133.32, 189.00, 138.71, 159.34, 134.46, 99.44, 101.44},
            {107.78, 81.98, 184.92, 168.84, 191.65, 190.60, 84.48, 124.50, 143.94, 198.27, 110.49, 85.34, 81.14, 147.44, 119.91, 0.00, 144.50, 126.65, 109.63, 105.94, 128.96, 150.49, 192.24, 129.60, 99.66, 145.08, 96.54, 154.60, 193.77, 122.84, 100.65, 175.52},
            {182.08, 195.68, 92.16, 83.62, 110.55, 94.62, 110.60, 184.73, 109.97, 164.11, 196.30, 191.21, 169.42, 104.64, 142.52, 144.50, 0.00, 159.99, 165.53, 108.69, 152.31, 129.89, 111.79, 196.82, 98.86, 166.24, 106.43, 117.02, 133.95, 152.79, 106.26, 86.62},
            {115.81, 137.78, 184.75, 120.65, 129.31, 163.50, 172.90, 137.73, 170.13, 154.13, 149.37, 165.21, 107.14, 115.60, 121.24, 126.65, 159.99, 0.00, 82.79, 80.27, 145.06, 143.87, 142.31, 148.06, 91.79, 116.46, 171.33, 99.24, 151.91, 150.02, 99.82, 118.66},
            {99.36, 104.50, 159.97, 87.96, 137.20, 110.89, 181.55, 186.29, 84.81, 145.98, 153.43, 169.17, 151.80, 169.70, 198.95, 109.63, 165.53, 82.79, 0.00, 180.65, 97.19, 126.84, 169.44, 199.87, 155.99, 116.91, 142.78, 137.21, 86.96, 148.12, 89.21, 176.33},
            {175.26, 131.40, 193.93, 100.36, 93.97, 129.89, 116.92, 191.35, 111.82, 198.92, 112.75, 92.95, 93.48, 184.58, 127.58, 105.94, 108.69, 80.27, 180.65, 0.00, 174.29, 112.37, 86.51, 181.28, 184.56, 139.46, 100.00, 156.15, 159.42, 171.29, 185.68, 182.60},
            {154.58, 154.72, 187.88, 173.56, 133.89, 120.56, 124.46, 197.41, 81.50, 95.29, 132.17, 134.31, 104.33, 89.80, 109.50, 128.96, 152.31, 145.06, 97.19, 174.29, 0.00, 155.22, 171.26, 94.89, 103.41, 92.22, 95.98, 160.86, 149.41, 100.31, 122.66, 163.78},
            {150.77, 191.14, 112.26, 128.75, 98.16, 138.22, 157.87, 194.40, 142.15, 154.49, 148.79, 164.94, 135.59, 101.05, 154.36, 150.49, 129.89, 143.87, 126.84, 112.37, 155.22, 0.00, 139.98, 105.45, 115.35, 199.97, 144.00, 172.82, 126.53, 184.40, 128.78, 107.16},
            {100.05, 119.95, 83.30, 173.14, 101.16, 182.70, 193.44, 168.33, 145.50, 165.81, 139.24, 105.02, 167.94, 187.85, 142.51, 192.24, 111.79, 142.31, 169.44, 86.51, 171.26, 139.98, 0.00, 188.22, 92.56, 188.21, 130.40, 107.45, 158.45, 109.87, 188.65, 166.05},
            {183.64, 83.96, 126.76, 85.97, 167.57, 133.68, 152.72, 121.98, 149.14, 133.29, 135.10, 177.89, 130.56, 145.47, 107.13, 129.60, 196.82, 148.06, 199.87, 181.28, 94.89, 105.45, 188.22, 0.00, 150.09, 151.99, 147.43, 93.07, 142.00, 157.09, 133.77, 110.52},
            {104.68, 151.84, 100.12, 106.16, 178.40, 159.75, 170.09, 158.93, 89.49, 190.81, 124.46, 130.73, 157.19, 110.35, 141.38, 99.66, 98.86, 91.79, 155.99, 184.56, 103.41, 115.35, 92.56, 150.09, 0.00, 136.28, 127.77, 138.14, 129.26, 89.44, 173.51, 96.19},
            {112.67, 175.26, 195.57, 113.46, 128.04, 154.36, 107.93, 125.51, 184.30, 198.98, 156.50, 146.48, 157.33, 175.42, 133.32, 145.08, 166.24, 116.46, 116.91, 139.46, 92.22, 199.97, 188.21, 151.99, 136.28, 0.00, 91.43, 141.93, 143.82, 152.28, 99.69, 80.19},
            {178.92, 88.32, 150.83, 112.77, 192.24, 188.44, 145.47, 159.33, 136.59, 188.09, 124.52, 125.07, 110.74, 182.30, 189.00, 96.54, 106.43, 171.33, 142.78, 100.00, 95.98, 144.00, 130.40, 147.43, 127.77, 91.43, 0.00, 173.39, 101.39, 140.49, 168.34, 139.42},
            {149.73, 199.23, 160.98, 102.29, 182.00, 89.03, 155.51, 181.35, 170.64, 86.20, 176.34, 153.61, 183.12, 122.58, 138.71, 154.60, 117.02, 99.24, 137.21, 156.15, 160.86, 172.82, 107.45, 93.07, 138.14, 141.93, 173.39, 0.00, 115.13, 140.09, 107.30, 188.79},
            {119.47, 83.12, 148.38, 172.81, 191.50, 111.54, 182.79, 114.22, 180.62, 112.82, 149.81, 131.10, 148.69, 134.74, 159.34, 193.77, 133.95, 151.91, 86.96, 159.42, 149.41, 126.53, 158.45, 142.00, 129.26, 143.82, 101.39, 115.13, 0.00, 140.17, 106.29, 133.45},
            {93.91, 194.38, 155.81, 116.43, 194.47, 139.10, 163.77, 150.99, 167.56, 170.67, 176.99, 196.36, 174.77, 164.12, 134.46, 122.84, 152.79, 150.02, 148.12, 171.29, 100.31, 184.40, 109.87, 157.09, 89.44, 152.28, 140.49, 140.09, 140.17, 0.00, 194.86, 93.61},
            {150.57, 187.83, 164.04, 108.25, 99.25, 99.48, 128.62, 131.33, 96.94, 126.18, 84.82, 83.80, 98.95, 87.14, 99.44, 100.65, 106.26, 99.82, 89.21, 185.68, 122.66, 128.78, 188.65, 133.77, 173.51, 99.69, 168.34, 107.30, 106.29, 194.86, 0.00, 87.89},
            {80.32, 189.68, 195.48, 189.68, 158.93, 84.03, 146.78, 133.11, 132.38, 93.55, 155.59, 128.50, 144.02, 136.47, 101.44, 175.52, 86.62, 118.66, 176.33, 182.60, 163.78, 107.16, 166.05, 110.52, 96.19, 80.19, 139.42, 188.79, 133.45, 93.61, 87.89, 0.00},
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

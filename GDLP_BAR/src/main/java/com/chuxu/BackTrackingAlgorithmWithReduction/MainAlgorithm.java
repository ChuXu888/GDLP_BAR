package com.chuxu.BackTrackingAlgorithmWithReduction;

import com.chuxu.BackTrackingAlgorithmWithReduction.part00_entity.TabuCouple;
import com.chuxu.BackTrackingAlgorithmWithReduction.part01_preHandleData.RandomCase.N35_1;
import com.chuxu.BackTrackingAlgorithmWithReduction.part02_properties.Properties;
import com.chuxu.BackTrackingAlgorithmWithReduction.part03_lowerBoundAlgorithm.LowerBoundAlgorithm;
import com.chuxu.BackTrackingAlgorithmWithReduction.part05_orderReductionAlgorithm.OrderReductionAlgorithm;
import com.chuxu.BackTrackingAlgorithmWithReduction.part06_backTrackingAlgorithm.BackTrackingAlgorithm;
import com.chuxu.entity.Facility;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class MainAlgorithm {
    //参数
    public static double[][] disMatrix;  //邻接矩阵
    public static double P;  //这两个数值作为总数，保持不变
    public static double C;  //这两个数值作为总数，保持不变
    public static double Pr;  //这两个数值实时更新
    public static double Cr;  //这两个数值实时更新
    public static final double NSFlag = -1.0;  //上界子算法无解时的返回值
    //集合
    public static LinkedHashSet<Facility> V = new LinkedHashSet<>();
    public static List<Facility> V_list = new ArrayList<>();
    public static LinkedHashSet<Facility> V1 = new LinkedHashSet<>();  //一定加入最小支配阈值集的集合
    public static LinkedHashSet<Facility> V0 = new LinkedHashSet<>();  //一定不加入最小支配阈值集的集合
    public static LinkedHashSet<Facility> V5 = new LinkedHashSet<>();  //V5=V\V1\V0
    public static LinkedHashSet<Facility> VV1 = new LinkedHashSet<>();  //假设加入最小支配阈值集的集合
    public static LinkedHashSet<Facility> VV0 = new LinkedHashSet<>();  //假设不加入最小支配阈值集的集合
    public static LinkedHashSet<Facility> VV5 = new LinkedHashSet<>();  //VV5=V\V1\V0\VV1\VV0，现在将VV5当作初始不动的V使用，而将原本应该不动的V当作V和VV5在使用
    public static LinkedHashSet<Facility> S_best = new LinkedHashSet<>();  //当前状态下已知最优目标值对应的开设设施s合
    public static LinkedHashSet<TabuCouple> ConflictList = new LinkedHashSet<>();  //性质5确定的冲突表
    //变量
    public static double b = Double.MAX_VALUE;  //全局下界
    public static int a = 0;  //该问题中的任一可行解中至少包含(a+1)个备选点
    public static int count = 0;  //二叉树搜索次数(包含根结点)
    public static int countOfLeaf = 0;  //二叉树叶子结点搜索次数
    public static int countOfProperty01 = 0;  //回溯搜索时性质1使用次数
    public static int countOfProperty02 = 0;  //回溯搜索时性质2使用次数
    public static int countOfProperty03 = 0;  //回溯搜索时性质3使用次数
    public static int countOfProperty04 = 0;  //回溯搜索时性质4使用次数
    public static int countOfConflictList = 0;  //回溯搜索时ConflictList生效次数
    public static int countOfUpLowBound = 0;  //回溯搜索时上下界剪枝生效次数

    public static void main(String[] args) throws Exception {
        //记录程序结束时间
        long start = System.currentTimeMillis();

        //Step1：数据预处理和初始化
        N35_1.preHandle();
        deepCopy();
        System.out.println("P = " + P);
        System.out.println("C = " + C);
//        V.forEach(System.out::println);
//        for (double[] matrix : disMatrix) System.out.println(Arrays.toString(matrix));

        //Step2：判断给定的图G=(V, E)是否满足性质1的条件，若满足，则该问题无可行解，主算法结束；
        if (Properties.property02(V, V1, VV1)) {
            System.out.println("所有可能开设设施的总容量都小于P");
            System.exit(0);
        }

        //Step3：判断给定的图G=(V, E)是否满足性质4的条件，若满足且满足成本约束，则S*=V1=V，主算法结束；
        if (Properties.property04(V, V1, VV1)) {
            double wholeCi = 0.0;
            for (Facility facility : V) {
                wholeCi += facility.getCi();
            }
            if (wholeCi <= C) {
                System.out.println("直接开设V中的所有设施！");
                S_best = V;
                System.exit(0);
            }
        }

        //Step4：根据性质10确定该问题a的取值；
        Properties.property10();

        //Step5：调用下界子算法，获取问题的下界b；
        b = LowerBoundAlgorithm.lowerBoundAlgorithm();
        System.out.println("下界子算法得到的当前最优值为b = " + b);
        System.out.println("下界子算法得到的当前最好解为S_best = ");
        S_best.forEach(System.out::println);

        //Step6：调用降阶子算法，对问题进行降阶；
        OrderReductionAlgorithm.orderReductionAlgorithm();

        //Step7：调用回溯子算法Backtrack(1)，获取问题的最优解S*=S_best。
        initialList();
        BackTrackingAlgorithm.backTrackingAlgorithm();

        //记录程序结束时间
        long end = System.currentTimeMillis();

        //打印结果相关变量
        System.out.println("==================================================================================");
        System.out.println("V1.size() = " + V1.size());
        System.out.println("V0.size() = " + V0.size());
        System.out.println("进入回溯子算法中进行判断的VV5.size() = " + (VV5.size() - V0.size() - V1.size()));
        System.out.println("二叉树搜索次数为 = " + count);
        System.out.println("二叉树叶子结点搜索次数为 = " + countOfLeaf);
//        System.out.println("回溯搜索时性质1触发次数为 = " + countOfProperty01);
        System.out.println("回溯搜索时性质2触发次数为 = " + countOfProperty02);
        System.out.println("回溯搜索时性质3触发次数为 = " + countOfProperty03);
        System.out.println("回溯搜索时性质4触发次数为 = " + countOfProperty04);
        System.out.println("回溯搜索时countOfConflictList触发次数为 = " + countOfConflictList);
        System.out.println("回溯搜索时通过上下界剪枝次数为 = " + countOfUpLowBound);
//        System.out.println("countOfConflictList中的元素个数 = " + ConflictList.size());
        if (!S_best.isEmpty()) {
//            System.out.println("最优解为：");
//            S_best.forEach(System.out::println);
            System.out.println("最优值为：" + b);
        } else {
            System.out.println("该问题无解！");
        }
        System.out.println("程序运行总时间为：" + (end - start) / 1000.0 + "s");
        System.out.println("P = " + P);
        System.out.println("C = " + C);
        System.out.println("a + 1 = " + (a + 1));
    }

    //初始化V_list
    public static void initialList() {
        V_list.addAll(V);
        //进入回溯算法之前先对VV5=V5里的结点按照性价比排序，从而可以尽快得到一个优质的可行解，增加更新下界的概率
        V_list = V_list.stream().sorted(Comparator.comparing(Facility::getValueDensity)).collect(Collectors.toList());
        System.out.println("================================================================");
        System.out.println("回溯搜索前将VV5中所有备选点按性价比排序：");
        V_list.forEach(System.out::println);
    }

    //插入排序
    public static void insertSort(List<Facility> V_list) {
        for (int i = 1; i < V_list.size(); i++) {
            //第i轮
            Facility insertVal = V_list.get(i);
            int insertIndex = i - 1;
            //给insertVal找到插入的位置
            //1.这句话保证给insertVal找插入位置时，不越界
            //2.arr[1] < arr[0]（3 < 5），即待插入的数还没有找到适当的位置，arr[insertIndex]需要往后移动
            while (insertIndex >= 0 && insertVal.getId() < V_list.get(insertIndex).getId()) {
                //同选择排序类似，一偶遇之前定义了insertVal，所以此处的交换无需构造辅助变量
                V_list.set(insertIndex + 1, V_list.get(insertIndex));
                V_list.set(insertIndex, insertVal);
                insertIndex--;  //继续让insertVal往前移
            }
        }
    }

    //打印所有
    public static void printAll() {
        System.out.println("============================================================================");
        System.out.println("打印当前全局变量：");
        System.out.println("V：");
        V.forEach(System.out::println);
        System.out.println("============================================");
        System.out.println("V1：");
        V1.forEach(System.out::println);
        System.out.println("============================================");
        System.out.println("V0：");
        V0.forEach(System.out::println);
        System.out.println("============================================");
        System.out.println("VV1：");
        VV1.forEach(System.out::println);
        System.out.println("============================================");
        System.out.println("VV0：");
        VV0.forEach(System.out::println);
        System.out.println("============================================");
        System.out.println("VV5：");
        VV5.forEach(System.out::println);
    }

    //深拷贝
    public static void deepCopy() {
        disMatrix = N35_1.disMatrix;
        Pr = P = N35_1.P;
        Cr = C = N35_1.C;
        LinkedHashSet<Facility> facilities = N35_1.V;
        for (Facility facility : facilities) {
            V.add(facility.clone());
            VV5.add(facility.clone());
        }
    }
}
package com.chuxu.BackTrackingAlgorithmWithReduction.part06_backTrackingAlgorithm;

import com.chuxu.entity.Facility;
import com.chuxu.BackTrackingAlgorithmWithReduction.part00_entity.TabuCouple;
import com.chuxu.BackTrackingAlgorithmWithReduction.MainAlgorithm;
import com.chuxu.BackTrackingAlgorithmWithReduction.part02_properties.Properties;
import com.chuxu.BackTrackingAlgorithmWithReduction.part03_lowerBoundAlgorithm.LowerBoundAlgorithm;
import com.chuxu.BackTrackingAlgorithmWithReduction.part04_upperBoundAlgorithm.UpperBoundAlgorithm;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.chuxu.BackTrackingAlgorithmWithReduction.MainAlgorithm.*;

public class BackTrackingAlgorithm {
    public static void backTrackingAlgorithm() {
        System.out.println("============================================================================");
        System.out.println("============================================================================");
        System.out.println("现在进入回溯子算法");
        backTracking();
    }

    public static void backTracking() {
        //二叉树结点搜索次数
        count++;
        //Step1：若|VV5|=0，则到达叶子结点
        if (V_list.size() == 0) {
            //叶子节点搜索次数
            countOfLeaf++;
            System.out.println("countOfLeaf = " + countOfLeaf);
            //(1)若满足容量和成本约束，则得到可行解Sk=(V1∪VV1)
            double wholePi = 0.0;
            double wholeCi = 0.0;
            LinkedHashSet<Facility> V1_Add_VV1 = new LinkedHashSet<>(V1);
            V1_Add_VV1.addAll(VV1);
            for (Facility facility : V1_Add_VV1) {
                wholeCi += facility.getCi();
                wholePi += facility.getPi();
            }
            if (wholeCi <= C && wholePi >= P) {
                //(2)得到一个可行解，计算其目标值，下界子算法中有一个求目标值的函数，借用一下
                double z = LowerBoundAlgorithm.calculateFitness(V1_Add_VV1);
                //(3)若Sk的目标函数值z>b，则更新下界b=best_q=z，Sbest=Sk；然后返回上一层；
                if (z > b) {
                    b = z;
                    S_best.clear();
                    S_best.addAll(V1_Add_VV1);
                    Properties.property05(V, V1, VV1, VV0);  //根据新下界更新ConflictList
                }
            }
            //到达叶子节点是肯定要return的
            return;
        }

        //Step2：情况(1)：假设备选点vi开设，VV1=VV1∪{vi}，VV5=VV5\{vi}；
        System.out.println("============================================================================");
        System.out.println("现在进入左子树假设！");
        Facility curFacility = V_list.get(0);
        System.out.println("curFacility = " + curFacility);
        VV1.add(curFacility);
        V.remove(curFacility);
        V_list.remove(curFacility);
        Cr -= curFacility.getCi();
        Pr -= curFacility.getPi();
        //由性质3和ConflictList确定的加入VV0中的结点集
        LinkedHashSet<Facility> closeFacilitiesOfProperty03 = new LinkedHashSet<>();
        LinkedHashSet<Facility> closeFacilitiesOfConflictList = new LinkedHashSet<>();

        //(1)判断此时是否满足性质2的条件，若满足则该情况下无可行解，左子树剪枝；
        //若返回true，则无可行解，左子树剪枝，若返回false则继续
        boolean property01Flag = Properties.property01(V1, VV1);
        if (property01Flag) {
            System.out.println("满足性质1的条件，达到降阶效果");
            countOfProperty01++;
            System.out.println("countOfProperty01 = " + countOfProperty01);
        } else {
            //(2)调用上界子算法计算上界u，若u≠NSFlag且b≤u，则此时可能存在比当前上界更优的解
            double u = UpperBoundAlgorithm.upperBoundAlgorithm(V, V1, V0, VV1, VV0);
            System.out.println("x(" + curFacility.getId() + ") = 1时上界为u = " + u);
            System.out.println("此时下界b = " + b);
            if (u != NSFlag && MainAlgorithm.b <= u) {
                //(3)由性质3得出加入VV0中的备选点集合为closeFacilitiesOfProperty03
                closeFacilitiesOfProperty03 = Properties.property03(V);
                if (!closeFacilitiesOfProperty03.isEmpty()) {
                    //需要判断V_temp是否含有VV5(V)中的结点，也即是否真正的达到了降阶的效果
                    Set<Facility> facilities = closeFacilitiesOfProperty03.stream().filter(facility -> V.contains(facility)).collect(Collectors.toSet());
                    closeFacilitiesOfProperty03.clear();
                    closeFacilitiesOfProperty03.addAll(facilities);
                    //V_temp要含有VV5(V)中的节点，才是真正达到了降阶的效果
                    if (!closeFacilitiesOfProperty03.isEmpty()) {
                        System.out.println("=================================================");
                        System.out.println("通过性质3得到的关闭设施中含有VV5(V)中的节点，达到了降阶效果");
                        countOfProperty03++;
                        System.out.println("countOfProperty03 = " + countOfProperty03);
                        closeFacilitiesOfProperty03.forEach(System.out::println);
                        //更新各集合
                        VV0.addAll(closeFacilitiesOfProperty03);
                        V.removeAll(closeFacilitiesOfProperty03);
                        V_list.removeAll(closeFacilitiesOfProperty03);
                    }
                }
                //(4)由ConflictList表找出VV5中一定不开设的备选点集合为closeFacilitiesOfConflictList
                closeFacilitiesOfConflictList = new LinkedHashSet<>();
                LinkedHashSet<Facility> V1_Add_VV1 = new LinkedHashSet<>(V1);
                V1_Add_VV1.addAll(VV1);
                for (TabuCouple tabuCouple : ConflictList) {
                    Facility vi = tabuCouple.getVi();
                    Facility vj = tabuCouple.getVj();
                    for (Facility facility : V1_Add_VV1) {
                        if (facility.getId() == vi.getId()) {
                            closeFacilitiesOfConflictList.add(vj);
                        }
                        if (facility.getId() == vj.getId()) {
                            closeFacilitiesOfConflictList.add(vi);
                        }
                    }
                }
                if (!closeFacilitiesOfConflictList.isEmpty()) {
                    //需要判断V_temp是否含有VV5(V)中的结点，也即是否真正的达到了降阶的效果
                    Set<Facility> facilities = closeFacilitiesOfConflictList.stream().filter(facility -> V.contains(facility)).collect(Collectors.toSet());
                    closeFacilitiesOfConflictList.clear();
                    closeFacilitiesOfConflictList.addAll(facilities);
                    //V_temp要含有VV5(V)中的节点，才是真正达到了降阶的效果
                    if (!closeFacilitiesOfConflictList.isEmpty()) {
                        System.out.println("=================================================");
                        System.out.println("通过ConflictList得到的关闭设施中含有VV5(V)中的节点，达到了降阶效果");
                        countOfConflictList++;
                        System.out.println("countOfConflictList = " + countOfConflictList);
                        closeFacilitiesOfConflictList.forEach(System.out::println);
                        //更新各集合
                        VV0.addAll(closeFacilitiesOfConflictList);
                        V.removeAll(closeFacilitiesOfConflictList);
                        V_list.removeAll(closeFacilitiesOfConflictList);
                    }
                }
                backTracking();
            } else {
                //否则通过上下界进行剪枝，此处统计次数
                countOfUpLowBound++;
            }
        }

        //Step3：返回上一层前执行VV0=VV0\V_temp，VV1=VV1\{vi}，VV5=VV5∪V_temp∪{vi}；
        if (!closeFacilitiesOfProperty03.isEmpty()) {
            VV0.removeAll(closeFacilitiesOfProperty03);
            V.addAll(closeFacilitiesOfProperty03);
            V_list.addAll(closeFacilitiesOfProperty03);
        }
        if (!closeFacilitiesOfConflictList.isEmpty()) {
            VV0.removeAll(closeFacilitiesOfConflictList);
            V.addAll(closeFacilitiesOfConflictList);
            V_list.addAll(closeFacilitiesOfConflictList);
        }
        Cr += curFacility.getCi();
        Pr += curFacility.getPi();
        VV1.remove(curFacility);
        V.add(curFacility);
        V_list.add(curFacility);  //如果换成存储变量替换，就要把这里注释掉
        //对V_list重新排序，是采用快速排序还是变量周转，还没有想好
//        List<Facility> newV_list = new ArrayList<>();
//        newV_list.add(curFacility);  //先加curFacility
//        newV_list.addAll(V_list);  //再加之前的V_list
//        V_list.clear();  //将F清掉
//        V_list.addAll(newV_list);  //实质上是将原V_list换为了将curFacility加到了第一位的新F
//        V_list = V_list.stream().sorted(Comparator.comparing(Facility::getValueDensity)).collect(Collectors.toList());
//        MainAlgorithm.insertSort(V_list);
//        System.out.println("恢复状态并重新按照序号排序完毕后V_list的状态：");
//        V_list.forEach(System.out::println);

        //Step4：情况(2)：假设备选点vi不开设，VV0=VV0∪{vi}，VV5=VV5\{vi}；
        System.out.println("============================================================================");
        System.out.println("现在进入右子树假设！");
        VV0.add(curFacility);
        V.remove(curFacility);
        V_list.remove(curFacility);

        //(1)判断此时是否满足性质1的条件，若满足则该情况下无可行解，右子树剪枝；
        //若返回true，则无可行解，右子树剪枝，若返回false则继续
        boolean property02Flag = Properties.property02(V, V1, VV1);
        if (property02Flag) {
            System.out.println("满足性质2的条件，达到降阶效果");
            countOfProperty02++;
            System.out.println("countOfProperty02 = " + countOfProperty02);
        } else {
            //(2)再判断此时是否满足性质4的条件，若满足且符合成本约束，则得到可行解Sk=(V1∪VV1∪VV5(V))
            if (Properties.property04(V, V1, VV1)) {
                System.out.println("满足性质4的条件，达到降阶效果");
                countOfProperty04++;
//                countOfLeaf++;
                System.out.println("countOfProperty04 = " + countOfProperty04);
                //定义可行解Sk
                LinkedHashSet<Facility> Sk = new LinkedHashSet<>(V);
                Sk.addAll(V1);
                Sk.addAll(VV1);
                //判断Sk是否满足成本约束
                double wholeCi = 0.0;
                for (Facility facility : Sk) {
                    wholeCi += facility.getCi();
                }
                if (wholeCi <= C) {
                    //(3)若满足成本约束并且Sk的目标函数值z>b，则更新下界及最优解b=best_q=z，S_best=Sk；
                    double z = LowerBoundAlgorithm.calculateFitness(Sk);
                    System.out.println("由性质4直接得到了一个可行解Sk，其目标值为：" + z);
                    if (z > MainAlgorithm.b) {
                        MainAlgorithm.b = z;
                        S_best.clear();
                        S_best.addAll(Sk);
                        System.out.println("得到的可行解Sk的目标值优于当前下界，进行更新！b = " + b);
                        Properties.property05(V, V1, VV1, VV0);  //根据新下界更新ConflictList
                    }
                }
            } else {
                //若不满足性质4的条件调用上界子算法计算上界u，若u≠NSFlag且b≤u，调用Backtrack(cur_i+1)继续搜索
                double u = UpperBoundAlgorithm.upperBoundAlgorithm(V, V1, V0, VV1, VV0);
                System.out.println("x(" + curFacility.getId() + ") = 0时上界为u = " + u);
                System.out.println("此时下界b = " + b);
                if (u != NSFlag && MainAlgorithm.b <= u) {
                    backTracking();
                } else {
                    //否则通过上下界进行剪枝，此处统计次数
                    countOfUpLowBound++;
                }
            }
        }

        //Step5：返回上一层前执行VV0=VV0\{vi}，VV5=VV5∪{vi}；
        VV0.remove(curFacility);
        V.add(curFacility);
        V_list.add(curFacility);
        //需要将V_list重新按照编号从小到大的顺序排列
//        newV_list = new ArrayList<>();
//        newV_list.add(curFacility);  //先加curFacility
//        newV_list.addAll(V_list);  //再加之前的V_list
//        V_list.clear();  //将F清掉
//        V_list.addAll(newV_list);  //实质上是将原V_list换为了将curFacility加到了第一位的新F
//        V_list = V_list.stream().sorted(Comparator.comparing(Facility::getValueDensity)).collect(Collectors.toList());
//        MainAlgorithm.insertSort(V_list);
//        System.out.println("恢复状态并重新按照序号排序完毕后V_list的状态：");
//        V_list.forEach(System.out::println);
    }
}

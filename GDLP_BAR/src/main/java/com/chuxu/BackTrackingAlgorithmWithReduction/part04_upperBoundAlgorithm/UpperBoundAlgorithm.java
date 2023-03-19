package com.chuxu.BackTrackingAlgorithmWithReduction.part04_upperBoundAlgorithm;

import com.chuxu.BackTrackingAlgorithmWithReduction.part00_entity.CloneObject;
import com.chuxu.bak.LowerBoundAlgorithm;
import com.chuxu.entity.Facility;
import com.chuxu.BackTrackingAlgorithmWithReduction.part00_entity.TabuCouple;
import com.chuxu.BackTrackingAlgorithmWithReduction.part02_properties.Properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import static com.chuxu.BackTrackingAlgorithmWithReduction.MainAlgorithm.*;

//恢复状态也很重要啊，还好已经深拷贝了一份自己操作
public class UpperBoundAlgorithm {


    public static double upperBoundAlgorithm(LinkedHashSet<Facility> V, LinkedHashSet<Facility> V1,
                                             LinkedHashSet<Facility> V0, LinkedHashSet<Facility> VV1,
                                             LinkedHashSet<Facility> VV0) {
        System.out.println("============================================================================");
        System.out.println("============================================================================");
        System.out.println("现在进入上界子算法");

        //上界子算法是在自己的一个小空间内自己玩，所以上界子算法中一开始就要克隆所有的形参，
        //不对主函数中传入的全局变量造成影响。V其实是VV5=V\V0\V1\VV0\VV1
        CloneObject cloneObject = cloneObject(V, V0, V1, VV0, VV1);
        V = cloneObject.getV();
        V0 = cloneObject.getV0();
        V1 = cloneObject.getV1();
        VV0 = cloneObject.getVV0();
        VV1 = cloneObject.getVV1();

        //Step1：由性质1和性质2判断该情况下是否无解，若无解则返回u=NSFlag，上界子算法结束；
        if (Properties.property02(V, V1, VV1)) {
            return NSFlag;
        }
        if (Properties.property01(V1, VV1)) {
            return NSFlag;
        }

        //Step2：由性质3找出VV5中一定不开设的备选点
        //然后由性质1判断该情况下是否无解，若无解则返回u=NSFlag，上界子算法结束；
        LinkedHashSet<Facility> closeFacilitiesOfProperty03 = Properties.property03(V);
        //若closeFacilitiesOfProperty03不为空，则更新对应变量
        if (!closeFacilitiesOfProperty03.isEmpty()) {
            //更新集合
            V.removeAll(closeFacilitiesOfProperty03);
            VV0.addAll(closeFacilitiesOfProperty03);
            //由性质1判断该情况下是否无解，若无解则返回u=NSFlag
            if (Properties.property02(V, V1, VV1)) {
                return NSFlag;
            }
        }
        //由ConflictList表找出VV5中一定不开设的备选点
        LinkedHashSet<Facility> closeFacilitiesOfConflictList = new LinkedHashSet<>();
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
        //若closeFacilitiesOfConflictList不为空，则更新对应变量
        if (!closeFacilitiesOfConflictList.isEmpty()) {
            //更新集合
            V.removeAll(closeFacilitiesOfConflictList);
            VV0.addAll(closeFacilitiesOfConflictList);
            //由性质1判断该情况下是否无解，若无解则返回u=NSFlag
            if (Properties.property02(V, V1, VV1)) {
                return NSFlag;
            }
        }

        //Step3：由性质4找出VV5中一定开设的备选点加入集合VV1，然后由性质2判断该情况下是否无解，若无解则返回u=NSFlag，上界子算法结束；
        //否则得到一可行解Sk，计算Sk的目标函数值z，令u=z，上界子算法结束；
        if (Properties.property04(V, V1, VV1)) {
            //更新集合
            VV1.addAll(V);
            V.clear();

            //通过性质2判断其是否真的为一个可行解
            if (!Properties.property01(V1, VV1)) {
                //用局部变量Sk来存储这个可行解
                LinkedHashSet<Facility> Sk = new LinkedHashSet<>();
                //此时V1∪VV1∪VV5(V)可能是一个可行解
                Sk.addAll(V1);
                Sk.addAll(VV1);
                //计算Sk的目标值
                return LowerBoundAlgorithm.calculateFitness(Sk);
            }
        }

        //Step4：取V\V0\VV0中各备选点之间第a(a+1)/2大的距离作为上界；
        LinkedHashSet<Facility> V_Add_V1_Add_VV1 = new LinkedHashSet<>(V);
        V_Add_V1_Add_VV1.addAll(V1);
        V_Add_V1_Add_VV1.addAll(VV1);
        //计算V\V0\VV0的子矩阵中第a(a+1)/2大的距离作为上界；
//        System.out.println("a * (a + 1) / 2 = " + a * (a + 1) / 2);
        return calculateUpperBound(V_Add_V1_Add_VV1);
    }

    private static double calculateUpperBound(LinkedHashSet<Facility> V_Add_V1_Add_VV1) {
        //存储所有V_Add_V1_Add_VV1涉及到的距离
        List<Double> allValue = new ArrayList<>();
        //存储相关索引，用于遍历列(j)时进行判断
        List<Integer> relatedIndex = new ArrayList<>();
        for (Facility facility : V_Add_V1_Add_VV1) {
            relatedIndex.add(facility.getId() - 1);
        }
        //遍历V1_Add_VV1里面的每个节点
        for (Facility facility : V_Add_V1_Add_VV1) {
            //遍历距离矩阵中代表当前结点的那一行
            for (int i = 0; i < disMatrix.length; i++) {
                if (i == facility.getId() - 1) {
                    //遍历距离矩阵中非对角线元素，寻找满足距离小于当前已知的最小距离的元素，应该只遍历上三角就行了
                    for (int j = i + 1; j < disMatrix[0].length; j++) {
                        //如果当前列是本次实参相关的结点，才进行判断
                        if (relatedIndex.contains(j)) {
                            allValue.add(disMatrix[i][j]);
                        }
                    }
                }
            }
        }
        //将allValue降序排序，并返回第a(a+1)/2大的距离作为上界
        //这个排序增加的时间开销还真是不少，超过一倍
        Collections.sort(allValue);
        Collections.reverse(allValue);
//        System.out.println("allValue = " + allValue);
        return allValue.get(a * (a + 1) / 2 - 1);
    }

    private static double calculateMaxDistance(LinkedHashSet<Facility> V_Add_V1_Add_VV1) {
        //现在的问题是如何从原矩阵中获取V1_Add_VV1相关的一个子矩阵==>用判断
        double maxDistance = Double.MIN_VALUE;
        //存储相关索引，用于遍历列(j)时进行判断
        List<Integer> relatedIndex = new ArrayList<>();
        for (Facility facility : V_Add_V1_Add_VV1) {
            relatedIndex.add(facility.getId() - 1);
        }
        //遍历V1_Add_VV1里面的每个节点
        for (Facility facility : V_Add_V1_Add_VV1) {
            //遍历距离矩阵中代表当前结点的那一行
            for (int i = 0; i < disMatrix.length; i++) {
                if (i == facility.getId() - 1) {
                    //遍历距离矩阵中非对角线元素，寻找满足距离小于当前已知的最小距离的元素
                    for (int j = i + 1; j < disMatrix[0].length; j++) {
                        if (relatedIndex.contains(j) && disMatrix[i][j] > maxDistance) {
                            maxDistance = disMatrix[i][j];
                        }
                    }
                }
            }
        }
        return maxDistance;
    }

    private static CloneObject cloneObject(LinkedHashSet<Facility> V, LinkedHashSet<Facility> V0, LinkedHashSet<Facility> V1, LinkedHashSet<Facility> VV0, LinkedHashSet<Facility> VV1) {
        LinkedHashSet<Facility> V_u = new LinkedHashSet<>();
        for (Facility facility : V) {
            V_u.add(facility.clone());
        }
        LinkedHashSet<Facility> V0_u = new LinkedHashSet<>();
        for (Facility facility : V0) {
            V0_u.add(facility.clone());
        }
        LinkedHashSet<Facility> V1_u = new LinkedHashSet<>();
        for (Facility facility : V1) {
            V1_u.add(facility.clone());
        }
        LinkedHashSet<Facility> VV0_u = new LinkedHashSet<>();
        for (Facility facility : VV0) {
            VV0_u.add(facility.clone());
        }
        LinkedHashSet<Facility> VV1_u = new LinkedHashSet<>();
        for (Facility facility : VV1) {
            VV1_u.add(facility.clone());
        }
        return new CloneObject(V_u, V0_u, V1_u, VV0_u, VV1_u);
    }
}
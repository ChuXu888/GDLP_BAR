package com.chuxu.BackTrackingAlgorithmWithReduction.part05_orderReductionAlgorithm;

import com.chuxu.entity.Facility;
import com.chuxu.BackTrackingAlgorithmWithReduction.part00_entity.TabuCouple;
import com.chuxu.BackTrackingAlgorithmWithReduction.part02_properties.Properties;

import java.util.LinkedHashSet;

import static com.chuxu.BackTrackingAlgorithmWithReduction.MainAlgorithm.*;

public class OrderReductionAlgorithm {

    public static void orderReductionAlgorithm() {

        //Step1：利用性质5得出不能同时开设的备选点的对应关系表ConflictList。
        Properties.property05(V, V1, VV1);
        System.out.println("ConflictList = ");
        ConflictList.forEach(System.out::println);

        //Step2：利用性质7和性质9对问题进行降阶；
        LinkedHashSet<Facility> closeFacilitiesOfProperty07 = Properties.property07(V, V0, V1);
        LinkedHashSet<Facility> closeFacilitiesOfProperty09 = Properties.property09(V, V1);
        if (!closeFacilitiesOfProperty07.isEmpty() || !closeFacilitiesOfProperty09.isEmpty()) {
            System.out.println("有结点必须关闭！");
            System.out.println("closeFacilitiesOfProperty07：");
            closeFacilitiesOfProperty07.forEach(System.out::println);
            System.out.println("closeFacilitiesOfProperty09：");
            closeFacilitiesOfProperty09.forEach(System.out::println);
            updateAndJudgeOfV0(closeFacilitiesOfProperty07, closeFacilitiesOfProperty09);
        }

        //Step4：利用性质6和性质8对问题进行降阶；
        LinkedHashSet<Facility> openFacilitiesOfProperty06 = Properties.property06(V, V0, V1);
        LinkedHashSet<Facility> openFacilitiesOfProperty08 = Properties.property08(V);
        if (!openFacilitiesOfProperty06.isEmpty() || !openFacilitiesOfProperty08.isEmpty()) {
            System.out.println("有结点必须开设！");
            System.out.println("closeFacilitiesOfProperty06：");
            openFacilitiesOfProperty06.forEach(System.out::println);
            System.out.println("closeFacilitiesOfProperty08：");
            openFacilitiesOfProperty08.forEach(System.out::println);
            //更新相应的集合和变量
            V1.addAll(openFacilitiesOfProperty06);
            V1.addAll(openFacilitiesOfProperty08);
            V.removeAll(openFacilitiesOfProperty06);
            V.removeAll(openFacilitiesOfProperty08);
            //将这两个集合求并集，顺便去重
            openFacilitiesOfProperty06.addAll(openFacilitiesOfProperty08);
            //更新Cr
            for (Facility facility : openFacilitiesOfProperty06) {
                Cr -= facility.getCi();
                Pr -= facility.getPi();
            }
            //Step5：若Step3处理完毕后V1发生变化，则由性质2判断该问题是否无解；
            //然后由性质3结合ConflictList找出V5中一定不开设的备选点加入集合V0；
            if (Properties.property01(V1, VV1)) {
                System.out.println("满足性质2的条件，该问题无解！");
                System.exit(0);
            }
            //由性质3找出V5中一定不开设的备选点加入集合V0；
            LinkedHashSet<Facility> closeFacilitiesOfProperty03 = Properties.property03(V);
            //由ConflictList表找出VV5中一定不开设的备选点
            LinkedHashSet<Facility> closeFacilitiesOfConflictList = new LinkedHashSet<>();
            for (TabuCouple tabuCouple : ConflictList) {
                Facility vi = tabuCouple.getVi();
                Facility vj = tabuCouple.getVj();
                for (Facility facility : V1) {
                    if (facility.getId() == vi.getId()) {
                        closeFacilitiesOfConflictList.add(vj);
                    }
                    if (facility.getId() == vj.getId()) {
                        closeFacilitiesOfConflictList.add(vi);
                    }
                }
            }
            if (!closeFacilitiesOfProperty03.isEmpty() || !closeFacilitiesOfConflictList.isEmpty()) {
                System.out.println("有结点必须关闭！");
                System.out.println("closeFacilitiesOfProperty03：");
                closeFacilitiesOfProperty03.forEach(System.out::println);
                System.out.println("closeFacilitiesOfConflictList：");
                closeFacilitiesOfConflictList.forEach(System.out::println);
                updateAndJudgeOfV0(closeFacilitiesOfProperty03, closeFacilitiesOfConflictList);
            }
        }
    }

    private static void updateAndJudgeOfV0(LinkedHashSet<Facility> closeFacilitiesOfProperty03, LinkedHashSet<Facility> closeFacilitiesOfConflictList) {
        //更新相应的集合和变量
        V0.addAll(closeFacilitiesOfProperty03);
        V0.addAll(closeFacilitiesOfConflictList);
        V.removeAll(closeFacilitiesOfProperty03);
        V.removeAll(closeFacilitiesOfConflictList);
        //嵌套：若V0发生变化，则由性质1判断该问题是否无解；然后由性质4结合成本约束判断能否直接得到该问题的最优解；
        if (Properties.property02(V, V1, VV1)) {
            System.out.println("满足性质1的条件，该问题无解！");
            System.exit(0);
        }
        if (Properties.property04(V, V1, VV1)) {
            double wholeCi = 0.0;
            for (Facility facility : V) {
                wholeCi += facility.getCi();
            }
            //由性质4结合成本约束判断能否直接得到该问题的最优解
            if (wholeCi <= C) {
                System.out.println("满足性质4的条件，并且满足成本约束，将VV5=V5(V)中的所有节点均开设，得到唯一解也即最优解");
                S_best = V;
                S_best.forEach(System.out::println);
                System.exit(0);
            }
        }
    }
}

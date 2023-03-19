package com.chuxu.BackTrackingAlgorithmWithReduction.part02_properties;

import com.chuxu.entity.Facility;
import com.chuxu.BackTrackingAlgorithmWithReduction.part00_entity.TabuCouple;
import com.chuxu.BackTrackingAlgorithmWithReduction.MainAlgorithm;
import com.chuxu.BackTrackingAlgorithmWithReduction.part04_upperBoundAlgorithm.UpperBoundAlgorithm;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.chuxu.BackTrackingAlgorithmWithReduction.MainAlgorithm.*;

public class Properties {

    //性质1：问题求解过程中，若所有开设设施V1∪VV1的总成本已经超过了总成本上限C，
    //则当|VV0∪VV1|=0时，该问题无解；当|VV0∪VV1|≠0时，该情况下无解。
    public static boolean property01(LinkedHashSet<Facility> V1, LinkedHashSet<Facility> VV1) {
        //计算V1∪VV1
        LinkedHashSet<Facility> V1_Add_VV1 = new LinkedHashSet<>(V1);
        V1_Add_VV1.addAll(VV1);
        //遍历V1_Add_VV1，将所有ci相加，与C做对比
        double wholeCi = 0.0;
        for (Facility facility : V1_Add_VV1) {
            wholeCi += facility.getCi();
        }
        //返回布尔值
        return wholeCi > C;
    }

    //性质2：问题求解过程中，若剩余所有可能开设设施V\V0\VV0=V1∪VV1∪VV5(V)的总容量小于P，
    //则当|VV0∪VV1|=0时，该问题无解；当|VV0∪VV1|≠0时，该情况下无解。
    public static boolean property02(LinkedHashSet<Facility> V, LinkedHashSet<Facility> V1, LinkedHashSet<Facility> VV1) {
        //计算V1∪VV1∪VV5(V)
        LinkedHashSet<Facility> V_Add_V1_Add_VV1 = new LinkedHashSet<>(V);
        V_Add_V1_Add_VV1.addAll(V1);
        V_Add_V1_Add_VV1.addAll(VV1);
        //遍历V_Add_V1_Add_VV1，将所有pi相加，与P做对比
        double wholePi = 0.0;
        for (Facility facility : V_Add_V1_Add_VV1) {
            wholePi += facility.getPi();
        }
        //返回布尔值
        return wholePi < P;
    }

    //性质3：在导出子图G[VV5]中，若存在满足ci >Cr的备选点vi，
    //则当|VV0∪VV1|=0时，将vi加入V0；当|VV0∪VV1|≠0时，将vi加入VV0。
    public static LinkedHashSet<Facility> property03(LinkedHashSet<Facility> V) {
        //存储一定不开设的备选点集合
        LinkedHashSet<Facility> closeFacilities = new LinkedHashSet<>();
        //遍历V，寻找符合性质3的备选点
        for (Facility facility : V) {
            if (facility.getCi() > Cr) {
                closeFacilities.add(facility);
            }
        }
        //返回一定不开设的备选点集合
        return closeFacilities;
    }

    //性质4：令P_real=Σvi∈V\V0\VV0 pi，若Preal≥P，且∀vi∊VV5，有pi>Preal-P，则VV5中的设施必须全部开设【无论删哪一个点，都将无法满足P的要求】。
    //当|VV0∪VV1|=0时，将VV5所有节点全部加入V1；当|VV0∪VV1|≠0时，将VV5中所有节点全部加入VV1。
    public static boolean property04(LinkedHashSet<Facility> V, LinkedHashSet<Facility> V1, LinkedHashSet<Facility> VV1) {
        //计算V\V0\VV0=V1∪VV1∪VV5(V)
        LinkedHashSet<Facility> V_Add_V1_Add_VV1 = new LinkedHashSet<>(V);
        V_Add_V1_Add_VV1.addAll(V1);
        V_Add_V1_Add_VV1.addAll(VV1);
        //存储一定开设的备选点集合
        LinkedHashSet<Facility> openFacilities = new LinkedHashSet<>();
        //定义P_real变量
        double P_real = 0.0;
        //计算P_real
        for (Facility facility : V_Add_V1_Add_VV1) {
            P_real += facility.getPi();
        }
        if (P_real >= P) {
            //遍历V_Add_V1_Add_VV1，判断是否符合性质4
            for (Facility facility : V_Add_V1_Add_VV1) {
                if (facility.getPi() <= P_real - P) {
                    //只要有一个备选点满足pi<=P_real-P，则性质4就不可能成立了，就返回false
                    return false;
                }
            }
        }
        //如果程序还能正常运行到这里而没有return，那么就已经是符合了性质4的条件，就返回true
        //调用方收到true之后，就直接开设VV5(V)中的所有设施即可
        return true;
    }

    //性质5：算法求解过程中，对于距离矩阵中V\V0=VV5(V)∪V1∪VV1∪VV0涉及到的【子矩阵】中，如果有任何一个数值dij<b
    //则该数值涉及到的一对点vi和vj不能同时开设。注意：此处不应涉及VV0，仅需将V0去掉就行了，VV0仍处在二叉树的假设阶段
    public static void property05(LinkedHashSet<Facility> V, LinkedHashSet<Facility> V1, LinkedHashSet<Facility> VV1, LinkedHashSet<Facility> VV0) {
        ConflictList.clear();  //由于性质5可能会多次调用【更新】，所以每次调用时都先清掉
        //计算V1∪VV1
        LinkedHashSet<Facility> V_Add_V1_Add_VV1_Add_VV0 = new LinkedHashSet<>(V);
        V_Add_V1_Add_VV1_Add_VV0.addAll(V1);
        V_Add_V1_Add_VV1_Add_VV0.addAll(VV1);
        V_Add_V1_Add_VV1_Add_VV0.addAll(VV0);
        //同时遍历VV5(V)和V1_Add_VV1，寻找dij<b的couple
        for (Facility vi : V_Add_V1_Add_VV1_Add_VV0) {
            for (Facility vj : V_Add_V1_Add_VV1_Add_VV0) {
                if (vi.getId() != vj.getId()) {
                    int rowIndex = vi.getId() - 1;
                    int columnIndex = vj.getId() - 1;
                    //保证只在上三角遍历，这样就去重了
                    if (columnIndex > rowIndex && disMatrix[rowIndex][columnIndex] < b) {
                        ConflictList.add(new TabuCouple(vi,vj));
                    }
                }
            }
        }
        System.out.println("============================================================================");
        System.out.println("由于下界b的更新，重新调用性质5，计算ConflictList如下：");
        ConflictList.forEach(System.out::println);
        //去重
//        LinkedHashSet<TabuCouple> removeDuplicates = new LinkedHashSet<>();
//        for (TabuCouple tabuCouple1 : ConflictList) {
//            for (TabuCouple tabuCouple2 : ConflictList) {
//                //如果碰到只是顺序不同的tabuCouple，就删掉一个
//                if (tabuCouple1.getVi().equals(tabuCouple2.getVj()) && tabuCouple1.getVj().equals(tabuCouple2.getVi())) {
//                    removeDuplicates.add(tabuCouple1);
//                }
//            }
//        }
//        ConflictList.removeAll(removeDuplicates);
    }

    //性质6：当|VV0∪VV1|=0时，若假设某个备选点vi∊V5不开设， VV0={vi}，VV1={}，
    //如果此时的上界u=NSFlag或下界b大于上界u，则vi一定开设，应将vi加入V1；判断结束后恢复VV0={}。
    public static LinkedHashSet<Facility> property06(LinkedHashSet<Facility> V, LinkedHashSet<Facility> V0, LinkedHashSet<Facility> V1) {
        //定义开设【加入V1】的结点集合，返回到主函数中再进行处理
        LinkedHashSet<Facility> openFacilities = new LinkedHashSet<>();
        //从主算法中获取下界
        double b = MainAlgorithm.b;
        //V\V0\V1=V5(V)
        LinkedHashSet<Facility> newV = new LinkedHashSet<>(V);
        LinkedHashSet<Facility> newV0 = new LinkedHashSet<>(V0);
        LinkedHashSet<Facility> newV1 = new LinkedHashSet<>(V1);

        for (Facility curFacility : newV) {
            //定义VV0
            LinkedHashSet<Facility> VV0 = new LinkedHashSet<>();
            VV0.add(curFacility);
            //新建一个列表用来存储VV5\{curFacility}的情况，以免形成一边遍历一边修改的情况，并且也保持了newV始终如一
            LinkedHashSet<Facility> newV_Minus_curFacility = new LinkedHashSet<>(newV);
            newV_Minus_curFacility.remove(curFacility);
            //调用上界子算法计算上界
            double u = UpperBoundAlgorithm.upperBoundAlgorithm(newV_Minus_curFacility, newV1, newV0, new LinkedHashSet<>(), VV0);
            System.out.println("当前假设Facility：" + curFacility.getId() + "不开设时，上界为：" + u);
            //如果得到的下界大于上界，当前设施一定开设，将其加入V1
            if (b > u) {
                openFacilities.add(curFacility);
            }
        }
        //在调用方进行非空判断
        return openFacilities;
    }

    //性质7：当|VV0∪VV1|=0时，若假设某个备选点vi∊V5开设，VV0={}，VV1={vi}，
    //如果此时的上界u=NSFlag或b大于上界u，则vi一定不开设，应将vi加入V0；判断结束后恢复VV1={}。
    public static LinkedHashSet<Facility> property07(LinkedHashSet<Facility> V, LinkedHashSet<Facility> V0, LinkedHashSet<Facility> V1) {
        //定义开设【加入V1】的结点集合，返回到主函数中再进行处理
        LinkedHashSet<Facility> closeFacilities = new LinkedHashSet<>();
        //从主算法中获取下界
        double b = MainAlgorithm.b;
        //V\V0\V1=V5(V)
        LinkedHashSet<Facility> newV = new LinkedHashSet<>(V);
        LinkedHashSet<Facility> newV0 = new LinkedHashSet<>(V0);
        LinkedHashSet<Facility> newV1 = new LinkedHashSet<>(V1);

        for (Facility curFacility : newV) {
            //定义VV1
            LinkedHashSet<Facility> VV1 = new LinkedHashSet<>();
            VV1.add(curFacility);
            //新建一个列表用来存储VV5\{curFacility}的情况，以免形成一边遍历一边修改的情况，并且也保持了newV始终如一
            LinkedHashSet<Facility> newV_Minus_curFacility = new LinkedHashSet<>(newV);
            newV_Minus_curFacility.remove(curFacility);
            //调用上界子算法计算下界
            double u = UpperBoundAlgorithm.upperBoundAlgorithm(newV_Minus_curFacility, newV1, newV0, VV1, new LinkedHashSet<>());
            System.out.println("当前假设Facility：" + curFacility.getId() + "开设时，上界为：" + u);
            //如果得到的下界大于上界，当前设施一定开设，将其加入V0
            if (b > u) {
                closeFacilities.add(curFacility);
            }
        }
        //在调用方进行非空判断
        return closeFacilities;
    }

    //性质8：当|VV0∪VV1|=0时，若假设某个备选点vi∊V5不开设，VV0={vi}，VV1={}，
    //此时若容量不可能满足，则vi必须开设，应将vi加入V1；判断结束后恢复VV0={}。
    public static LinkedHashSet<Facility> property08(LinkedHashSet<Facility> V) {
        //定义开设设施集合
        LinkedHashSet<Facility> openFacilities = new LinkedHashSet<>();
        //计算当前VV5(V)的总容量
        double wholePi = 0.0;
        for (Facility facility : V) {
            wholePi += facility.getPi();
        }
        //遍历VV5(V)，依次判断去掉某个结点行不行，不能去掉就必须开设
        for (Facility facility : V) {
            if (wholePi - facility.getPi() < Pr) {
                openFacilities.add(facility);
            }
        }
        //在调用方进行非空判断
        return openFacilities;
    }

    //性质9：当|VV0∪VV1|=0时，若假设某个备选点vi ∊V5开设，VV0={}，VV1={vi}，
    //此时若成本会超，则vi必须不开设，应将vi加入V0；判断结束后恢复VV0={}
    public static LinkedHashSet<Facility> property09(LinkedHashSet<Facility> V, LinkedHashSet<Facility> V1) {
        //定义关闭设施集合
        LinkedHashSet<Facility> closeFacilities = new LinkedHashSet<>();
        //计算当前V1已经占据的总成本
        double wholeCi = 0.0;
        for (Facility facility : V1) {
            wholeCi += facility.getCi();
        }
        //遍历VV5(V)，依次判断开设某个结点行不行，不能开设就关闭
        for (Facility facility : V) {
            if (wholeCi + facility.getCi() > C) {
                closeFacilities.add(facility);
            }
        }
        //在调用方进行非空判断
        return closeFacilities;
    }

    //性质10：将V中所有设施备选点按照容量降序排序，若排序后V中前a个备选点的容量之和 ，而前(a+1)个备选点的容量之和 ，
    //则MMDP的一个可行解中至少包含(a+1)个备选点，该(a+1)个备选点的点导出子图中至少含有a(a+1)/2条边。
    public static void property10() {
        //降序排序
        List<Facility> tempList = V.stream().sorted(Comparator.comparing(Facility::getPi).reversed()).collect(Collectors.toList());
        //计算a
        double wholePi = 0.0;
        while (true) {
            Facility curBestFacility = tempList.get(0);
            if (wholePi + curBestFacility.getPi() >= P) {
                break;
            }
            a++;
            wholePi += curBestFacility.getPi();
            tempList.remove(curBestFacility);
        }
    }
}

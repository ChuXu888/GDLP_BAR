package com.chuxu.BackTrackingAlgorithmWithReduction.part03_lowerBoundAlgorithm;

import com.chuxu.BackTrackingAlgorithmWithReduction.part00_entity.CloneObject;
import com.chuxu.entity.Facility;
import com.chuxu.BackTrackingAlgorithmWithReduction.MainAlgorithm;
import com.chuxu.BackTrackingAlgorithmWithReduction.part02_properties.Properties;

import java.util.*;

import static com.chuxu.BackTrackingAlgorithmWithReduction.MainAlgorithm.*;

//下界算法中没有全局降阶部分，是不是也应该克隆一份再操作呢？
public class LowerBoundAlgorithm {
    public static double lowerBoundAlgorithm() {
        System.out.println("============================================================================");
        System.out.println("============================================================================");
        System.out.println("现在进入下界子算法");

        //由于可能会重复调用上界子算法，所以上界子算法一开始就要将u置为0然后重新开始
        double cur_u = 0;

        //注意：无论是全局降阶还是局部降阶，都最好克隆之后再进行操作，以免造成一些不必要的麻烦
        CloneObject cloneObject = cloneObject(V, V0, V1);
        LinkedHashSet<Facility> V = cloneObject.getV();
        LinkedHashSet<Facility> V0 = cloneObject.getV0();
        LinkedHashSet<Facility> V1 = cloneObject.getV1();

        //Step0：利用性质3对问题进行降阶；
        LinkedHashSet<Facility> closeFacilitiesOfProperty03 = Properties.property03(V);
        if (!closeFacilitiesOfProperty03.isEmpty()) {
            System.out.println("==========================================================");
            System.out.println("通过性质3实现了降阶,降阶点为:");
            closeFacilitiesOfProperty03.forEach(System.out::println);
            //集合更新
            V0.addAll(closeFacilitiesOfProperty03);
            V.removeAll(closeFacilitiesOfProperty03);
            //反拷贝全局变量V、V0、V1
            reverseCopyToGlobalSet(V, V0, V1);
        }

        //Step1：令集合V_temp={}；
        LinkedHashSet<Facility> V_temp = new LinkedHashSet<>();

        //Step2：将V5中距离最远的两个备选点加入集合Vtemp，并从V5中移除这两个备选点
        //其实就是带着V5=V\V0\V1【此时上界子算法刚开始，就等于V】在距离矩阵中寻找最大值
        Facility vi = null;
        Facility vj = null;
        double maxDistance = Double.MIN_VALUE;
        for (int i = 0; i < disMatrix.length; i++) {
            for (int j = 0; j < disMatrix[0].length; j++) {
                if (disMatrix[i][j] > maxDistance) {
                    maxDistance = disMatrix[i][j];
                    for (Facility facility : VV5) {
                        if (facility.getId() == i + 1) {
                            vi = facility;
                        }
                        if (facility.getId() == j + 1) {
                            vj = facility;
                        }
                    }
                }
            }
        }
        V.remove(vi);
        V.remove(vj);
        V_temp.add(vi);
        V_temp.add(vj);

        //Step3：若容量已足够，转至Step5；
        //否则转至Step4:假设V5中距Vtemp中所有备选点距离最大的备选点为vk，执行Vtemp=Vtemp∪{vk}，V5=V5\{vk}，转回Step3；
        double wholePi = V_temp.stream().map(Facility::getPi).reduce(0.0, Double::sum);
        while (wholePi < P) {
            //若容量已足够，转至Step5；
            //Step4:假设V5中距Vtemp中所有备选点距离最大的备选点为vk，执行Vtemp=Vtemp∪{vk}，V5=V5\{vk}，转回Step3；
            Facility vk = null;
            double localMaxDistance = Double.MIN_VALUE;
            //对于每个V5中的每个结点，都要算一个它到V_temp中结点的最大距离，而最终又要在这些最大距离中再选一个最大的
            for (Facility facility01 : V) {
                for (Facility facility02 : V_temp) {
                    if (disMatrix[facility01.getId() - 1][facility02.getId() - 1] > localMaxDistance) {
                        localMaxDistance = disMatrix[facility01.getId() - 1][facility02.getId() - 1];
                        vk = facility01;  //这种方式这里就会有比较多的多余的赋值
                    }
                }
            }
            V.remove(vk);
            V_temp.add(vk);
            wholePi += vk.getPi();
        }

        //使用贪心策略加点至容量足够之后，V_temp的状态
        System.out.println("使用贪心策略加点至容量足够之后，V_temp的状态：");
        V_temp.forEach(System.out::println);
        //Step5：若成本超了，转至Step6；否则令S_best=V_temp，∀vi,vj∊V_temp，b=best_q=min{dij}，下界子算法结束；
        double localWholeCi = 0.0;
        for (Facility facility : V_temp) {
            localWholeCi += facility.getCi();
        }
        //Step5-1：若成本超了，转至Step6；
        if (localWholeCi > C) {
            //Step6：采用贪心修复策略对V_temp中备选点进行调整：
            //不断从V_temp中去除性价比ri=pi/ci低的备选点，然后从V5中补充性价比高的备选点；
            System.out.println("采用贪心修复策略对V_temp中备选点进行调整！");

            //(1)将V_temp中所有备选点按照性价比从小到大排序，假设排序后V_temp中性价比最低的备选点为vh，
            //执行V_temp=V_temp\{vh}，V5=V5∪{vh}，重复此过程直至成本不超；
            while (localWholeCi > C) {
                //找到性价比最低的结点从V_temp中删除
                List<Facility> transitionOfVtemp = new ArrayList<>(V_temp);
                Collections.sort(transitionOfVtemp);
                Facility vh = transitionOfVtemp.get(0);
                V_temp.remove(vh);
                V.add(vh);
                localWholeCi -= vh.getCi();
            }

            //(2)若此时总成本刚好等于C，则转至Step7；若此时总成本小于C，则或许还能再加点，将V5中所有备选点按照性价比从大到小排序；
            if (localWholeCi < C) {
                //(3)假设V5中性价比最高的备选点为vl，若加它之后成本不超，转至(4)加上它，否则转至Step7；
                while (true) {
                    List<Facility> transitionOfVtemp = new ArrayList<>(V);
                    Collections.sort(transitionOfVtemp);
                    Facility vl = transitionOfVtemp.get(transitionOfVtemp.size() - 1);
                    if (localWholeCi + vl.getCi() > C) {
                        break;
                    }
                    //(4)执行V_temp=V_temp∪{vl}，V5=V5\{vl}，转回(3)；
                    V.remove(vl);
                    V_temp.add(vl);
                    localWholeCi += vl.getCi();
                }
            }

            //Step7：若修复完毕后满足容量约束，则得到一个可行解，令Sbest=Vtemp，∀vi,vj∊Vtemp，b=best_q =min{dij}；
            //否则∀vi,vj∊V\V0，令b=best_q=min{dij}；
            wholePi = 0.0;
            for (Facility facility : V_temp) {
                wholePi += facility.getPi();
            }
            //三元表达式
            if (wholePi >= P) {
                S_best.clear();
                S_best.addAll(V_temp);
                cur_u = calculateFitness(V_temp);
            } else {
                System.out.println("没有得到可行解，只能原图G中的最小距离作为下界！");
                cur_u = calculateFitness(VV5);
            }
            return cur_u;
        }

        //Step5-2：若成本未超，则令S_best=V_temp，∀vi,vj∊V_temp，b=best_q=min{dij}，下界子算法结束；
        S_best.clear();
        S_best.addAll(V_temp);
        cur_u = calculateFitness(V_temp);
        return cur_u;
    }

    private static void reverseCopyToGlobalSet(LinkedHashSet<Facility> V, LinkedHashSet<Facility> V0, LinkedHashSet<Facility> V1) {
        System.out.println("=================================================");
        MainAlgorithm.V.clear();
        for (Facility facility : V) {
            MainAlgorithm.V.add(facility.clone());
        }
        System.out.println("反拷贝结束后全局变量V(V5)：");
        MainAlgorithm.V.forEach(System.out::println);
        System.out.println("=================================================");
        //将上界子算法中的V1拷贝给主函数中的V1
        MainAlgorithm.V1.clear();
        for (Facility facility : V1) {
            MainAlgorithm.V1.add(facility.clone());
        }
        System.out.println("反拷贝结束后全局变量V1：");
        MainAlgorithm.V1.forEach(System.out::println);
        System.out.println("=================================================");
        //将上界子算法中的F拷贝给主函数中的V0
        MainAlgorithm.V0.clear();
        for (Facility facility : V0) {
            MainAlgorithm.V0.add(facility.clone());
        }
        System.out.println("反拷贝结束后全局变量V0：");
        MainAlgorithm.V0.forEach(System.out::println);
    }

    public static double calculateFitness(LinkedHashSet<Facility> V1_Add_VV1) {
        //现在的问题是如何从原矩阵中获取V1_Add_VV1相关的一个子矩阵==>用判断
        double minDistance = Double.MAX_VALUE;
        //存储相关索引，用于遍历列(j)时进行判断
        List<Integer> relatedIndex = new ArrayList<>();
        for (Facility facility : V1_Add_VV1) {
            relatedIndex.add(facility.getId() - 1);
        }
        //遍历V1_Add_VV1里面的每个节点
        for (Facility facility : V1_Add_VV1) {
            //遍历距离矩阵中代表当前结点的那一行
            for (int i = 0; i < disMatrix.length; i++) {
                if (i == facility.getId() - 1) {
                    //遍历距离矩阵中非对角线元素，寻找满足距离小于当前已知的最小距离的元素，应该只遍历上三角就行了
                    for (int j = i + 1; j < disMatrix[0].length; j++) {
                        //如果当前列是本次实参相关的结点，才进行判断
//                        if (relatedIndex.contains(j) && j != i && disMatrix[i][j] < minDistance) {
//                            minDistance = disMatrix[i][j];
//                        }
                        if (relatedIndex.contains(j) && disMatrix[i][j] < minDistance) {
                            minDistance = disMatrix[i][j];
                        }
                    }
                }
            }
        }
        return minDistance;
    }

    private static CloneObject cloneObject(LinkedHashSet<Facility> V, LinkedHashSet<Facility> V0, LinkedHashSet<Facility> V1) {
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
        return new CloneObject(V_u, V0_u, V1_u);
    }
}
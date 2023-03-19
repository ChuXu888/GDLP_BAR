package com.chuxu.BackTrackingAlgorithmWithReduction.part07_comparedAlgorithms.FA;

import com.chuxu.BackTrackingAlgorithmWithReduction.part01_preHandleData.RandomCase.N35_1;
import com.chuxu.BackTrackingAlgorithmWithReduction.part07_comparedAlgorithms.FA.entity.Firefly;
import com.chuxu.entity.Facility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FA {
    //初始条件相关变量
    private static final int dimension = N35_1.dimension;  //问题规模
    private static final double[][] disMatrix = N35_1.disMatrix;  //距离矩阵
    private static final List<Double> pi = new ArrayList<>();
    private static final List<Double> ci = new ArrayList<>();
    private static final List<Facility> allFacilities = new ArrayList<>();
    private static final double φc = N35_1.φc;  //这个数值越小，越难获得可行解
    private static final double φp = N35_1.φp;  //这个数值越大，越难获得可行解
    private static double P;
    private static double C;
    private static double globalMinDistance;  //距离矩阵中的最小值，用做惩罚量
    private static final Random random = new Random();
    //萤火虫算法相关常量
    private static final int populationNum = 50;  //萤火虫数量
    private static final int maxIteration = 1000;  //最大迭代轮数
    private static final double γ = 1.0;  //光强吸收系数
    private static final double β0 = 1.0;  //最大吸引度
    private static final double α = 0.2;  //步长因子
    private static final double ωmax = 0.9;  //惯性权重
    private static final double ωmin = 0.5;  //惯性权重

    public static void main(String[] args) throws Exception {
        //0.封装数据，计算P和C
        N35_1.preHandle();
        capsulateData();

        //计算P和C，求最小距离作为惩罚值
        C = ci.stream().reduce(0.0, Double::sum) * φc;
        P = pi.stream().reduce(0.0, Double::sum) * φp;
        globalMinDistance = getGlobalMinDistance();
        System.out.println("C = " + C);
        System.out.println("P = " + P);
        //记录算法开始运行时的时间
        long start = System.currentTimeMillis();
        //1.初始化种群
        List<Firefly> fireflies = generateSolution();
        //进入循环迭代
        int iteration = 1;
        while (iteration < maxIteration) {
//            System.out.println("iteration = " + iteration);
            //2.修复种群
            fireflies.forEach(FA::check);
            //3.计算各萤火虫的适应值，如果不可行那个则设置惩罚值
            fireflies.forEach(FA::calculateFitness);
            //4.更新各萤火虫的位置
            updateLocation(fireflies, iteration);
            //迭代次数加1
            iteration++;
        }
        //记录算法结束运行时的时间
        long end = System.currentTimeMillis();
        Collections.sort(fireflies);
        System.out.println("最优解 = " + fireflies.get(fireflies.size() - 1));
        System.out.println("最优目标值 = " + fireflies.get(fireflies.size() - 1).getCurFitness());
        System.out.println("程序耗时：" + (end - start) / 1000.0);
    }

    //5.sigmoid映射函数
    public static double sigmoid(double x) {
        return 1 / (1 + Math.exp((-10) * (x - 0.5)));
    }

    //4.位置更新
    public static void updateLocation(List<Firefly> fireflies, int iteration) {
        //根据相对亮度计算公式可知，对于两个给定的萤火虫AB，而r_ab都是一样的，若目标值f(A)>f(B)，则I(A)>I(B)
        //所以最终其实还是归结为适应值低的向适应值高的移动即可
        for (int i = 0; i < fireflies.size(); i++) {
            //仅需对比上三角即可
            for (int j = i + 1; j < fireflies.size(); j++) {
                Firefly firefly1 = fireflies.get(i);
                Firefly firefly2 = fireflies.get(j);
                //如果亮度相对高才会进行距离的更新，注意次数劣势方firefly2是要更新位置的一方
                if (firefly1.getCurFitness() > firefly2.getCurFitness()) {
                    List<Integer> curX = firefly2.getX();
                    //类似于粒子群优化算法，对每一维的分量分开更新
                    for (int k = 0; k < dimension; k++) {
                        double r_ijk = Math.abs(firefly1.getX().get(k) - firefly2.getX().get(k));
//                        System.out.println("r_ijk = " + r_ijk);
                        //计算吸引度
                        double curβ = β0 * Math.exp(-γ * Math.pow(r_ijk, 2));
//                        System.out.println("curβ = " + curβ);
                        //更新位置
                        double dynamicWeight = ωmax - ((ωmax - ωmin) * iteration) / maxIteration;
                        double newXk = dynamicWeight * curX.get(k) + curβ * (firefly1.getX().get(k) - firefly2.getX().get(k)) + α * (random.nextGaussian() - 0.5);
                        double sigmoidValue = sigmoid(newXk);
                        if (random.nextDouble() < sigmoidValue) {
                            curX.set(k, 1);
                        } else {
                            curX.set(k, 0);
                        }
                    }
                }
            }
        }
    }

    //3.计算各萤火虫的适应值
    public static void calculateFitness(Firefly firefly) {
        //由于本修复方案无法保证修复之后为可行解，所以还要再进行一次判断。
        //如果是可行解，那就正常计算适应值；如果不是可行解，那就使用罚函数，例如直接返回距离矩阵中最小的值作为适应值
        List<Integer> curX = firefly.getX();
        double wholeCost = 0.0;
        double wholeCapacity = 0.0;
        for (int i = 0; i < curX.size(); i++) {
            if (curX.get(i) == 1) {
                wholeCost += ci.get(i);
                wholeCapacity += pi.get(i);
            }
        }

        if (wholeCost <= C && wholeCapacity >= P) {
            //现在的问题是如何根据position获取这样一个子矩阵，用判断
            double minDistance = Double.MAX_VALUE;
            for (int i = 0; i < disMatrix.length; i++) {
                //只有position[i] == 1.0的第i行才需要继续遍历
                if (curX.get(i) == 1) {
                    for (int j = i + 1; j < disMatrix[0].length; j++) {
                        //对于该行的每个数据，只有position[j] == 1.0的第j列才需要继续判断，同时j != i保证了不是对角线数字
                        if (curX.get(j) == 1 && disMatrix[i][j] < minDistance) {
                            minDistance = disMatrix[i][j];
                        }
                    }
                }
            }
            firefly.setCurFitness(minDistance);
        } else {
            firefly.setCurFitness(globalMinDistance);
        }
    }

    //2.3-约束处理函数，如果一个个体不满足约束条件，则对其进行相应的处理，使其满足约束条件
    public static void repairWolf(Firefly firefly) {
        List<Integer> curX = firefly.getX();
        double wholeCi = 0.0;
        double wholePi = 0.0;
        List<Facility> selectedFacilities = new ArrayList<>();  //存储当前被选中的所有个体
        for (int i = 0; i < curX.size(); i++) {
            if (curX.get(i) == 1) {
                selectedFacilities.add(allFacilities.get(i));
                wholeCi += ci.get(i);
                wholePi += pi.get(i);
            }
        }
        List<Facility> unSelectedFacilities = new ArrayList<>(allFacilities);
        unSelectedFacilities.removeAll(selectedFacilities);

        //观察未修复时position数组的状态
//        System.out.println("======================================================");
//        System.out.println("未修复时wholeCost = " + wholeCi);
//        System.out.println("未修复时wholeCapacity = " + wholePi);

        //下面分成两大类进行处理：
        //(1)如果P不够而C没超，那就先加性价比高的物品直到P够了，此时C如果超了，那就同下面(2)中的P够C超；如果C没超那就已经修复好了。
        if (wholePi < P && wholeCi < C) {
            //将未被选中的所有个体按照性价比升序排列
            Collections.sort(unSelectedFacilities);
            while (wholePi < P) {
                //获取unSelectedNodes中性价比最高的结点
                Facility bestFacility = unSelectedFacilities.get(unSelectedFacilities.size() - 1);
                //进行相关修改
                wholePi += bestFacility.getPi();
                wholeCi += bestFacility.getCi();
                curX.set(bestFacility.getId() - 1, 1);
                selectedFacilities.add(bestFacility);
                unSelectedFacilities.remove(unSelectedFacilities.size() - 1);
            }
//            System.out.println("P不够C没超，修复后wholeCost = " + wholeCi);
//            System.out.println("P不够C没超，修复后wholeCapacity = " + wholePi);
            //这个if如果不满足就变成了P够C超的局面，就转到(2)的处理流程了
            if (wholeCi <= C) {
                return;
            }
        }

        //(2)如果P不够C超或者P够C超，都先移除性价比低的物品直到C不超，然后增加性价比高的物品；如果还不是可行解，那就用罚函数了。
        //将被选中的所有个体按照性价比升序排列
        Collections.sort(selectedFacilities);

        //进行第一步修复
        while (wholeCi > C) {
            //获取性价比最低的结点
            Facility worstFacility = selectedFacilities.get(0);
            //一定要先把这个重量减掉，再把物品移除
            wholeCi -= worstFacility.getCi();
            wholePi -= worstFacility.getPi();
            //由于将性价比最低的结点移除了，那么需要获取这个被移除结点的id，
            curX.set(worstFacility.getId() - 1, 0);
            //最后移除该结点
            selectedFacilities.remove(0);
            //在unSelectedNodes中添加该结点
            unSelectedFacilities.add(worstFacility);
        }

        //观察第一步修复时后position数组的状态
//        System.out.println("第一步修复后wholeCost = " + wholeCi);
//        System.out.println("第一步修复后wholeCapacity = " + wholePi);

        //将未被选中的所有个体按照性价比升序排列
        Collections.sort(unSelectedFacilities);

        //进行第二步修复
        while (true) {
            //此时在列表末尾的是性价比最高的商品
            Facility bestFacility = unSelectedFacilities.get(unSelectedFacilities.size() - 1);
            //若将该商品加入后，重量抄了，就break
            if (bestFacility.getCi() + wholeCi > C) {
                break;
            } else {
                //获取这个被添加商品的id，在chars数组中同步修改其为1
                curX.set(bestFacility.getId() - 1, 1);
                //将当前结点的成本和容量加上去
                wholeCi += bestFacility.getCi();
                wholePi += bestFacility.getPi();
                //将当前物品加入到selectedGoods中，这一步可有可无，因为最终要的只是chars
                selectedFacilities.add(bestFacility);
                //从unSelectedGoods移除该物品，否则一直处理的都是那一个商品。所以之前的代码也写错了
                unSelectedFacilities.remove(unSelectedFacilities.size() - 1);
            }
        }

        //观察第二步修复后position数组的状态
//        System.out.println("第二步修复后wholeCost = " + wholeCi);
//        System.out.println("第二步修复后wholeCapacity = " + wholePi);

        //设置修复后的结果
        firefly.setX(curX);
    }

    //2.2-判断函数，判断某个染色体个体解码出来的装包方案是否能够满足背包的载重量约束，进而判断该个体是否合理
    public static boolean judgeWolf(Firefly firefly) {
        List<Integer> curX = firefly.getX();
        double wholeCi = 0.0;
        double wholePi = 0.0;
        for (int i = 0; i < curX.size(); i++) {
            if (curX.get(i) == 1) {
                //获取唯一标识id，通过id-1从dataSet_weight中获取该物品的重量
//                int curId = wolf.getId();
//                wholeWeight += dataSet_weight.get(curId - 1);
                wholeCi += ci.get(i);
                wholePi += pi.get(i);
            }
        }
//        System.out.println("wholeCi = " + wholeCi);
//        System.out.println("wholePi = " + wholePi);
        return wholeCi <= C && wholePi >= P;
    }

    //2.1-对上一轮的结果进行检查修复
    public static void check(Firefly firefly) {
        boolean flag = judgeWolf(firefly);
        //如果不满足约束条件，也即返回值为false时，则进入约束处理函数
        if (!flag) {
//            System.out.println("id为：" + curWolf.getId() + "的个体需要修复");
            repairWolf(firefly);
        }
    }

    //1.初始化种群
    public static List<Firefly> generateSolution() {
        List<Firefly> curSolution = new ArrayList<>();
        for (int i = 0; i < populationNum; i++) {
            List<Integer> curX = new ArrayList<>();
            for (int j = 0; j < dimension; j++) {
                if (random.nextDouble() > φc + 0.1) {
                    curX.add(0);
                } else {
                    curX.add(1);
                }
            }
            curSolution.add(new Firefly(i + 1, curX, null));
        }
        return curSolution;
    }

    //0.3-获取距离矩阵中的最大距离
    public static double getGlobalMinDistance() {
        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < disMatrix.length; i++) {
            //在上三角找
            for (int j = i + 1; j < disMatrix[0].length; j++) {
                if (disMatrix[i][j] < minDistance) {
                    minDistance = disMatrix[i][j];
                }
            }
        }
        return minDistance;
    }

    //0.2-导入数据集并读取ci和pi并封装实体类
    public static void capsulateData() {
        for (double v : N35_1.ci) {
            ci.add(v);
        }
        for (double v : N35_1.pi) {
            pi.add(v);
        }
        //封装实体类
        for (int i = 0; i < pi.size(); i++) {
            allFacilities.add(new Facility(i + 1, ci.get(i), pi.get(i), pi.get(i) / ci.get(i)));
        }
    }
}

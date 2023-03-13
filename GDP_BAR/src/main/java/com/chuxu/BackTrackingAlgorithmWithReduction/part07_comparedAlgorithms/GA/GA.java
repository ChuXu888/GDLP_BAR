package com.chuxu.BackTrackingAlgorithmWithReduction.part07_comparedAlgorithms.GA;

import com.chuxu.entity.Facility;
import com.chuxu.BackTrackingAlgorithmWithReduction.part01_preHandleData.RandomCase.N27_1;
import com.chuxu.BackTrackingAlgorithmWithReduction.part07_comparedAlgorithms.GA.entity.Individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GA {
    //初始条件相关变量
    private static final int dimension = N27_1.dimension;  //问题规模
    private static final double[][] disMatrix = N27_1.disMatrix;  //距离矩阵
    private static final List<Double> pi = new ArrayList<>();
    private static final List<Double> ci = new ArrayList<>();
    private static final List<Facility> allFacilities = new ArrayList<>();
    private static final double φc = N27_1.φc;
    private static final double φp = N27_1.φp;
    private static double P;
    private static double C;
    private static double globalMinDistance;  //距离矩阵中的最小值，用做惩罚量
    //遗传算法相关常量
    private static final int populationNum = 50;  //种群数量
    private static final int iterationNum = 1000;  //迭代次数
    private static final double crossProbability = 0.9;  //交叉概率
    private static final double variationProbability = 0.08;  //变异概率
    private static final double gap = 0.9;  //代沟
    private static final Random random = new Random();  //生成随机数的类的对象
    private static List<Double> bestFitnessVariation = new ArrayList<>();  //记录群体最佳适应值的变化过程

    public static void main(String[] args) throws Exception {
        //0.封装数据，计算P和C
        N27_1.preHandle();
        capsulateData();
        //计算P和C，求最小距离作为惩罚值
        C = ci.stream().reduce(0.0, Double::sum) * φc;
        P = pi.stream().reduce(0.0, Double::sum) * φp;
        globalMinDistance = getGlobalMinDistance();
        System.out.println("C = " + C);
        System.out.println("P = " + P);
        //记录算法开始运行时的时间
        long start = System.currentTimeMillis();
        //1.生成初始种群
        List<Individual> curIndividuals = initialPopulation();
        int iteration = 1;
        while (iteration <= iterationNum) {
            //2对上轮得到的解进行修正
            curIndividuals.forEach(GA::check);
            //3计算适应值
            List<Individual> curIndividualsWithFitness = calFitness(curIndividuals);
            //4.记录缺额vacancyNum，然后保存curIndividualsWithFitness中适应度最高的前vacancyNum个个体
            List<Individual> bakIndividuals = saveBak(curIndividualsWithFitness);
            //5.计算累积概率
            List<Double> cumulativeProbability = cumulativeProbability(curIndividualsWithFitness);
            //6.选择操作
            List<Individual> afterSelect = select(curIndividualsWithFitness, cumulativeProbability);
            //7.交叉操作
            List<Individual> afterCross = cross(afterSelect);
            //8.变异操作
            List<Individual> afterVary = vary(afterCross);
            //9.重组操作
            curIndividuals = recombine(afterVary, bakIndividuals);
            //10.迭代次数加1
            iteration++;
        }
        //将最终得到的解修正并计算适应值，再展示出来
        curIndividuals.forEach(GA::check);
        List<Individual> endIndividuals = calFitness(curIndividuals);
        Collections.sort(endIndividuals);
        long end = System.currentTimeMillis();
        System.out.println("============================================");
        System.out.println("最优目标值的变化过程：");
//        bestFitnessVariation.forEach(System.out::println);
        System.out.println("bestFitnessVariation.size() = " + bestFitnessVariation.size());
        System.out.println("最优解 = " + curIndividuals.get(curIndividuals.size() - 1));
        System.out.println("最优解对应的最优值 = " + curIndividuals.get(curIndividuals.size() - 1).getFitness());
        System.out.println("求解耗时：" + (end - start)/1000.0 + "s");
    }

    //9.重组
    public static List<Individual> recombine(List<Individual> afterVary, List<Individual> bakIndividuals) {
        List<Individual> afterRecombine = new ArrayList<>();
        afterRecombine.addAll(afterVary);
        afterRecombine.addAll(bakIndividuals);
        return afterRecombine;
    }

    //8.变异
    public static List<Individual> vary(List<Individual> afterCross) {
        for (int i = 0; i < afterCross.size(); i++) {
            double randomProbability = random.nextDouble();
            if (variationProbability > randomProbability) {
                //在左半区生成varyDot1
                int varyDot1 = random.nextInt(dimension) / 2;
                //变异长度为染色体总长度的一半
                int varyDot2 = varyDot1 + dimension / 2;
                //逆序保存要变异的片段
                List<Integer> curIndividualCode = afterCross.get(i).getCode();
                for (int j = varyDot1; j <= varyDot2; j++) {
                    curIndividualCode.set(j, 1 - curIndividualCode.get(j));
                }
                afterCross.set(i, new Individual(curIndividualCode, null));
            }
        }
        return afterCross;
    }

    //7.交叉
    public static List<Individual> cross(List<Individual> afterSelect) {
        List<Individual> afterCross = new ArrayList<>();
        //随机生成的一个0-1之间的概率
        double randomProbability = random.nextDouble();
        //如果给定的交叉概率大于一个随机生成的一个0-1之间的随机数，那么就进行交叉操作，否则不进行交叉操作，返回传入的实参即可
        if (crossProbability > randomProbability) {
            //交叉的具体操作为，两两配对，交换后两位
            //如果是偶数个元素，就遍历一半元素，与相邻元素进行交叉
            for (int i = 0; i < afterSelect.size() - 1; i += 2) {
                //获取当前元素
                Individual individual1 = afterSelect.get(i);
                //获取当前元素的后一个元素
                Individual individual2 = afterSelect.get(i + 1);
                //0~AllGoods.size()-1之间随机生成一个交叉位置，注意左闭右开
                int crossDot = random.nextInt(dimension);
//                System.out.println("crossDot = " + crossDot);
                //交换两个个体的[0,crossDot]位元素
                List<Integer> newCodeOfIndividual1 = new ArrayList<>();
                List<Integer> newCodeOfIndividual2 = new ArrayList<>();
                for (int j = 0; j <= crossDot; j++) {
                    //新的解的前[0,crossDot]个元素是对方的
                    newCodeOfIndividual1.add(individual2.getCode().get(j));
                    newCodeOfIndividual2.add(individual1.getCode().get(j));
                }
                for (int j = crossDot + 1; j < dimension; j++) {
                    //新的解的剩余元素仍是自己的
                    newCodeOfIndividual1.add(individual1.getCode().get(j));
                    newCodeOfIndividual2.add(individual2.getCode().get(j));
                }
                //加入结果
                afterCross.add(new Individual(newCodeOfIndividual1, null));
                afterCross.add(new Individual(newCodeOfIndividual2, null));
//                System.out.println("===================");
            }
            //如果是奇数个元素，就遍历一半元素少一个，留一个不变异，也没啥，但是要先把最后一个不变异元素加入到afterCrossStringList中
            if (afterSelect.size() % 2 == 1) {
                afterCross.add(new Individual(afterSelect.get(afterSelect.size() - 1).getCode(), null));
            }
        } else {
            afterCross.addAll(afterSelect);
        }
        return afterCross;
    }

    //6.选择
    public static List<Individual> select(List<Individual> curIndividualsWithFitness, List<Double> cumulativeProbability) {
        //计算要选择的数量
        int selectNum = (int) (populationNum * gap);
        //定义返回的列表
        List<Individual> afterSelect = new ArrayList<>();
        //开始选择
        for (int i = 0; i < selectNum; i++) {
            double probability = random.nextDouble();
            //如果生成的概率小于第一个累积概率，选择后列表就加入第一个元素
            if (probability <= cumulativeProbability.get(0)) {
                afterSelect.add(curIndividualsWithFitness.get(0));
            } else {
                //否则就要进入循环，来遍历查找，由于有一种情况已经在外面判断过，故此处j从1开始
                for (int j = 1; j < cumulativeProbability.size(); j++) {
                    if (probability > cumulativeProbability.get(j - 1) && probability <= cumulativeProbability.get(j)) {
                        afterSelect.add(curIndividualsWithFitness.get(j));
                    }
                }
            }
        }
        return afterSelect;
    }

    //5.计算累积概率
    public static List<Double> cumulativeProbability(List<Individual> curIndividualsWithFitness) {
        List<Double> cumulativeProbability = new ArrayList<>();
        double allSum = curIndividualsWithFitness.stream().map(Individual::getFitness).reduce(0.0, Double::sum);
        ;  //全部和
        double curSum = 0;  //当前累计和，每遍历一个元素加一个
        //再求每个累积概率
        for (Individual individualsWithFitness : curIndividualsWithFitness) {
            curSum += individualsWithFitness.getFitness();
            cumulativeProbability.add(curSum / allSum);
        }
        return cumulativeProbability;
    }

    //4.计算选择的种群数量selectNum，并记录缺额vacancyNum，然后保存curIndividualsWithFitness中适应度最高的前vacancyNum个个体
    public static List<Individual> saveBak(List<Individual> curIndividualsWithFitness) {
        int selectNum = (int) (populationNum * gap);
        int vacancyNum = populationNum - selectNum;
        //复制一份
        List<Individual> tempCurIndividualsWithFitness = new ArrayList<>(curIndividualsWithFitness);
        //按照适应度从低到高进行排序
        Collections.sort(tempCurIndividualsWithFitness);
        bestFitnessVariation.add(tempCurIndividualsWithFitness.get(tempCurIndividualsWithFitness.size() - 1).getFitness());
        //新建一个列表保存适应度最高的前vacancyNum个个体
        List<Individual> bakIndividuals = new ArrayList<>();
        while (vacancyNum > 0) {
            Individual curBestIndividual = tempCurIndividualsWithFitness.get(tempCurIndividualsWithFitness.size() - 1);
            bakIndividuals.add(curBestIndividual);
            tempCurIndividualsWithFitness.remove(curBestIndividual);
            vacancyNum--;
        }
        return bakIndividuals;
    }

    //3.计算当前种群的适应值
    public static List<Individual> calFitness(List<Individual> curIndividuals) {
        for (Individual curIndividual : curIndividuals) {
            //由于本修复方案无法保证修复之后为可行解，所以还要再进行一次判断。
            //如果是可行解，那就正常计算适应值；如果不是可行解，那就使用罚函数，例如直接返回距离矩阵中最小的值作为适应值
            double wholeCi = 0.0;
            double wholePi = 0.0;
            for (int i = 0; i < curIndividual.getCode().size(); i++) {
                if (curIndividual.getCode().get(i) == 1) {
                    wholeCi += ci.get(i);
                    wholePi += pi.get(i);
                }
            }

            if (wholeCi <= C && wholePi >= P) {
                //现在的问题是如何根据position获取这样一个子矩阵，用判断
                double minDistance = Double.MAX_VALUE;
                for (int i = 0; i < disMatrix.length; i++) {
                    //只有position[i] == 1.0的第i行才需要继续遍历
                    if (curIndividual.getCode().get(i) == 1) {
                        for (int j = i + 1; j < disMatrix[0].length; j++) {
                            //对于该行的每个数据，只有position[j] == 1.0的第j列才需要继续判断，同时j != i保证了不是对角线数字
                            if (curIndividual.getCode().get(j) == 1 && disMatrix[i][j] < minDistance) {
                                minDistance = disMatrix[i][j];
                            }
                        }
                    }
                }
                curIndividual.setFitness(minDistance);
            } else {
                curIndividual.setFitness(globalMinDistance);
            }
        }
        return curIndividuals;
    }

    //2.3-约束处理函数，如果一个个体不满足约束条件，则对其进行相应的处理，使其满足约束条件
    public static void repairWolf(Individual individual) {
        List<Integer> code = individual.getCode();
        double wholeCi = 0.0;
        double wholePi = 0.0;
        List<Facility> selectedFacilities = new ArrayList<>();  //存储当前被选中的所有个体
        for (int i = 0; i < code.size(); i++) {
            if (code.get(i) == 1) {
                selectedFacilities.add(allFacilities.get(i));
                wholeCi += ci.get(i);
                wholePi += pi.get(i);
            }
        }
        List<Facility> unSelectedFacilities = new ArrayList<>(allFacilities);
        unSelectedFacilities.removeAll(selectedFacilities);

        //观察未修复时position数组的状态
//        System.out.println("===================================================");
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
                code.set(bestFacility.getId() - 1, 1);
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
            code.set(worstFacility.getId() - 1, 0);
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
                code.set(bestFacility.getId() - 1, 1);
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
    }

    //2.2-判断函数，判断某个染色体个体解码出来的装包方案是否能够满足背包的载重量约束，进而判断该个体是否合理
    public static boolean judgeWolf(Individual individual) {
        List<Integer> code = individual.getCode();
        double wholeCi = 0.0;
        double wholePi = 0.0;
        for (int i = 0; i < code.size(); i++) {
            if (code.get(i) == 1) {
                wholeCi += ci.get(i);
                wholePi += pi.get(i);
            }
        }
        return wholeCi <= C && wholePi >= P;
    }

    //2.1-对上一轮的结果进行检查修复
    public static void check(Individual individual) {
        boolean flag = judgeWolf(individual);
        //如果不满足约束条件，也即返回值为false时，则进入约束处理函数
        if (!flag) {
//            System.out.println("id为：" + curWolf.getId() + "的个体需要修复");
            repairWolf(individual);
        }
    }

    //1.生成初始种群，即populationNum个长度为100【备选物品的数量】的字符串，每个字符要么为0要么为1
    public static List<Individual> initialPopulation() {
        List<Individual> list = new ArrayList<>();
        for (int i = 0; i < populationNum; i++) {
            List<Integer> curCode = new ArrayList<>();
            for (int j = 0; j < dimension; j++) {
                if (random.nextDouble() > φc + 0.1) {
                    curCode.add(0);
                } else {
                    curCode.add(1);
                }
            }
            list.add(new Individual(curCode, null));
        }
        return list;
    }

    //0.3-获取距离矩阵中的最大距离
    public static double getGlobalMinDistance() {
        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < disMatrix.length; i++) {
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
        for (double v : N27_1.ci) {
            ci.add(v);
        }
        for (double v : N27_1.pi) {
            pi.add(v);
        }
        //封装实体类
        for (int i = 0; i < pi.size(); i++) {
            allFacilities.add(new Facility(i + 1, ci.get(i), pi.get(i), pi.get(i) / ci.get(i)));
        }
    }
}

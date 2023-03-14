package com.chuxu.BackTrackingAlgorithmWithReduction.part07_comparedAlgorithms.GWO;

import com.chuxu.entity.Facility;
import com.chuxu.BackTrackingAlgorithmWithReduction.part01_preHandleData.RandomCase.N27_1;
import com.chuxu.BackTrackingAlgorithmWithReduction.part07_comparedAlgorithms.GWO.entity.GreyWolf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GWO {
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
    //灰狼优化算法相关参数
    private static final int N = 50;  //初始种群数量
    private static final int maxIteration = 1000;
    private static final double crossProbability = 0.9;  //交叉概率
    private static final double variationProbability = 0.1;  //变异概率
    private static final double setBadBetaProbability = 0.1;  //变异概率
    private static final double w_max = 0.9;  //权重系数
    private static final double w_min = 0.5;  //权重系数
    private static final double k = 0.05;  //权重系数
    private static double a;
    private static double r1;  //随机数r1
    private static double r2;  //随机数r2
    private static double[] A1 = new double[dimension];
    private static double[] A2 = new double[dimension];
    private static double[] A3 = new double[dimension];
    private static double[] C1 = new double[dimension];
    private static double[] C2 = new double[dimension];
    private static double[] C3 = new double[dimension];
    private static GreyWolf W_alpha;  //头狼
    private static GreyWolf W_beta;  //第二好的狼
    private static GreyWolf W_delta;  //第三好的狼
    private static List<GreyWolf> wolves = new ArrayList<>();
    private static final Random random = new Random();
    private static List<Double> bestFitnessVariation = new ArrayList<>();  //记录群体最佳适应值的变化过程

    public static void main(String[] args) throws Exception {
        //0.导入数据集并封装数据
        System.out.println("========================================================");
        N27_1.preHandle();
        capsulateData();
        allFacilities.forEach(System.out::println);
        globalMinDistance = getGlobalMinDistance();

        //计算P和C
        System.out.println("========================================================");
        C = ci.stream().reduce(0.0, Double::sum) * φc;
        P = pi.stream().reduce(0.0, Double::sum) * φp;
        System.out.println("P = " + P);
        System.out.println("C = " + C);

        //从这里再开始统计灰狼算法的耗时
        long start = System.currentTimeMillis();

        //1.生成初始种群
        initialWolves();
        System.out.println("========================================================");
        System.out.println("生成的初始种群：");
        wolves.forEach(System.out::println);

        //2.对初始种群进行检查与修正
        System.out.println("========================================================");
        wolves.forEach(GWO::check);

        //3.计算当前种群的适应值
        System.out.println("========================================================");
        wolves.forEach(GWO::calculateFitness);
        Collections.sort(wolves);
        System.out.println("查看初始种群的适应值：");
        wolves.forEach(System.out::println);

        //4.设置头狼、二狼、三狼
        setLeadWolves();

        int iteration = 1;
        while (iteration <= maxIteration) {
            //5.更新干活狼ω的位置，然后修正它，并重新计算适应值
            System.out.println("========================================================");
            for (GreyWolf wolf : wolves) {
                //更新干活狼ω的位置
                updateOmegaWolf(wolf, iteration);
                //对各种操作后的干活狼进行修复
//                check(wolf);
//                calculateFitness(wolf);
            }

            //再根据更新后的结果进行随机地交叉和变异操作
            cross(wolves);
            variation(wolves);
            wolves.forEach(GWO::check);
            wolves.forEach(GWO::calculateFitness);
            System.out.println("========================================================");
//            System.out.println("更新位置，交叉，变异，修复，并重新计算适应值后的干活狼🐺wolves = ");
//            wolves.forEach(System.out::println);

            //6.把前三名的狼重新加进来排序
            System.out.println("========================================================");
            wolves.add(W_alpha.clone());
            wolves.add(W_beta.clone());
            wolves.add(W_delta.clone());
            Collections.sort(wolves);
//            System.out.println("将前三名的狼重新加进来排序：");
//            wolves.forEach(System.out::println);

            //7.重新设置前三名的狼
            setLeadWolves();

            //迭代次数加1
            iteration++;
        }
        System.out.println("========================================================");
        System.out.println("最优目标值的变化过程：");
//        bestFitnessVariation.forEach(System.out::println);
        System.out.println("bestFitnessVariation.size() = " + bestFitnessVariation.size());
        System.out.println("最优解为：W_alpha = " + W_alpha);
        judgeWolf(W_alpha);
        long end = System.currentTimeMillis();
        System.out.println("程序耗时：" + (end - start) / 1000.0 + "s");
    }


    public static void variation(List<GreyWolf> wolves) {
        for (GreyWolf wolf : wolves) {
            //随机生成的一个0-1之间的概率
            double randomProbability = random.nextDouble();
            if (variationProbability > randomProbability) {
                //计算总变异位数，dimension越大，变异位数越大
                int wholeCount = (int) (dimension * variationProbability);
                int step = dimension / wholeCount;  //最小步长
                int curIndex = random.nextInt(step);  //在0到最小步长之间随机生成第一个索引
                while (wholeCount > 0) {
                    wolf.getPosition()[curIndex] = 1.0 - wolf.getPosition()[curIndex];  //变异
                    curIndex += step;  //指针后移步长step
                    wholeCount--;
                }
            }
        }
    }

    public static void cross(List<GreyWolf> wolves) {
        //随机生成的一个0-1之间的概率
        double randomProbability = random.nextDouble();
        //如果给定的交叉概率大于一个随机生成的一个0-1之间的随机数，那么就进行交叉操作，否则不进行交叉操作，返回传入的实参即可
        if (crossProbability > randomProbability) {
            //交叉的具体操作为，两两配对，交换后两位
            //如果是偶数个元素，就遍历一半元素，与相邻元素进行交叉
            for (int k = 0; k < wolves.size() - 1; k += 2) {
                //获取当前狼的选址方案
                double[] position1 = wolves.get(k).getPosition();
                //获取当前狼的后一只狼的选址方案
                double[] position2 = wolves.get(k + 1).getPosition();
                //在左半区生成一个交叉位置，右半区生成一个交叉位置
                int crossDot1 = random.nextInt(dimension / 2);
                int crossDot2 = random.nextInt(dimension / 2) + dimension / 2;
                //交换两只狼的选址方案中的[crossDot1,crossDot2]的部分
                List<Double> tempPiece1 = new ArrayList<>();
                List<Double> tempPiece2 = new ArrayList<>();
                for (int i = crossDot1; i <= crossDot2; i++) {
                    tempPiece1.add(position1[i]);
                    tempPiece2.add(position2[i]);
                }
                for (int i = crossDot1; i <= crossDot2; i++) {
                    position1[i] = tempPiece2.get(0);
                    tempPiece2.remove(0);
                    position2[i] = tempPiece1.get(0);
                    tempPiece1.remove(0);
                }
            }
        }
    }

    //6.sigmoid映射函数
    public static double sigmoid(double x) {
        return 1 / (1 + Math.exp((-10) * (x - 0.5)));
    }

    //5.2-处理三个过度变量X1，X2，X3
    public static void processTransition(double[] X, double[] curPosition, double[] leadPosition, double[] A, double[] C) {
        for (int i = 0; i < dimension; i++) {
            r1 = random.nextDouble();  //处理每个维度的分量时，随机数都重新生成
            r2 = random.nextDouble();
            A[i] = 2 * a * r1 - a;
            C[i] = 2 * r2;
            X[i] = leadPosition[i] - A[i] * Math.abs(C[i] * leadPosition[i] - curPosition[i]);
        }
    }

    //5.1-更新干活狼的位置
    public static void updateOmegaWolf(GreyWolf wolf, int iteration) {
        //在一个特定的迭代轮数中，a的值是固定的，后者说其每一个维度的分量都是一样的
//        a = -2 * (iteration / (maxIteration * 1.0)) + 2;
        a = Math.cos(Math.PI * iteration / (maxIteration * 1.0)) + 1;
        //定义三个更新位置时要用到的过渡变量
        double[] X1 = new double[dimension];
        double[] X2 = new double[dimension];
        double[] X3 = new double[dimension];
        //对三个过渡变量X1，X2，X3分开处理，内部再对对每一个维度的数值分开处理
        double[] curPosition = wolf.getPosition();

        double[] alphaPosition = W_alpha.getPosition();
        processTransition(X1, curPosition, alphaPosition, A1, C1);

        double[] betaPosition = W_beta.getPosition();
        processTransition(X2, curPosition, betaPosition, A2, C2);

        double[] deltaPosition = W_delta.getPosition();
        processTransition(X3, curPosition, deltaPosition, A3, C3);

        //更新位置
        for (int i = 0; i < dimension; i++) {
            double ω = w_min + (w_max - w_min) * Math.pow((maxIteration - iteration) * 1.0 / maxIteration, 0.8) + k * random.nextDouble();
            double tempPosition = ω * (X1[i] + X2[i] + X3[i]) / 3.0;
//            double tempPosition = (X1[i] + X2[i] + X3[i]) / 3.0;
//            double tempPosition = (0.4 * X1[i] + 0.35 * X2[i] + 0.25 * X3[i]);
            double sigmoidValue = sigmoid(tempPosition);
            //sigmoidValue越大，生成的[0,1]随机数比sigmoidValue小的概率就越大，从而curPosition[i] = 1的可能性就越大
            if (random.nextDouble() < sigmoidValue) {
                curPosition[i] = 1;
            } else {
                curPosition[i] = 0;
            }
        }
        wolf.setPosition(curPosition);
    }

    //4.设置前三名的狼
    public static void setLeadWolves() {
        System.out.println("本轮前三名的狼🐺为：");

        W_alpha = wolves.get(wolves.size() - 1).clone();
        wolves.remove(wolves.size() - 1);
        System.out.println("W_alpha = " + W_alpha);
        bestFitnessVariation.add(W_alpha.getFitness());

//        if (random.nextDouble() < setBadBetaProbability) {
//            W_beta = wolves.get(0).clone();
//            wolves.remove(0);
//            System.out.println("W_beta = " + W_beta);
//        } else {
//            W_beta = wolves.get(wolves.size() - 1).clone();
//            wolves.remove(wolves.size() - 1);
//            System.out.println("W_beta = " + W_beta);
//        }
        W_beta = wolves.get(wolves.size() - 1).clone();
        wolves.remove(wolves.size() - 1);
        System.out.println("W_beta = " + W_beta);

        W_delta = wolves.get(wolves.size() - 1).clone();
        wolves.remove(wolves.size() - 1);
        System.out.println("W_delta = " + W_delta);
    }

    //3.计算适应值
    public static void calculateFitness(GreyWolf wolf) {
        //由于本修复方案无法保证修复之后为可行解，所以还要再进行一次判断。
        //如果是可行解，那就正常计算适应值；如果不是可行解，那就使用罚函数，例如直接返回距离矩阵中最小的值作为适应值
        double[] position = wolf.getPosition();
        double wholeCost = 0.0;
        double wholeCapacity = 0.0;
        for (int i = 0; i < position.length; i++) {
            if (position[i] == 1.0) {
                //获取唯一标识id，通过id-1从dataSet_weight中获取该物品的重量
//                int curId = wolf.getId();
//                wholeWeight += dataSet_weight.get(curId - 1);
                wholeCost += ci.get(i);
                wholeCapacity += pi.get(i);
            }
        }

        if (wholeCost <= C && wholeCapacity >= P) {
            //现在的问题是如何根据position获取这样一个子矩阵，用判断
            double minDistance = Double.MAX_VALUE;
            for (int i = 0; i < disMatrix.length; i++) {
                //只有position[i] == 1.0的第i行才需要继续遍历
                if (position[i] == 1.0) {
                    for (int j = i + 1; j < disMatrix[0].length; j++) {
                        //对于该行的每个数据，只有position[j] == 1.0的第j列才需要继续判断，同时j != i保证了不是对角线数字
                        if (position[j] == 1.0 && disMatrix[i][j] < minDistance) {
                            minDistance = disMatrix[i][j];
                        }
                    }
                }
            }
            wolf.setFitness(minDistance);
        } else {
            wolf.setFitness(globalMinDistance);
        }
    }

    //获取距离矩阵中的最大距离
    public static double getGlobalMinDistance() {
        double minDistance = Double.MAX_VALUE;
        for (double[] matrix : disMatrix) {
            for (double curDistance : matrix) {
                if (curDistance < minDistance) {
                    minDistance = curDistance;
                }
            }
        }
        return minDistance;
    }

    //2.3-约束处理函数，如果一个个体不满足约束条件，则对其进行相应的处理，使其满足约束条件
    public static void repairWolf(GreyWolf wolf) {
        double[] position = wolf.getPosition();
        double wholeCost = 0.0;
        double wholeCapacity = 0.0;
        List<Facility> selectedFacilities = new ArrayList<>();  //存储当前被选中的所有个体
        for (int i = 0; i < position.length; i++) {
            if (position[i] == 1.0) {
                selectedFacilities.add(allFacilities.get(i));
                wholeCost += ci.get(i);
                wholeCapacity += pi.get(i);
            }
        }
        List<Facility> unSelectedFacilities = new ArrayList<>(allFacilities);
        unSelectedFacilities.removeAll(selectedFacilities);

        //观察未修复时position数组的状态
//        System.out.println("未修复时wholeCost = " + wholeCost);
//        System.out.println("未修复时wholeCapacity = " + wholeCapacity);

        //下面分成两大类进行处理：
        //(1)如果P不够而C没超，那就先加性价比高的物品直到P够了，此时C如果超了，那就同下面(2)中的P够C超；如果C没超那就已经修复好了。
        if (wholeCapacity < P && wholeCost < C) {
            //将未被选中的所有个体按照性价比升序排列
            Collections.sort(unSelectedFacilities);
            while (wholeCapacity < P) {
                //获取unSelectedNodes中性价比最高的结点
                Facility bestFacility = unSelectedFacilities.get(unSelectedFacilities.size() - 1);
                //进行相关修改
                wholeCapacity += bestFacility.getPi();
                wholeCost += bestFacility.getCi();
                position[bestFacility.getId() - 1] = 1.0;
                selectedFacilities.add(bestFacility);
                unSelectedFacilities.remove(unSelectedFacilities.size() - 1);

            }
//            System.out.println("P不够C没超，修复后wholeCost = " + wholeCost);
//            System.out.println("P不够C没超，修复后wholeCapacity = " + wholeCapacity);
            //这个if如果不满足就变成了P够C超的局面，就转到(2)的处理流程了
            if (wholeCost <= C) {
                return;
            }
        }

        //(2)如果P不够C超或者P够C超，都先移除性价比低的物品直到C不超，然后增加性价比高的物品；如果还不是可行解，那就用罚函数了。
        //将被选中的所有个体按照性价比升序排列
        Collections.sort(selectedFacilities);

        //进行第一步修复
        while (wholeCost > C) {
            //获取性价比最低的结点
            Facility worstFacility = selectedFacilities.get(0);
            //一定要先把这个重量减掉，再把物品移除
            wholeCost -= worstFacility.getCi();
            wholeCapacity -= worstFacility.getPi();
            //由于将性价比最低的结点移除了，那么需要获取这个被移除结点的id，
            position[worstFacility.getId() - 1] = 0.0;
            //最后移除该结点
            selectedFacilities.remove(0);
            //在unSelectedNodes中添加该结点
            unSelectedFacilities.add(worstFacility);
        }

        //观察第一步修复时后position数组的状态
//        System.out.println("第一步修复后wholeCost = " + wholeCost);
//        System.out.println("第一步修复后wholeCapacity = " + wholeCapacity);

        //将未被选中的所有个体按照性价比升序排列
        Collections.sort(unSelectedFacilities);

        //进行第二步修复
        while (true) {
            //此时在列表末尾的是性价比最高的商品
            Facility bestFacility = unSelectedFacilities.get(unSelectedFacilities.size() - 1);
            //若将该商品加入后，重量抄了，就break
            if (bestFacility.getCi() + wholeCost > C) {
                break;
            } else {
                //获取这个被添加商品的id，在chars数组中同步修改其为1
                position[bestFacility.getId() - 1] = 1.0;
                //将当前结点的成本和容量加上去
                wholeCost += bestFacility.getCi();
                wholeCapacity += bestFacility.getPi();
                //将当前物品加入到selectedGoods中，这一步可有可无，因为最终要的只是chars
                selectedFacilities.add(bestFacility);
                //从unSelectedGoods移除该物品，否则一直处理的都是那一个商品。所以之前的代码也写错了
                unSelectedFacilities.remove(unSelectedFacilities.size() - 1);
            }
        }

        //观察第二步修复后position数组的状态
//        System.out.println("第二步修复后wholeCost = " + wholeCost);
//        System.out.println("第二步修复后wholeCapacity = " + wholeCapacity);

        //设置修复后的结果
        wolf.setPosition(position);
    }

    //2.2-判断函数，判断某个染色体个体解码出来的装包方案是否能够满足背包的载重量约束，进而判断该个体是否合理
    public static boolean judgeWolf(GreyWolf wolf) {
        double[] position = wolf.getPosition();
        double wholeCost = 0.0;
        double wholeCapacity = 0.0;
        for (int i = 0; i < position.length; i++) {
            if (position[i] == 1.0) {
                //获取唯一标识id，通过id-1从dataSet_weight中获取该物品的重量
//                int curId = wolf.getId();
//                wholeWeight += dataSet_weight.get(curId - 1);
                wholeCost += ci.get(i);
                wholeCapacity += pi.get(i);
            }
        }
//        System.out.println("wholeCost = " + wholeCost);
//        System.out.println("wholeCapacity = " + wholeCapacity);
        return wholeCost <= C && wholeCapacity >= P;
    }

    //2.1-对上一轮的结果进行检查修复
    public static void check(GreyWolf curWolf) {
        boolean flag = judgeWolf(curWolf);
        //如果不满足约束条件，也即返回值为false时，则进入约束处理函数
        if (!flag) {
//            System.out.println("id为：" + curWolf.getId() + "的个体需要修复");
            repairWolf(curWolf);
        }
    }

    //1.初始化狼群
    public static void initialWolves() {
        for (int i = 0; i < N; i++) {
            double[] position = new double[dimension];
            //根据有多少维决定生成多少个分量，且位于给定的上下界之间
            for (int j = 0; j < dimension; j++) {
                //根据成本上限和总成本的比例，也就是φc，来决定这个比例【或者再稍微加一点也行】，否则生成的几乎都不是可行解
                if (random.nextDouble() > φc + 0.1) {
                    position[j] = 0;
                } else {
                    position[j] = 1;
                }
            }
            wolves.add(new GreyWolf(i + 1, dimension, position));
        }
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
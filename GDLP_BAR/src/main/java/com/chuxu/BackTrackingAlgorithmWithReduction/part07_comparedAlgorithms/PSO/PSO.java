package com.chuxu.BackTrackingAlgorithmWithReduction.part07_comparedAlgorithms.PSO;

import com.chuxu.BackTrackingAlgorithmWithReduction.part01_preHandleData.RandomCase.N35_1;
import com.chuxu.BackTrackingAlgorithmWithReduction.part07_comparedAlgorithms.PSO.entity.Particle;
import com.chuxu.entity.Facility;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PSO {
    //初始条件相关变量
    private static final int dimension = N35_1.dimension;  //问题规模
    private static final double[][] disMatrix=N35_1.disMatrix;  //距离矩阵
    private static final List<Double> pi = new ArrayList<>();
    private static final List<Double> ci = new ArrayList<>();
    private static final List<Facility> allFacilities = new ArrayList<>();
    private static final double φc = N35_1.φc;  //这个数值越小，越难获得可行解
    private static final double φp = N35_1.φp;  //这个数值越大，越难获得可行解
    private static double P;
    private static double C;
    private static double globalMinDistance;  //距离矩阵中的最小值，用做惩罚量
    private static final Random random = new Random();
    //粒子群优化算法相关常量
    private static final int particleNum = 50;  //粒子数
    private static final int maxIteration = 1000;  //最大迭代轮数
    private static final double maxV = 10;  //最大速度限制
    private static final double minV = -10;  //最小速度限制
    private static final int c1 = 2;  //个体学习因子
    private static final int c2 = 2;  //群体学习因子
    private static final double w_max = 0.9;  //权重系数
    private static final double w_min = 0.5;  //权重系数
    private static List<Integer> Gbest = new ArrayList<>();  //记录群体最佳
    private static double GbestFitness = Double.MIN_VALUE;  //记录群体最佳对应的适应值，比它大才替换
    private static List<Double> GbestFitnessVariation = new ArrayList<>();  //记录群体最佳适应值的变化过程

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
        //进入循环迭代
        int iteration = 1;
        while (iteration <= maxIteration) {
            //1.初始化各粒子的位置和速度
            List<Particle> particles = generateSolution();
//            particles.forEach(System.out::println);
            //2.对初始化的各粒子进行修正，使之满足约束条件
            particles.forEach(PSO::check);
            //3.计算种群中粒子的适应度，顺带更新个体最佳Pbest
            List<Double> curRoundFitnesses = calFitness(particles);
            System.out.println("curRoundFitnesses = " + curRoundFitnesses);
            //4.更新全局最佳Gbest
            updateGbest(particles);
            System.out.println("更新后Gbest = " + Gbest);
            System.out.println("更新后GbestFitness = " + GbestFitness);
            GbestFitnessVariation.add(GbestFitness);
            //5.根据公式更新速度和位置
            updateInformation(particles, iteration);
            //6.当前迭代轮数加1
            iteration++;
            System.out.println("===============================================================");
        }
        long end = System.currentTimeMillis();
        System.out.println("===============================================================");
//        System.out.println("最优目标值的变化过程：");
//        GbestFitnessVariation.forEach(System.out::println);
//        System.out.println("GbestFitnessVariation.size() = " + GbestFitnessVariation.size());
        System.out.println("最优解 = " + Gbest);
        System.out.println("最优目标值 = " + GbestFitness); //因为GbestValue是全局最优，是所有粒子都认同的一个结果
        System.out.println("程序耗时：" + (end - start) / 1000.0);
    }

    //5.根据公式更新速度和位置
    public static void updateInformation(List<Particle> particles, int iteration) {
        for (Particle particle : particles) {
            List<Integer> curX = particle.X;
            List<Double> curV = particle.V;
            List<Integer> curPbest = particle.Pbest;
            for (int i = 0; i < curX.size(); i++) {
                double rand_1 = random.nextDouble();
                double rand_2 = random.nextDouble();
                double adjustValue_V = (w_max - ((w_max - w_min) * iteration) / maxIteration) * curV.get(i) +
                        c1 * rand_1 * (curPbest.get(i) - curX.get(i)) +
                        c2 * rand_2 * (Gbest.get(i) - curX.get(i));
                if (adjustValue_V > maxV) {
                    particle.V.set(i, maxV);
                } else if (adjustValue_V < minV) {
                    particle.V.set(i, minV);
                } else {
                    particle.V.set(i, adjustValue_V);
                }
                //对x的每一个分量以一定的概率做变异操作：因为这是离散问题，x的每个分量只能为0或1，不像连续问题，直接根据速度算一个量赋给x
                double variationProbability = 1 / (1 + Math.exp(-particle.V.get(i)));
                double randomProbability = random.nextDouble();
                if (variationProbability > randomProbability) {
                    particle.X.set(i, 1);
                } else {
                    particle.X.set(i, 0);
                }
            }
        }
    }

    //4.更新全局最佳Gbest
    public static void updateGbest(List<Particle> particles) {
        //5.1将更新过个体最佳Pbest的粒子复制一份
        List<Particle> tempParticles = new ArrayList<>(particles);
        //5.2将粒子按照个体最佳适应度由低到高进行排序
        Collections.sort(tempParticles);
        //5.3获取最高个体最佳适应度的个体，将其与全局最佳进行比较
        Particle curRoundBestParticle = tempParticles.get(tempParticles.size() - 1);
        if (curRoundBestParticle.PbestFitness > GbestFitness) {
            Gbest = new ArrayList<>(curRoundBestParticle.X);
            GbestFitness = curRoundBestParticle.PbestFitness;
        }
    }

    //3.2计算单个粒子的适应度，之所以分离开来，是因为后面有只计算单个粒子的适应度的情形
    public static double calFitnessSingle(Particle particle) {
        //由于本修复方案无法保证修复之后为可行解，所以还要再进行一次判断。
        //如果是可行解，那就正常计算适应值；如果不是可行解，那就使用罚函数，例如直接返回距离矩阵中最小的值作为适应值
        List<Integer> curX = particle.X;
        double wholeCi = 0.0;
        double wholePi = 0.0;
        for (int i = 0; i < curX.size(); i++) {
            if (curX.get(i) == 1) {
                wholeCi += ci.get(i);
                wholePi += pi.get(i);
            }
        }

        if (wholeCi <= C && wholePi >= P) {
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
            return minDistance;
        } else {
            return globalMinDistance;
        }
    }

    //3.1计算所有粒子的适应度
    public static List<Double> calFitness(List<Particle> particles) {
        List<Double> fitnesses = new ArrayList<>();
        for (Particle particle : particles) {
            double curFitness = calFitnessSingle(particle);
            //如果是第一轮，Pbest为null，那就直接设置；否则就进行比较，更优则替换
            if (particle.Pbest == null) {
                particle.Pbest = new ArrayList<>(particle.X);
                particle.PbestFitness = curFitness;
            } else if (curFitness > particle.PbestFitness) {
                particle.Pbest = new ArrayList<>(particle.X);
                particle.PbestFitness = curFitness;
            }
            particle.curRoundFitness = curFitness;
            fitnesses.add(curFitness);
        }
        return fitnesses;
    }

    //2.3-约束处理函数，如果一个个体不满足约束条件，则对其进行相应的处理，使其满足约束条件
    public static void repairWolf(Particle particle) {
        List<Integer> curX = particle.X;
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
        particle.X = curX;
    }

    //2.2-判断函数，判断某个染色体个体解码出来的装包方案是否能够满足背包的载重量约束，进而判断该个体是否合理
    public static boolean judgeWolf(Particle particle) {
        List<Integer> curX = particle.X;
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
    public static void check(Particle particle) {
        boolean flag = judgeWolf(particle);
        //如果不满足约束条件，也即返回值为false时，则进入约束处理函数
        if (!flag) {
//            System.out.println("id为：" + curWolf.getId() + "的个体需要修复");
            repairWolf(particle);
        }
    }

    //1.生成初始解
    public static List<Particle> generateSolution() {
        List<Particle> particles = new ArrayList<>();
        for (int i = 0; i < particleNum; i++) {
            List<Integer> X = new ArrayList<>();
            List<Double> V = new ArrayList<>();
            //生成位置分量，也即0或1
            for (int j = 0; j < dimension; j++) {
                if (random.nextDouble() > φc + 0.1) {
                    X.add(0);
                } else {
                    X.add(1);
                }
                V.add(random.nextDouble() * (maxV - minV) + minV);
            }
            particles.add(new Particle(X, V, null, null, null));
        }
        return particles;
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
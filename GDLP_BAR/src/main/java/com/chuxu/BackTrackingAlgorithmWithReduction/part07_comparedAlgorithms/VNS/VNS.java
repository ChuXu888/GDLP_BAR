package com.chuxu.BackTrackingAlgorithmWithReduction.part07_comparedAlgorithms.VNS;

import com.chuxu.BackTrackingAlgorithmWithReduction.part01_preHandleData.RandomCase.N35_1;
import com.chuxu.entity.Facility;

import java.util.*;
import java.util.stream.Collectors;

//两轮局部搜索造成很慢，这一点在源文献中提到，500规模的问题在该作者的计算机中无法在3600秒内运行完毕
public class VNS {
    //初始条件相关变量
    private static final int dimension = N35_1.dimension;  //问题规模
    private static final double[][] disMatrix= N35_1.disMatrix;  //距离矩阵
    private static final List<Double> pi = new ArrayList<>();
    private static final List<Double> ci = new ArrayList<>();
    private static final List<Facility> allFacilities = new ArrayList<>();
    private static final double φc = N35_1.φc;  //这个数值越小，越难获得可行解
    private static final double φp = N35_1.φp;  //这个数值越大，越难获得可行解
    private static double P;
    private static double C;
    private static final Random random = new Random();
    //变邻域搜索算法相关变量
    private static final int it_max = 8;
    private static final double k_max = 0.5;
    private static LinkedHashSet<Facility> X_best = new LinkedHashSet<>();  //存储最优解
    private static int localSearchCount;  //进行局部的次数
    private static double best_fitness = Double.MIN_VALUE;  //存储最优目标值

    public static void main(String[] args) throws Exception {
        //0.封装数据，计算P和C
        N35_1.preHandle();
        capsulateData();
        //计算P和C
        C = ci.stream().reduce(0.0, Double::sum) * φc;
        P = pi.stream().reduce(0.0, Double::sum) * φp;
        System.out.println("C = " + C);
        System.out.println("P = " + P);

        long start = System.currentTimeMillis();

        //生成it_max个初始解作为出发点
        for (int i = 0; i < it_max; i++) {
            //1.生成一个初始解，并将其作为当前最好解
            //【重要】：当φc比较小时，使用C1和C2方案很难得到一个可行解，最终都走了兜底方案
            //此时也就无法享受C1和C2带来的多样性了，需要将φc提高才能正确的试验
            LinkedHashSet<Facility> X = generateSolution_C2();
//            LinkedHashSet<Facility> X = generateSolution_C1();
            boolean feasibleFlag = checkFeasible(X);
            System.out.println("======================================================");
            System.out.println("feasibleFlag = " + feasibleFlag);
            if (!feasibleFlag) {
                //如果没有得到可行解，再启用兜底方案
                X = generateSolution_bak();
            }
            double X_fitness = calculateFitness(new ArrayList<>(X));

            //2.对当前解进行邻域搜索并寻找局部最优
            X = localSearch(X);

            //对当前解进行扰动，根据k_max确定当前要扰动的轮数
            int realK_max = (int) Math.max(1, X.size() * k_max);
            //进行realK_max轮扰动，i=1 to realK_max，第i轮扰动就删除i个结点，然后补充
            //k越大，对当前解的变动就越大
            int k = 1;
            while (k < realK_max) {
//                System.out.println("======================================================");
//                System.out.println("k = " + k);
                //3.根据当前k的值决定扰动多少个bit，并进行扰动操作
                LinkedHashSet<Facility> X_shake = shake(X, k);
                //4.对当前解进行邻域搜索并寻找局部最优
                LinkedHashSet<Facility> X_improved = localSearch(X_shake);
                //5.判定X_improved(X'')和X_improved哪个更好
                double X_improved_fitness = calculateFitness(new ArrayList<>(X_improved));
                if (checkFeasible(X_improved) && X_improved_fitness > X_fitness) {
//                    System.out.println("======================================================");
//                    System.out.println("第" + (i + 1) + "个初始解时，当前局部搜索得到的局部最优，优于本轮内部的全局最优，更新！");
//                    System.out.println("更新前：");
//                    System.out.println("本轮局部搜索得到的局部最优值为 = " + X_improved_fitness);
//                    System.out.println("第" + (i + 1) + "个初始解时，本轮内部的全局最优 = " + X_fitness);
                    X = new LinkedHashSet<>(X_improved);
                    X_fitness = X_improved_fitness;
                    k = 1;
//                    System.out.println("更新后：");
//                    System.out.println("本轮局部搜索得到的局部最优值为 = " + X_improved_fitness);
//                    System.out.println("第" + (i + 1) + "个初始解时，本轮内部的全局最优 = " + best_fitness);
                } else {
//                    System.out.println("======================================================");
//                    System.out.println("第" + (i + 1) + "个初始解时，局部搜索得到的局部最优，不优于本轮内部的全局最优，不更新！");
//                    System.out.println("本轮局部搜索得到的局部最优值为 = " + X_improved_fitness);
//                    System.out.println("第" + (i + 1) + "个初始解时，本轮内部的全局最优 = " + best_fitness);
                    k++;
                }
            }

            //5.当前第i个初始解的所有操作都已经结束了，此时X存的就是第i个初始解下的最好解，且一定为可行解，此处无需进行可行性判断
            //由于要进行it_max轮，所以把本轮最优和全局最优作比较，并确定要不要替换
            if (X_fitness > best_fitness) {
                System.out.println("======================================================");
                System.out.println("第" + (i + 1) + "个初始解时的最优值优于当前全局最优");
                System.out.println("X_fitness = " + X_fitness);
                System.out.println("best_fitness = " + best_fitness);
                X_best = new LinkedHashSet<>(X);
                best_fitness = X_fitness;
            }
        }

        //6.输出最优解及最优目标值
        long end = System.currentTimeMillis();
        System.out.println("======================================================");
        X_best.forEach(System.out::println);
        System.out.println("best_fitness = " + best_fitness);
        System.out.println("进行局部邻域搜索的次数为： " + localSearchCount);
        System.out.println("程序耗时：" + (end - start) / 1000.0);
    }

    private static boolean checkFeasible(LinkedHashSet<Facility> curSolution) {
        double wholeCi = curSolution.stream().map(Facility::getCi).reduce(0.0, Double::sum);
        double wholePi = curSolution.stream().map(Facility::getPi).reduce(0.0, Double::sum);
//        System.out.println("wholeCi = " + wholeCi);
//        System.out.println("wholePi = " + wholePi);
        return wholePi >= P && wholeCi <= C;
    }

    //3.扰动：从curSolution中删除k个元素，然后添加若干个元素
    private static LinkedHashSet<Facility> shake(LinkedHashSet<Facility> curSolution, int k) {
        //1.构造V\X以及X
        List<Facility> X = new ArrayList<>(curSolution);
        List<Facility> V_Minus_X = new ArrayList<>(allFacilities);
        V_Minus_X.removeAll(curSolution);
        //2.从X中随机地删除k个元素
        while (k > 0) {
            int randomIndex = random.nextInt(X.size());
            Facility targetFacility = X.get(randomIndex);
            X.remove(targetFacility);
            V_Minus_X.add(targetFacility);
            k--;
        }
        //3.从V\X中按照性价比添加元素，直到成本超过为止
        Collections.sort(V_Minus_X);
        double wholeCi = X.stream().map(Facility::getCi).reduce(0.0, Double::sum);
        while (true) {
            Facility bestFacility = V_Minus_X.get(V_Minus_X.size() - 1);
            if (wholeCi + bestFacility.getCi() > C) {
                break;
            }
            X.add(bestFacility);
            wholeCi += bestFacility.getCi();
            V_Minus_X.remove(bestFacility);
        }
        return new LinkedHashSet<>(X);
    }

    //2.2-计算目标值2，也即该解的各开设设施之间的距离之和
    private static double calculateSum(List<Facility> curSolution) {
        double wholeDistance = 0.0;
        for (Facility xi : curSolution) {
            for (Facility xj : curSolution) {
                //仅遍历上三角矩阵即可
                if (xj.getId() > xi.getId()) {
                    wholeDistance += disMatrix[xi.getId() - 1][xj.getId() - 1];
                }
            }
        }
        return wholeDistance;
    }

    //2.1-计算目标值
    private static double calculateFitness(List<Facility> curSolution) {
        double minDistance = Double.MAX_VALUE;
        for (Facility xi : curSolution) {
            for (Facility xj : curSolution) {
                //仅遍历上三角矩阵即可
                if (xj.getId() > xi.getId() && disMatrix[xi.getId() - 1][xj.getId() - 1] < minDistance) {
                    minDistance = disMatrix[xi.getId() - 1][xj.getId() - 1];
                }
            }
        }
        return minDistance;
    }

    //2.对当前解进行邻域搜索并寻找局部最优
    private static LinkedHashSet<Facility> localSearch(LinkedHashSet<Facility> curSolution) {
        //构造V\X以及传入的X
        List<Facility> X = new ArrayList<>(curSolution);
        List<Facility> V_Minus_X = new ArrayList<>(allFacilities);
        V_Minus_X.removeAll(curSolution);
        //定义当前X的目标值，后面的需要更新这个目标值作比较并决定是否更新这个目标值
        double X_fitness = Double.MIN_VALUE;
        //此处内部可能对X进行了修改，而此处又对X进行了遍历，这样有同时操作==>再复制一份专门用于遍历
        List<Facility> X_traverse = new ArrayList<>(X);
        List<Facility> V_Minus_X_traverse = new ArrayList<>(V_Minus_X);
        //进行交换操作，由于X_traverse始终不变，而X却始终在变，
        //所以X_copy<==X的X_copy中添加X_traverse的当前元素可能之前已经有过了，而又没有去重
        //并且X_copy<==X的X_copy中移除X_traverse的当前元素时也可能扑了个空，所以它的集合中元素数量是有可能增加的
        for (Facility xi : X_traverse) {
            for (Facility xj : V_Minus_X_traverse) {
                localSearchCount++;
                //构造新解，注意不能对原集合直接操作，否则会报ConcurrentModificationException，应该复制一份
                List<Facility> X_copy = new ArrayList<>(X);
                X_copy.remove(xi);
                X_copy.add(xj);
                List<Facility> V_Minus_X_copy = new ArrayList<>(V_Minus_X);
                V_Minus_X_copy.remove(xj);
                V_Minus_X_copy.add(xi);
                //在这里对X_copy和V_Minus_X_copy去重
                X_copy = X_copy.stream().distinct().collect(Collectors.toList());
                V_Minus_X_copy = V_Minus_X_copy.stream().distinct().collect(Collectors.toList());
                //新解的两个约束值
                double wholeCi = X_copy.stream().map(Facility::getCi).reduce(0.0, Double::sum);
                double wholePi = X_copy.stream().map(Facility::getPi).reduce(0.0, Double::sum);
                //对X和V\X根据性价比进行排序，以便ADD和DROP操作
                Collections.sort(X_copy);
                Collections.sort(V_Minus_X_copy);
                //如果容量不足且成本也还有剩余，那就尝试着再往里加
                if (wholePi < P && wholeCi <= C) {
                    while (wholePi < P) {
                        Facility bestFacility = V_Minus_X_copy.get(V_Minus_X_copy.size() - 1);
                        //更新集合及属性值
                        X_copy.add(bestFacility);
                        wholeCi += bestFacility.getCi();
                        wholePi += bestFacility.getPi();
                        V_Minus_X_copy.remove(bestFacility);
                    }
                } else {
                    //否则，也即容量超了或者成本超了，就尝试着删掉一些
                    while (wholeCi > C) {
//                        System.out.println("====================================================");
//                        System.out.println("容量超了或者成本超了，尝试着删掉一些！");
                        Facility worstFacility = X_copy.get(0);
                        //更新集合及属性值
                        X_copy.remove(worstFacility);
                        wholeCi -= worstFacility.getCi();
                        wholePi -= worstFacility.getPi();
                        V_Minus_X_copy.add(worstFacility);
//                        X_copy.forEach(System.out::println);
                    }
                }
                //得到新解为X_copy，判断其是否可行，以及有没有优于交换之前的解X，
                //此处对于两个解的目标值相同的情况也单独做了处理
                if (wholePi >= P && wholeCi <= C) {
                    double X_copy_fitness = calculateFitness(X_copy);
                    if (X_copy_fitness > X_fitness) {
//                        System.out.println("======================================================");
//                        System.out.println("xi = " + xi);
//                        System.out.println("xj = " + xj);
//                        System.out.println("X_copy_fitness = " + X_copy_fitness);
//                        System.out.println("X_fitness = " + X_fitness);
//                        System.out.println("此时的X_copy = ");
//                        X_copy.forEach(System.out::println);
//                        System.out.println("此时的X = ");
//                        X.forEach(System.out::println);
                        X = new ArrayList<>(X_copy);
                        X_fitness = X_copy_fitness;
                    } else if (X_copy_fitness == X_fitness) {
                        //对于目标值相等的情况，就进而比较两个解的【各开设设施之间的距离之和，其实就是MaxSum模型的目标函数】
                        if (calculateSum(X_copy) > calculateSum(X)) {
                            X = new ArrayList<>(X_copy);
                            X_fitness = X_copy_fitness;  //由于目标值相同，所以这一步可有可无
                        }
                    }
                }
            }
        }
        return new LinkedHashSet<>(X);
    }

    //1.3-生成一个初始解_兜底方案，但它的缺点是对于一个给定的数据集，它的结果是唯一的，不像C1和C2那样有随机性
    private static LinkedHashSet<Facility> generateSolution_bak() {
        //构建allFacilities的复制版S并按照性价比排序，初始化X
        List<Facility> V_Minus_X = new ArrayList<>(allFacilities);
        Collections.sort(V_Minus_X);
        LinkedHashSet<Facility> X = new LinkedHashSet<>();
        //初始化变量
        double wholeCi = 0.0, wholePi = 0.0;
        while (V_Minus_X.size() > 0) {
            //先将性价比最高的结点加入X，并更新相关变量和集合
            Facility xi = V_Minus_X.get(V_Minus_X.size() - 1);
            X.add(xi);
            wholeCi += xi.getCi();
            wholePi += xi.getPi();
            V_Minus_X.remove(V_Minus_X.size() - 1);  //如果还有下一轮循环，此时S中性价比最高的结点已经去掉了，S中结点也越来越少了
            //继续贪心地添加S中的剩余元素直至发现一个可行解，注意倒序遍历
            for (int i = V_Minus_X.size() - 1; i >= 0; i--) {
                Facility xj = V_Minus_X.get(i);
                if (wholeCi + xj.getCi() <= C) {
                    X.add(xj);
                    wholeCi += xj.getCi();
                    wholePi += xj.getPi();
                }
                if (wholeCi <= C && wholePi >= P) {
                    return X;
                }
            }
            X.clear();
            wholeCi = 0.0;
            wholePi = 0.0;
        }
        return X;
    }

    //1.2-生成一个初始解——C2，在C1的基础上简化得来
    private static LinkedHashSet<Facility> generateSolution_C2() {
        //构建allFacilities的复制版S并按照性价比排序，初始化X
        List<Facility> V_Minus_X = new ArrayList<>(allFacilities);
        LinkedHashSet<Facility> X = new LinkedHashSet<>();
        //初始化变量
        double wholeCi = 0.0, wholePi = 0.0;
        //先从S中任意挑一个加入X，这个第一个元素的不同，决定了下面xk的不同，所以C1的随机性还是很可以的
        int randomIndex = random.nextInt(V_Minus_X.size());
        Facility firstFacility = V_Minus_X.get(randomIndex);
        V_Minus_X.remove(firstFacility);
        X.add(firstFacility);
        wholeCi += firstFacility.getCi();
        wholePi += firstFacility.getPi();
        //==>只要成本没超，就不断从V\X中寻找g(i)=di*(pi^/ci^)值最大的节点加入X，注意di是V\X某个结点到X中结点的最小距离
        while (true) {
            //定义要加的那个结点
            Facility xk = null;
            double maxGreedyValue = Double.MIN_VALUE;  //体现目标函数max
            for (Facility xi : V_Minus_X) {
                //准备计算贪心值的素材1——di
                double minDistance = Double.MAX_VALUE;  //体现目标函数内部的max(min)
                for (Facility xj : X) {
                    if (disMatrix[xi.getId() - 1][xj.getId() - 1] < minDistance) {
                        minDistance = disMatrix[xi.getId() - 1][xj.getId() - 1];
                    }
                }
                //计算贪心值
                if (minDistance > maxGreedyValue) {
                    maxGreedyValue = minDistance;
                    xk = xi;
                }
            }
            //两层for循环结束，这个xk就找到了，若wholeCi加上xk的成本不超，才将其加入X中，否则直接break
            if (wholeCi + xk.getCi() > C) {
                break;
            }
            //若wholeCi加上xk的成本不超，才将其加入X中，这里如果不这样处理，则永远都得不到一个可行解，因为C总是超了才退出循环
            V_Minus_X.remove(xk);
            X.add(xk);
            wholeCi += xk.getCi();
            wholePi += xk.getPi();
        }
        return X;
    }

    //1.1-生成一个初始解——C1
    private static LinkedHashSet<Facility> generateSolution_C1() {
        //构建allFacilities的复制版S并按照性价比排序，初始化X
        List<Facility> V_Minus_X = new ArrayList<>(allFacilities);
        LinkedHashSet<Facility> X = new LinkedHashSet<>();
        //初始化变量
        double wholeCi = 0.0, wholePi = 0.0;
        //先从S中任意挑一个加入X，这个第一个元素的不同，决定了下面xk的不同，所以C1的随机性还是很可以的
        int randomIndex = random.nextInt(V_Minus_X.size());
        Facility firstFacility = V_Minus_X.get(randomIndex);
        V_Minus_X.remove(firstFacility);
        X.add(firstFacility);
        wholeCi += firstFacility.getCi();
        wholePi += firstFacility.getPi();
        //将S按照C1中给定的规则排序——【不！找最大跟排序是两码事】
        double maxCi = V_Minus_X.stream().max(Comparator.comparing(Facility::getCi)).get().getCi();
        double maxPi = V_Minus_X.stream().max(Comparator.comparing(Facility::getPi)).get().getPi();
        //==>只要成本没超，就不断从V\X中寻找g(i)=di*(pi^/ci^)值最大的节点加入X，注意di是V\X某个结点到X中结点的最小距离
        while (true) {
            //定义要加的那个结点
            Facility xk = null;
            double maxGreedyValue = Double.MIN_VALUE;  //体现目标函数max
            for (Facility xi : V_Minus_X) {
                //准备计算贪心值的素材1——di
                double minDistance = Double.MAX_VALUE;  //体现目标函数内部的max(min)
                for (Facility xj : X) {
                    if (disMatrix[xi.getId() - 1][xj.getId() - 1] < minDistance) {
                        minDistance = disMatrix[xi.getId() - 1][xj.getId() - 1];
                    }
                }
                //准备计算贪心值的素材2——pi^和ci^
                double pi_hat = xi.getPi() / maxPi;
                double ci_hat = xi.getCi() / maxCi;
                //计算贪心值
                double curGreedyValue = minDistance * (pi_hat / ci_hat);
                if (curGreedyValue > maxGreedyValue) {
                    maxGreedyValue = curGreedyValue;
                    xk = xi;
                }
            }
            //两层for循环结束，这个xk就找到了，若wholeCi加上xk的成本不超，才将其加入X中，否则直接break
            if (wholeCi + xk.getCi() > C) {
                break;
            }
            //若wholeCi加上xk的成本不超，才将其加入X中
            V_Minus_X.remove(xk);
            X.add(xk);
            wholeCi += xk.getCi();
            wholePi += xk.getPi();
        }
        return X;
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
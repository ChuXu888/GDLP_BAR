package com.chuxu.BackTrackingAlgorithmWithReduction.part07_comparedAlgorithms.PSO.entity;

import java.util.List;

public class Particle implements Comparable<Particle> {
    public List<Integer> X;
    public List<Double> V;
    public List<Integer> Pbest;  //使用包装类型，从而可以设置为null
    public Double PbestFitness;  //Pbest对应的适应值
    public Double curRoundFitness;  //某轮迭代中该粒子的适应值

    public Particle(List<Integer> x, List<Double> v, List<Integer> pbest, Double pbestFitness, Double curRoundFitness) {
        X = x;
        V = v;
        Pbest = pbest;
        PbestFitness = pbestFitness;
        this.curRoundFitness = curRoundFitness;
    }

    @Override
    public String toString() {
        return "Particle{" + "X=" + X + ", V=" + V + ", Pbest=" + Pbest + ", PbestFitness=" + PbestFitness + ", curRoundFitness=" + curRoundFitness + '}';
    }

    @Override
    public int compareTo(Particle o) {
        return Double.compare(this.PbestFitness, o.PbestFitness);
    }
}
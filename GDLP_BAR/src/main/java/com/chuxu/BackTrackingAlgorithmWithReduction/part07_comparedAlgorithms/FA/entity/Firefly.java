package com.chuxu.BackTrackingAlgorithmWithReduction.part07_comparedAlgorithms.FA.entity;

import java.util.List;

public class Firefly implements Comparable<Firefly>{
    private int id;
    private List<Integer> X;
    private Double curFitness;  //某轮迭代中该粒子的适应值

    public Firefly(int id, List<Integer> x, Double curFitness) {
        this.id = id;
        X = x;
        this.curFitness = curFitness;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getX() {
        return X;
    }

    public void setX(List<Integer> x) {
        X = x;
    }

    public Double getCurFitness() {
        return curFitness;
    }

    public void setCurFitness(Double curFitness) {
        this.curFitness = curFitness;
    }

    @Override
    public String toString() {
        return "Firefly{" +
                "id=" + id +
                ", curFitness=" + curFitness +
                ", X=" + X +
                '}';
    }


    @Override
    public int compareTo(Firefly o) {
        return Double.compare(this.curFitness, o.curFitness);
    }
}

package com.chuxu.BackTrackingAlgorithmWithReduction.part07_comparedAlgorithms.GA.entity;


import java.util.List;

public class Individual implements Comparable<Individual> {
    private List<Integer> code;
    private Double fitness;

    public Individual(List<Integer> code, Double fitness) {
        this.code = code;
        this.fitness = fitness;
    }

    public List<Integer> getCode() {
        return code;
    }

    public void setCode(List<Integer> code) {
        this.code = code;
    }

    public Double getFitness() {
        return fitness;
    }

    public void setFitness(Double fitness) {
        this.fitness = fitness;
    }

    @Override
    public String toString() {
        return "Individual{" +
                "code=" + code +
                ", fitness=" + fitness +
                '}';
    }

    @Override
    public int compareTo(Individual o) {
        return Double.compare(this.fitness, o.fitness);
    }
}

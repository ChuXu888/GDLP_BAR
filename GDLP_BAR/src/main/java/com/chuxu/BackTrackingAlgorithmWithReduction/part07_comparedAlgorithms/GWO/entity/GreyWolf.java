package com.chuxu.BackTrackingAlgorithmWithReduction.part07_comparedAlgorithms.GWO.entity;

import java.util.Arrays;
import java.util.Objects;

public class GreyWolf implements Comparable<GreyWolf>,Cloneable{
    private int id;
    private int dimension;
    private Double fitness;
    private double[] position = new double[dimension];

    public GreyWolf() {
    }

    public GreyWolf(int id, int dimension, double[] position) {
        this.id = id;
        this.dimension = dimension;
        this.position = position;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GreyWolf greyWolf = (GreyWolf) o;
        return id == greyWolf.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public Double getFitness() {
        return fitness;
    }

    public void setFitness(Double fitness) {
        this.fitness = fitness;
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "GreyWolf{" +
                "id=" + id +
                ", dimension=" + dimension +
                ", fitness=" + fitness +
                ", position=" + Arrays.toString(position) +
                '}';
    }

    @Override
    public int compareTo(GreyWolf o) {
        return this.fitness.compareTo(o.fitness);
    }

    @Override
    public GreyWolf clone() {
        try {
            // TODO: 复制此处的可变状态，这样此克隆就不能更改初始克隆的内部
            return (GreyWolf) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}


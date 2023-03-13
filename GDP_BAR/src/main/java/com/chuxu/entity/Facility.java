package com.chuxu.entity;

import java.util.Objects;

public class Facility implements Comparable<Facility>,Cloneable{
    private int id;
    private double ci;
    private double pi;
    private double valueDensity;

    public Facility(int id, double ci, double pi, double valueDensity) {
        this.id = id;
        this.ci = ci;
        this.pi = pi;
        this.valueDensity = valueDensity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Facility facility = (Facility) o;
        return id == facility.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Facility{" +
                "id=" + id +
                ", ci=" + ci +
                ", pi=" + pi +
                ", valueDensity=" + valueDensity +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCi() {
        return ci;
    }

    public void setCi(double ci) {
        this.ci = ci;
    }

    public double getPi() {
        return pi;
    }

    public void setPi(double pi) {
        this.pi = pi;
    }

    public double getValueDensity() {
        return valueDensity;
    }

    public void setValueDensity(double valueDensity) {
        this.valueDensity = valueDensity;
    }

    @Override
    public int compareTo(Facility o) {
        return Double.compare(this.valueDensity, o.valueDensity);
    }

    @Override
    public Facility clone() {
        try {
            // TODO: 复制此处的可变状态，这样此克隆就不能更改初始克隆的内部
            return (Facility) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

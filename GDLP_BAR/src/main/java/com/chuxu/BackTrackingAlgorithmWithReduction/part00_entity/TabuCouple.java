package com.chuxu.BackTrackingAlgorithmWithReduction.part00_entity;

import com.chuxu.entity.Facility;

public class TabuCouple {
    private Facility vi;
    private Facility vj;

    public TabuCouple(Facility vi, Facility vj) {
        this.vi = vi;
        this.vj = vj;
    }

    public Facility getVi() {
        return vi;
    }

    public void setVi(Facility vi) {
        this.vi = vi;
    }

    public Facility getVj() {
        return vj;
    }

    public void setVj(Facility vj) {
        this.vj = vj;
    }

    @Override
    public String toString() {
        return "TabuCouple{" +
                "vi=" + vi +
                ", vj=" + vj +
                '}';
    }
}

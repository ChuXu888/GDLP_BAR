package com.chuxu.BackTrackingAlgorithmWithReduction.part00_entity;

import com.chuxu.entity.Facility;

import java.util.LinkedHashSet;

public class CloneObject implements Cloneable{

    LinkedHashSet<Facility> V = new LinkedHashSet<>();
    LinkedHashSet<Facility> V0 = new LinkedHashSet<>();
    LinkedHashSet<Facility> V1 = new LinkedHashSet<>();
    LinkedHashSet<Facility> VV0 = new LinkedHashSet<>();
    LinkedHashSet<Facility> VV1 = new LinkedHashSet<>();

    public CloneObject() {
    }

    public LinkedHashSet<Facility> getV() {
        return V;
    }

    public void setV(LinkedHashSet<Facility> v) {
        V = v;
    }

    public LinkedHashSet<Facility> getV0() {
        return V0;
    }

    public void setV0(LinkedHashSet<Facility> v0) {
        V0 = v0;
    }

    public LinkedHashSet<Facility> getV1() {
        return V1;
    }

    public void setV1(LinkedHashSet<Facility> v1) {
        V1 = v1;
    }

    public LinkedHashSet<Facility> getVV0() {
        return VV0;
    }

    public void setVV0(LinkedHashSet<Facility> VV0) {
        this.VV0 = VV0;
    }

    public LinkedHashSet<Facility> getVV1() {
        return VV1;
    }

    public void setVV1(LinkedHashSet<Facility> VV1) {
        this.VV1 = VV1;
    }

    public CloneObject(LinkedHashSet<Facility> v, LinkedHashSet<Facility> v0, LinkedHashSet<Facility> v1, LinkedHashSet<Facility> VV0, LinkedHashSet<Facility> VV1) {
        V = v;
        V0 = v0;
        V1 = v1;
        this.VV0 = VV0;
        this.VV1 = VV1;
    }

    @Override
    public CloneObject clone() {
        try {
            // TODO: 复制此处的可变状态，这样此克隆就不能更改初始克隆的内部
            return (CloneObject) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public CloneObject(LinkedHashSet<Facility> v, LinkedHashSet<Facility> v0, LinkedHashSet<Facility> v1) {
        V = v;
        V0 = v0;
        V1 = v1;
    }
}

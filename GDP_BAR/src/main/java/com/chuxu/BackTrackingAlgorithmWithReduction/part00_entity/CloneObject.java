package com.chuxu.BackTrackingAlgorithmWithReduction.part00_entity;

import com.chuxu.entity.Facility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CloneObject implements Cloneable{

    LinkedHashSet<Facility> V = new LinkedHashSet<>();
    LinkedHashSet<Facility> V0 = new LinkedHashSet<>();
    LinkedHashSet<Facility> V1 = new LinkedHashSet<>();
    LinkedHashSet<Facility> VV0 = new LinkedHashSet<>();
    LinkedHashSet<Facility> VV1 = new LinkedHashSet<>();

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

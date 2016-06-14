package com.serena.optional.dto;

public class HDD {

    private Integer rotationSpeed;
    private Integer capacity;

    public HDD(Integer rotationSpeed, Integer capacity) {
        this.rotationSpeed = rotationSpeed;
        this.capacity = capacity;
    }

    public static HDD createHDD(Integer rotationSpeed, Integer capacity) {
        return new HDD(rotationSpeed, capacity);
    }

    public Integer getRotationSpeed() {
        return rotationSpeed;
    }

    public Integer getCapacity() {
        return capacity;
    }
}

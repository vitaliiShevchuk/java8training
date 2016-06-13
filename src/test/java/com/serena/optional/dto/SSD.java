package com.serena.optional.dto;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public class SSD {

    private Optional<Integer> capacity;
    private Integer readSpeed;
    private Integer writeSpeed;

    public SSD(Integer capacity, Integer readSpeed, Integer writeSpeed) {
        this.capacity = ofNullable(capacity);
        this.readSpeed = readSpeed;
        this.writeSpeed = writeSpeed;
    }

    public Optional<Integer> getCapacity() {
        return capacity;
    }

    public Integer getReadSpeed() {
        return readSpeed;
    }

    public Integer getWriteSpeed() {
        return writeSpeed;
    }


    public static class SSDBuilder {
        private Integer capacity;
        private Integer readSpeed;
        private Integer writeSpeed;

        public SSDBuilder setCapacity(Integer capacity) {
            this.capacity = capacity;
            return this;
        }

        public SSDBuilder setReadSpeed(Integer readSpeed) {
            this.readSpeed = readSpeed;
            return this;
        }

        public SSDBuilder setWriteSpeed(Integer writeSpeed) {
            this.writeSpeed = writeSpeed;
            return this;
        }

        public SSD createSSD() {
            return new SSD(capacity, readSpeed, writeSpeed);
        }
    }
}

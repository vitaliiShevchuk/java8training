package com.serena.optional.dto;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public class CPU {

    private String manufacturer;
    private Integer cores;
    private Integer clockRate;

    private Optional<String> integratedGraphics;

    public String getManufacturer() {
        return manufacturer;
    }

    public Integer getCores() {
        return cores;
    }

    public Integer getClockRate() {
        return clockRate;
    }

    public Optional<String> getIntegratedGraphics() {
        return integratedGraphics;
    }

    public CPU(String manufacturer, Integer cores, Integer clockRate, String integratedGraphics) {
        this.manufacturer = manufacturer;
        this.cores = cores;
        this.clockRate = clockRate;
        this.integratedGraphics = ofNullable(integratedGraphics);
    }

    public static class CPUBuilder {
        private String manufacturer;
        private Integer cores;
        private Integer clockRate;
        private String integratedGraphics;

        public CPUBuilder setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
            return this;
        }

        public CPUBuilder setCores(Integer cores) {
            this.cores = cores;
            return this;
        }

        public CPUBuilder setClockRate(Integer clockRate) {
            this.clockRate = clockRate;
            return this;
        }

        public CPUBuilder setIntegratedGraphics(String integratedGraphics) {
            this.integratedGraphics = integratedGraphics;
            return this;
        }

        public CPU createCPU() {
            return new CPU(manufacturer, cores, clockRate, integratedGraphics);
        }
    }
}

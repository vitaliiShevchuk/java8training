package com.serena.optional.dto;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

public class Computer {

    private CPU cpu;
    private Optional<HDD> hdd = empty();
    private Optional<SSD> ssd = empty();

    public Computer(CPU cpu, HDD hdd, SSD ssd) {
        this.cpu = cpu;
        this.hdd = ofNullable(hdd);
        this.ssd = ofNullable(ssd);
    }

    public CPU getCpu() {
        return cpu;
    }

    public Optional<HDD> getHdd() {
        return hdd;
    }

    public Optional<SSD> getSsd() {
        return ssd;
    }



    public static class ComputerBuilder {
        private CPU cpu;
        private HDD hdd;
        private SSD ssd;

        public ComputerBuilder setCpu(CPU cpu) {
            this.cpu = cpu;
            return this;
        }

        public ComputerBuilder setHdd(HDD hdd) {
            this.hdd = hdd;
            return this;
        }

        public ComputerBuilder setSsd(SSD ssd) {
            this.ssd = ssd;
            return this;
        }


        public Computer createComputer() {
            return new Computer(cpu, hdd, ssd);
        }
    }
}

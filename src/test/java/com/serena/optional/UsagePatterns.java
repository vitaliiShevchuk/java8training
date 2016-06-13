package com.serena.optional;

import com.serena.optional.dto.CPU;
import com.serena.optional.dto.CPU.CPUBuilder;
import com.serena.optional.dto.Computer;
import com.serena.optional.dto.Computer.ComputerBuilder;
import com.serena.optional.dto.HDD;
import com.serena.optional.dto.SSD.SSDBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class UsagePatterns {

    private static final Logger LOG = LogManager.getLogger(UsagePatterns.class);

    final private Computer computer =
            new ComputerBuilder()
                    .setCpu(new CPUBuilder()
                            .setCores(4)
                            .setClockRate(4000)
                            .setManufacturer("Intel")
                            .setIntegratedGraphics("HD4000")
                            .createCPU())
                    .setSsd(new SSDBuilder()
                            .setCapacity(128)
                            .setReadSpeed(400)
                            .setWriteSpeed(600)
                            .createSSD())
                    .createComputer();

    @Test
    public void ifPresent() {
        Optional<Computer> computer = Optional.of(this.computer);

        //`if` approach
        if (computer.isPresent()) {
            LOG.info("isPresent {}", computer.get());
        }

        //consumer approach
        computer.ifPresent(c -> LOG.info("ifPresent {})", c));
    }

    @Test(expected = RuntimeException.class)
    public void rejectWithFilter() {
        //classical approach to check for nulls and check if value matches some predicate
        if (computer != null && computer.getCpu() != null && computer.getCpu().getClockRate() > 1000) {
            //do something with computer
        }

        if (computer != null)
            if (computer.getCpu() != null)
                if (computer.getCpu().getClockRate() != null) {
                    //do something with clock rate
                }

        //code is starting to look closer to the problem statement and there are no verbose null checks getting in our way
        Optional<Computer> computerOptional = Optional.of(this.computer);
        computerOptional
                .map(Computer::getCpu)
                .map(CPU::getClockRate)
                .filter(rate -> rate > 1000)
                .ifPresent(LOG::info);

        //We want to check for hdd but as there is another Optional we get double Optional
        Optional<Optional<HDD>> hdd = computerOptional
                .map(Computer::getHdd);


        //if any subsequent optional is empty no later actions will be performed
        //all calls except orElseThrow after first flatMap will be ignored as there is no value to be used
        Optional<Integer> optionalCapacity = computerOptional
                .flatMap(Computer::getHdd)
                .map(HDD::getCapacity)
                .filter(capacity -> capacity > 128);

        optionalCapacity
                //throw exception if there is no value
                .orElseThrow(() -> {
                    RuntimeException ex = new RuntimeException("no hdd specified");
                    LOG.info("{} is about to be thrown ", ex);
                    return ex;
                });

        //we may replace empty value with
        assertEquals(42L, optionalCapacity.orElse(42).longValue());
        assertEquals(42L, optionalCapacity.orElseGet(() -> 42).longValue());
    }


}

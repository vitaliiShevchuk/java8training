package com.serena.optional;

import com.serena.optional.dto.CPU;
import com.serena.optional.dto.CPU.CPUBuilder;
import com.serena.optional.dto.Computer;
import com.serena.optional.dto.Computer.ComputerBuilder;
import com.serena.optional.dto.HDD;
import com.serena.optional.dto.SSD;
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

    final private Computer computer2 =
            new ComputerBuilder()
                    .setCpu(new CPUBuilder()
                            .setCores(4)
                            .setClockRate(3300)
                            .setManufacturer("AMD")
                            .setIntegratedGraphics("HD8000")
                            .createCPU())
                    .setSsd(new SSDBuilder()
                            .setCapacity(256)
                            .setReadSpeed(400)
                            .setWriteSpeed(600)
                            .createSSD())
                    .setHdd(new HDD(100, 1024))
                    .createComputer();

    private void problemStatement() {
        computer.getCpu().getClockRate();


        //classical approaches to check for nulls and check if value matches some predicate
        if (computer != null && computer.getCpu() != null && computer.getCpu().getClockRate() > 1000) {
            //do something with computer
        }

        if (computer != null)
            if (computer.getCpu() != null)
                if (computer.getCpu().getClockRate() != null) {
                    //do something with clock rate
                }
    }

    @Test
    public void extracting() {
        Optional<Computer> computerOptional = Optional.of(this.computer);

        computerOptional
                .orElse(new Computer());

        computerOptional
                .orElseGet(Computer::new);

        computerOptional
                .orElseThrow(RuntimeException::new);

        if (computerOptional.isPresent())
            computerOptional.get();

        computerOptional.ifPresent(LOG::info);
    }

    @Test
    public void transformingValues() {
        String manufacturer;
        if (computer != null)
            manufacturer = computer
                    .getCpu()
                    .getManufacturer();

        if (computer != null)
            if (computer.getCpu() != null)
                if (computer.getCpu().getManufacturer() != null) {
                    //do something with clock rate
                }

        manufacturer = Optional.ofNullable(computer)// Optional<Computer>
                .map(Computer::getCpu)// Optional<CPU>
                .map(CPU::getManufacturer)// Optional<String>

                .orElse("No Manufacturer specified");// String

        Optional<Computer> computer = Optional.ofNullable(this.computer);
        Optional<CPU> cpu = computer.map(Computer::getCpu);
        Optional<String> manufacturerOpt = cpu.map(CPU::getManufacturer);

        String s = manufacturerOpt.orElse("No Manufacturer specified");


        assertEquals("Intel", manufacturer);
    }

    @Test
    public void chaining() {

//        Optional<Computer> computerOptional = Optional.of(this.computer);
//        computerOptional
//                .map(Computer::getHdd) // Optional<Optional<HDD>>
//                .map(HDD::getCapacity)
//                .filter(capacity -> capacity > 1024)
//                .ifPresent(LOG::info);


        //Stream<List<Long>> => Stream<Long>
        //stream.flatMap(list -> list.stream())
        Optional<Computer> computerOptional = Optional.of(this.computer);
        computerOptional
                .flatMap(Computer::getHdd)// Optional<HDD>
                .map(HDD::getCapacity)
                .ifPresent(LOG::info);
    }

    @Test
    public void combining() {
//        Optional<Computer> computer = Optional.ofNullable(this.computer);
        Optional<Computer> computer2 = Optional.ofNullable(this.computer2);
        Optional<Computer> computer = Optional.ofNullable(this.computer);

        computer
                .flatMap(Computer::getSsd)
                .flatMap(SSD::getCapacity)
                .flatMap(capacity1 ->
                        computer2
                                .flatMap(Computer::getHdd)
                                .map(HDD::getCapacity)
                                .map(capacity2 -> capacity1 + capacity2)
                )
                .ifPresent(LOG::info);
    }

    @Test(expected = RuntimeException.class)
    public void examples() {

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

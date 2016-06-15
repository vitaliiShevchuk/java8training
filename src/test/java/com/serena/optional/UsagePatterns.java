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
import java.util.function.Function;

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
                            .setClockRate(4000)
                            .setManufacturer("Intel")
                            .setIntegratedGraphics("HD4000")
                            .createCPU())
                    .setSsd(new SSDBuilder()
                            .setCapacity(128)
                            .setReadSpeed(400)
                            .setWriteSpeed(600)
                            .createSSD())
                    .setHdd(new HDD(720, 1024))
                    .createComputer();

    private void classicalApproach() {
        //classical approach to check for nulls and check if value matches some predicate
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
    public void extractingAndTransformingValues() {
        String manufacturer = null;
        if (computer != null)
            manufacturer = computer.getCpu().getManufacturer();

        manufacturer = Optional.ofNullable(computer)// Optional<Computer>
                .map(Computer::getCpu)// <=> Optional<CPU>
                .map(CPU::getManufacturer)// <=> Optional<String>
                .orElse("No Manufacturer specified");// <=> String
//              .orElseThrow(RuntimeException::new);
//              .orElseGet(() -> "No Manufacturer specified");
//              .get()

        assertEquals("Intel", manufacturer);
    }

    @Test
    public void chaining() {

//        Optional<Computer> computerOptional = Optional.of(this.computer);
//        computerOptional
//                .map(Computer::getHdd) // <=> Optional<Optional<HDD>>
//                .map(HDD::getCapacity)
//                .filter(capacity -> capacity > 1024)
//                .ifPresent(LOG::info);

        //Stream<List<Long>> => Stream<Long>
        //stream.flatMap(list -> list.stream())
        Optional<Computer> computerOptional = Optional.of(this.computer);
        computerOptional
                .flatMap(Computer::getHdd)// <=> Optional<HDD>
                .map(HDD::getCapacity)
                .ifPresent(LOG::info);
    }

    @Test
    public void defaultActionsAndUnwrapping() {
        Optional<Computer> computerOptional = Optional.ofNullable(computer);

        computerOptional.get();
        computerOptional.orElse(null);
        computerOptional.orElseGet(() -> null);
        computerOptional.orElseThrow(NullPointerException::new);
        computerOptional.ifPresent(computerPresent -> System.out.println(computerPresent));

        if (computerOptional.isPresent()) {
            LOG.info("isPresent {}", computerOptional.get());
        }
    }

    @Test
    public void combining() {
        Optional<Computer> computer = Optional.ofNullable(this.computer);
        Optional<Computer> computer2 = Optional.ofNullable(this.computer2);

        Optional<Integer> totalCapacity = computer
                .flatMap(Computer::getSsd)
                .flatMap(SSD::getCapacity)
                .flatMap(capacity1 ->
                        computer2
                                .flatMap(Computer::getHdd)
                                .map(HDD::getCapacity)
                                .map(capacity2 -> capacity1 + capacity2)
                );
        LOG.info(totalCapacity);

        Function<Computer, Optional<Integer>> f = (Computer c) -> c.getSsd().flatMap(SSD::getCapacity);
        computer
                .flatMap(f)
                .flatMap(c1 -> computer2.flatMap(f).map(c2 -> c1 + c2));
    }

    @Test
    public void ifPresent() {
        Optional<Computer> computer = Optional.of(this.computer);

        //`if` approach


        //consumer approach
        computer.ifPresent(c -> LOG.info("ifPresent {})", c));
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

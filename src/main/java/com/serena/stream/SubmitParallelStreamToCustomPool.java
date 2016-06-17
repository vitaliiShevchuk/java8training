package com.serena.stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.rolling.action.Duration;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.serena.completable.CompletableFutures.fjp;
import static java.time.Duration.*;

public class SubmitParallelStreamToCustomPool {

    private static final Logger LOG = LogManager.getLogger(SubmitParallelStreamToCustomPool.class);

    @Test
    public void submitToCustomFJP() {
        Random random = new Random();
        List<Integer> randomInts = random.ints(1_000_000_00, 0, 100).boxed().collect(Collectors.toList());

        fjp().submit(() -> {
            randomInts.parallelStream()
                    .collect(Collectors.groupingByConcurrent(Function.identity(), Collectors.counting()));

            Instant before = Instant.now();
            ConcurrentMap<Integer, Long> counted = randomInts.parallelStream()
                    .collect(Collectors.groupingByConcurrent(Function.identity(), Collectors.counting()));
            Instant after = Instant.now();
            LOG.info("completed in {}", between(before, after).getSeconds());

            before = Instant.now();
            randomInts.stream()
                    .collect(Collectors.groupingByConcurrent(Function.identity(), Collectors.counting()));
            after = Instant.now();
            LOG.info("completed in {}", between(before, after).getSeconds());

            counted.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach((e) -> LOG.info("{} : {}", e.getKey(), e.getValue()));
        }).join();
    }

}

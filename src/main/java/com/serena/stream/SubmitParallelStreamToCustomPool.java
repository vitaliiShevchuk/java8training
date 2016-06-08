package com.serena.stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.stream.LongStream;

import static com.serena.CompletableFutures.fjp;

public class SubmitParallelStreamToCustomPool {

    private static final Logger LOG = LogManager.getLogger(SubmitParallelStreamToCustomPool.class);

    public static void submitToCustomFJP() {
        fjp().submit(() -> {
            LongStream.range(0, 500L)
                    .parallel()
                    .filter(l -> l % 100 == 0)
                    .mapToObj(Objects::toString)
                    .forEach(LOG::info);
        }).join();
    }

}

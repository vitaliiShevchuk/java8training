package com.serena.completable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CompletableFutures {
    private static final Logger LOG = LogManager.getLogger(CompletableFutures.class);
    private static final AtomicInteger threadNumber = new AtomicInteger(1);

    private static ExecutorService EXECUTOR_SERVICE;
    private static ForkJoinPool FORK_JOIN_POOL;

    static {
        EXECUTOR_SERVICE = Executors.newFixedThreadPool(4, runnable -> {
            Thread t = new Thread(runnable);
            t.setName("custom-pool-" + threadNumber.getAndAdd(1));
            return t;
        });

        FORK_JOIN_POOL = new ForkJoinPool(3);
    }

    public static final void await(long value, TimeUnit timeUnit) {
        LOG.info("waiting for {} {}", value, timeUnit.toString());
        try {
            Thread.sleep(timeUnit.toMillis(value));
        } catch (InterruptedException e) {
            LOG.error(e);
        }
    }

    public static BigInteger factorial(Long n) {
        LOG.info("computing factorial of {}", n);
        BigInteger result = BigInteger.valueOf(n);
        for (--n; n > 1; n--) {
            result = result.multiply(BigInteger.valueOf(n));
        }

        return result;
    }

    public static ExecutorService es() {
        return EXECUTOR_SERVICE;
    }
    public static ForkJoinPool fjp() {
        return FORK_JOIN_POOL;
    }
}

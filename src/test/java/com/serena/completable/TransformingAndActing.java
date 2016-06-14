package com.serena.completable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;


import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;
import static com.serena.completable.CompletableFutures.*;
import static java.util.concurrent.CompletableFuture.supplyAsync;

public class TransformingAndActing {

    private static final Logger LOG = LogManager.getLogger(TransformingAndActing.class);

    @Test
    public void transformUnderlyingValue() {
        //transformations are neither executed immediately nor blocking
        //they are simply remembered and when original f completes they are executed for you.
        CompletableFuture<BigInteger> f = supplyAsync(() -> factorial(50000L));

        CompletableFuture<String> stringCompletableFuture = f
                //CompletableFuture<BigInteger> => CompletableFuture<BigInteger>
                .thenApply(i -> {
                    LOG.info("divide");
                    return i.divide(BigInteger.TEN);
                })
                .thenApplyAsync(i -> {
                    LOG.info("not");
                    return i.not();
                })
                .thenApplyAsync(i -> {
                    LOG.info("toString");
                    return i.toString();
                }, es());

        stringCompletableFuture.join();
    }

    @Test
    public void actOnCompletion() {
        //same as transformations - this methods do not block

        {
            CompletableFuture<String> f =
                    supplyAsync(() -> factorial(50000L)).thenApply(BigInteger::toString);

            f
                    .thenAccept(s -> LOG.info("at this point we consume value and return CompletableFuture<Void> {}", s))
                    .thenRun(() -> LOG.info("thenAccept consumed value"))
                    .join();
        }

        //same as previous but now we're using custom pool
        {
            CompletableFuture<String> f =
                    supplyAsync(() -> factorial(50000L), es()).thenApplyAsync(BigInteger::toString, es());

            f
                    .thenAcceptAsync(s -> LOG.info("at this point we consume value {}", s), es())
                    .join();
        }
    }

}

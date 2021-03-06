package com.serena.completable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

import static com.serena.completable.CompletableFutures.factorial;
import static java.util.concurrent.CompletableFuture.supplyAsync;

public class CombineEither {

    private static final Logger LOG = LogManager.getLogger(CombineEither.class);

    @Test
    public void useResultFromFirstCompleted() {
        //Another interesting part of the CompletableFuture API is the ability to wait for first (as opposed to all) completed future.
        //This can come handy when you have two tasks yielding result of the same type and you only care about response time,
        //not which task resulted first
        CompletableFuture<String> f1 = supplyAsync(() -> factorial(5000L)).thenApply(BigInteger::toString);
        CompletableFuture<String> f2 = supplyAsync(() -> factorial(50000L)).thenApply(BigInteger::toString);




        //.runAfterEither
        //.applyToEither

        //.acceptEitherAsync
        //.runAfterEitherAsync
        //.applyToEitherAsync
    }

}

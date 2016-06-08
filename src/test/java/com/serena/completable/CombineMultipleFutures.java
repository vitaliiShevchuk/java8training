package com.serena.completable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.serena.CompletableFutures.await;
import static com.serena.CompletableFutures.es;
import static com.serena.CompletableFutures.factorial;
import static java.util.concurrent.CompletableFuture.supplyAsync;

public class CombineMultipleFutures {

    private static final Logger LOG = LogManager.getLogger(CombineMultipleFutures.class);

    private static final CompletableFuture<String> waitAndSupplyString(String value) {
        return supplyAsync(() -> {
            await(1L, TimeUnit.SECONDS);
            return value;
        }, es());
    }

    @Test
    public void combineOrChainMultipleFutures() throws ExecutionException, InterruptedException {
        //thenCompose() is an essential method that allows building robust, asynchronous pipelines
        //without blocking or waiting for intermediate steps. Each subsequently create future depends on previous one
        //if some future in chain fails the ones after will not trigger
        //
        //it is looks like thenApply()
        //but it gets Function<A, CompletableFuture<B>> and returns CompletableFuture<B>
        //while thenApply() returns CompletableFuture<CompletableFuture<B>>

        CompletableFuture<String> exceptional = supplyAsync(() -> {
            RuntimeException ex = new RuntimeException();
            throw ex;
        })
                .thenCompose(lets -> waitAndSupplyString(lets + " chain"))
                .thenCompose(letsChain -> waitAndSupplyString(letsChain + " 3 futures"));


        CompletableFuture<String> chained = waitAndSupplyString("lets")
                .thenCompose(lets -> waitAndSupplyString(lets + " chain"))
                .thenCompose(letsChain -> waitAndSupplyString(letsChain + " 3 futures"));

        CompletableFuture.allOf(exceptional, chained);

        LOG.info("got `{}` after chaining", chained.get());
        LOG.info(exceptional);
    }

    @Test
    public void transformValuesReceivedFrom2Futures() {
        //While thenCompose() is used to chain one future dependent on the other,
        //thenCombine combines two independent futures when they are both done:
        CompletableFuture<BigInteger> f1 = supplyAsync(() -> factorial(50000L), es());
        CompletableFuture<BigInteger> f2 = supplyAsync(() -> factorial(60000L), es());

        f1.thenCombineAsync(f2, (left, right) -> {
            LOG.info("{} / {}", left, right);
            return left.divide(right);
        }, es()).thenAccept(LOG::info).join();

        f1.thenCombineAsync(f2, (left, right) -> {
            LOG.info("{} / {}", left, right);
            return left.divide(right);
        }, es()).thenAccept(LOG::info).join();

    }

}

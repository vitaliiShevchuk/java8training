package com.serena.completable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.serena.completable.CompletableFutures.await;
import static com.serena.completable.CompletableFutures.es;

public class Creating {

    private static final Logger LOG = LogManager.getLogger(Creating.class);
    private static Long get42() {
        await(250, TimeUnit.MILLISECONDS);

        return 42L;
    }


    @Test
    public void testCreateWithSupplier() {
        CompletableFuture<Long> promise = new CompletableFuture<>();
        //currently following statement will block forever
        //promise.get();

        //..and complete it in 2 seconds
        CompletableFuture.runAsync(() -> {
            LOG.info("going to complete promise in 2 seconds");
            await(2, TimeUnit.SECONDS);
            promise.complete(42L);
        }, es());

        //Might be useful for testing or when writing some adapter layer (lifting Long to CompletableFuture<Long>)
        CompletableFuture<Long> completedFuture
                = CompletableFuture
                    .completedFuture(42L);

        //run supplier on commonPool
        CompletableFuture<Long> futureOnCommonPool =
                CompletableFuture.supplyAsync(Creating::get42);

        //run supplier on custom pool
        CompletableFuture<Long> futureOnCustomPool =
                CompletableFuture.supplyAsync(Creating::get42, es());


        //await for all futures to finish
        completedFuture.join();
        futureOnCommonPool.join();
        futureOnCustomPool.join();
        promise.join();
    }

    @Test
    public void testCreateWithRunnable() {
        CompletableFuture<Void> completableFuture =
                CompletableFuture.runAsync(() -> Creating.LOG.info("runAsync on common pool"));
        completableFuture.join();

        CompletableFuture<Void> completableFuture1 =
                CompletableFuture.runAsync(() -> Creating.LOG.info("runAsync on custom pool"), es());
        completableFuture1.join();
    }

}
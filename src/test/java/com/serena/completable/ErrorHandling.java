package com.serena.completable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static java.util.concurrent.CompletableFuture.*;

public class ErrorHandling {

    private static final Logger LOG = LogManager.getLogger(ErrorHandling.class);

    @Test
    public void actingOnSimpleException() {
        CompletableFuture<String> f =
                supplyAsync(throwRunnable());



        CompletableFutures.await(2, TimeUnit.SECONDS);
                //provide a value in case of exception
        CompletableFuture<String> asyncExceptionCatch = f.exceptionally(ex -> "Houston we have a problem");

        LOG.info(f);
        LOG.info(asyncExceptionCatch);
        LOG.info(asyncExceptionCatch.getNow(""));
    }

    private Supplier<String> throwRunnable() {
        return () -> {
            LOG.info("going to throw");
            RuntimeException ex = new RuntimeException();
            throw ex;
        };
    }

    @Test
    public void handleAnyResultOfFuture() {
        CompletableFuture<Object> exceptionalFuture = supplyAsync(() -> {
            RuntimeException ex = new RuntimeException();
            throw ex;
        });

        //handle() is called always, with either result or exception argument being not-null.
        // This is a one-stop catch-all strategy
        CompletableFuture<String> f = exceptionalFuture.handle((Object object, Throwable ex) -> {
            LOG.error("ok = {} ex = {}", object, ex);
            if (object != null) return "shouldn't get here";
            else return "exception caught";
        });

        f.join();
    }

}

package com.serena.completable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.*;

public class ErrorHandling {

    private static final Logger LOG = LogManager.getLogger(ErrorHandling.class);

    @Test
    public void actingOnSimpleException() {
        CompletableFuture<String> f = supplyAsync(() -> {
            RuntimeException ex = new RuntimeException();
            throw ex;
        });
                //provide a value in case of exception
        CompletableFuture<String> asyncExceptionCatch = f.exceptionally(ex -> "Houston we have a problem");

        LOG.info(f);
        LOG.info(asyncExceptionCatch);
        LOG.info(asyncExceptionCatch.getNow(""));
    }

    @Test
    public void handleAnyResultOfFuture() {
        CompletableFuture<Object> exceptionalFuture = supplyAsync(() -> {
            RuntimeException ex = new RuntimeException();
            throw ex;
        });

        //handle() is called always, with either result or exception argument being not-null.
        // This is a one-stop catch-all strategy
        CompletableFuture<String> f = exceptionalFuture.handle((ok, ex) -> {
            LOG.error("ok = {} ex = {}", ok, ex);
            if (ok != null) return "shouldn't get here";
            else return "exception caught";
        });

        f.join();
    }

}

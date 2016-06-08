package com.serena.completable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class ExtractModify {

    private static final Logger LOG = LogManager.getLogger(ExtractModify.class);

    public static final CompletableFuture<String> createFuture() {
        final CompletableFuture<String> f = new CompletableFuture<>();
        return f;
    }

    @Test
    public void weCanCompleteFutureWithSomeExistingValue() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = createFuture();

        LOG.info(future);
        assertEquals("no value there yet",future.getNow("no value there yet"));

        future.complete("qwerty");
        LOG.info(future);

        assertEquals("qwerty", future.get());

        LOG.info(future);
        future.obtrudeValue("override");
        assertEquals("override", future.get());

        CompletableFuture<String> futureExceptional = createFuture();
        futureExceptional.completeExceptionally(new RuntimeException());
        LOG.info(futureExceptional);
    }



}
package com.serena.tryM;

@FunctionalInterface
public interface CheckedRunnable {
    void run() throws Throwable;
}

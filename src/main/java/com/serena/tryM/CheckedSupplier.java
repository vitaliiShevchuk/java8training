package com.serena.tryM;

@FunctionalInterface
public interface CheckedSupplier<R> {
    R get() throws Throwable;
}

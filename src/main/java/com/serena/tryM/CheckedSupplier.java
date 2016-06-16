package com.serena.tryM;

/**
 * Created by HomePC on 16.06.2016.
 */
@FunctionalInterface
public interface CheckedSupplier<R> {
    R get() throws Throwable;
}

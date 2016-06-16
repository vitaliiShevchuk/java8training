package com.serena.tryM;

/**
 * Created by HomePC on 16.06.2016.
 */
@FunctionalInterface
public interface CheckedConsumer<T> {
    void accept(T value) throws Throwable;
}

package com.serena.tryM;

/**
 * Created by HomePC on 16.06.2016.
 */
@FunctionalInterface
public interface CheckedFunction<T, R> {
    R apply(T t) throws Throwable;
}

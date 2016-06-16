package com.serena.tryM;

/**
 * Created by HomePC on 16.06.2016.
 */
@FunctionalInterface
public interface CheckedPredicate<T> {
    boolean test(T t) throws Throwable;
}

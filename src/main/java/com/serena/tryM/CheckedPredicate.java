package com.serena.tryM;

@FunctionalInterface
public interface CheckedPredicate<T> {
    boolean test(T t) throws Throwable;
}

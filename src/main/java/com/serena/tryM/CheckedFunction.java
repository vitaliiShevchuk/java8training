package com.serena.tryM;

@FunctionalInterface
public interface CheckedFunction<T, R> {
    R apply(T t) throws Throwable;
}

package com.serena.tryM;

@FunctionalInterface
public interface CheckedConsumer<T> {
    void accept(T value) throws Throwable;
}

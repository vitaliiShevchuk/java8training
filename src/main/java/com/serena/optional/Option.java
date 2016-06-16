package com.serena.optional;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Option<T> {

    private static final Option<?> EMPTY = new Option<>();
    private final T value;

    public static <T> Option<T> of(T value) {
            Objects.requireNonNull(value);
            return new Option<>(value);
        }
        public static <T> Option<T> ofNullable(T value) {
            if (value == null) {
                return empty();
            }

            return new Option<>(value);
        }
        public static <T> Option<T> empty() {
            @SuppressWarnings("unchecked")
            Option<T> t = (Option<T>) EMPTY;
        return t;
    }

    //empty case
    private Option() {
        this.value = null;
    }

    private Option(T value) {
        this.value = value;
    }

    public <U> Option<U> map(Function<? super T, ? extends U> f) {
        throw new NotImplementedException();
    }

    public <U> Option<U> flatMap(Function<? super T, Option<U>> f) {
        throw new NotImplementedException();
    }

    public T orElse(T t) {
        throw new NotImplementedException();
    }

    public T orElseGet(Supplier<T> supplier) {
        throw new NotImplementedException();
    }

    public Option<T> filter(Predicate<T> predicate) {
        throw new NotImplementedException();
    }

}

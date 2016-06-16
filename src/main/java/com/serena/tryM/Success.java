package com.serena.tryM;

public final class Success<T> implements Try<T> {

    private final T value;

    Success(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public Throwable getCause() {
        throw new UnsupportedOperationException("getCause on Success");
    }

    @Override
    public boolean isFailure() {
        return false;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }
}

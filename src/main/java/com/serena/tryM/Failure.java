package com.serena.tryM;

public final class Failure<T> implements Try<T> {

    private final NonFatalException cause;

    Failure(Throwable exception) {
        cause = NonFatalException.of(exception);
    }

    // Throws NonFatal instead of Throwable because it is a RuntimeException which does not need to be checked.
    @Override
    public T get() throws NonFatalException {
        throw cause;
    }

    @Override
    public Throwable getCause() {
        return cause.getCause();
    }

    @Override
    public boolean isFailure() {
        return true;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

}

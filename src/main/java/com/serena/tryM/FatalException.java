package com.serena.tryM;

public final class FatalException extends RuntimeException {

    FatalException(Throwable exception) {
        super(exception);
    }
}

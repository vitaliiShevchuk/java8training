package com.serena.tryM;

public final class NonFatalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private NonFatalException(Throwable exception) {
        super(exception);
    }

    static NonFatalException of(Throwable exception) {
        if (exception instanceof NonFatalException) {
            return (NonFatalException) exception;
        } else if (exception instanceof FatalException) {
            throw (FatalException) exception;
        } else {
            final boolean isFatal = exception instanceof InterruptedException
                    || exception instanceof LinkageError
                    || exception instanceof ThreadDeath
                    || exception instanceof VirtualMachineError;
            if (isFatal) {
                throw new FatalException(exception);
            } else {
                return new NonFatalException(exception);
            }
        }
    }
}

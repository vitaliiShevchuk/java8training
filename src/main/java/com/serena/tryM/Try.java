package com.serena.tryM;


public interface Try<T> {

    final class Success<T> implements Try<T> {
        private final T value;

        public Success(T t) {
            value = t;
        }
    }

    final class Failure<T> implements Try<T> {

        private final NonFatalException exception;

        public Failure(Throwable throwable) {
            exception = NonFatalException.of(throwable);
        }

    }

    final class FatalException extends RuntimeException {

        private FatalException(Throwable exception) {
            super(exception);
        }
    }

    final class NonFatalException extends RuntimeException {

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



    @FunctionalInterface
    interface CheckedPredicate<T> {
        boolean test(T t);
    }

    //() -> ()
    @FunctionalInterface
    interface CheckedRunnable {
        void run() throws Throwable;
    }

    //T -> ()
    @FunctionalInterface
    interface CheckedConsumer<T> {
        void accept(T t);
    }

    //() -> T
    @FunctionalInterface
    interface CheckedSupplier<T> {
        T get() throws Throwable;
    }

    //T -> R
    @FunctionalInterface
    interface CheckedFunction<T, R> {
        R apply(T t) throws Throwable;
    }




}

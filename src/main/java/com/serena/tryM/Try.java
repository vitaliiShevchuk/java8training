package com.serena.tryM;


import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface Try<T> {


    /*** Helpers to indicate what's `this` ***/

    T get();
    Throwable getCause();
    boolean isFailure();
    boolean isSuccess();


    /*** Creation ***/
    static <T> Try<T> of(CheckedSupplier<? extends T> supplier) {
        try {
            T t = supplier.get();
            return new Success<T>(t);
        } catch (Throwable e) {
            return new Failure<T>(e);

        }
    }

    static Try<Void> run(CheckedRunnable runnable) {
        try {
            runnable.run();
            return new Success<Void>(null);
        } catch (Throwable e) {
            return new Failure<Void>(e);

        }
    }


    static <T> Try<T> success(T value) {
        return new Success<>(value);
    }

    static <T> Try<T> failure(Throwable exception) {
        return new Failure<>(exception);
    }

    /*** Filtering ***/
    default Try<T> filter(Predicate<? super T> predicate) {
        return filterTry(predicate::test);
    }

    default Try<T> filterTry(CheckedPredicate<? super T> predicate) {
        return filterTry(predicate, () -> new NoSuchElementException("Predicate does not hold for " + get()));
    }

    default Try<T> filter(Predicate<? super T> predicate, Supplier<? extends Throwable> throwableSupplier) {
        return filterTry(predicate::test, throwableSupplier);
    }

    default Try<T> filterTry(CheckedPredicate<? super T> predicate, Supplier<? extends Throwable> throwableSupplier) {
        if (isFailure())
            return this;
        else {
            try {
                if (predicate.test(get()))
                    return this;
                else
                    return new Failure<T>(throwableSupplier.get());
            } catch (Throwable e) {
                return new Failure<T>(e);
            }
        }
    }

    /*** combinators ***/
    default <U> Try<U> map(Function<? super T, ? extends U> mapper) {
        return mapTry(mapper::apply);
    }

    default <U> Try<U> mapTry(CheckedFunction<? super T, ? extends U> mapper) {
        if (isFailure())
            return (Try<U>)this;
        else {
            try {
                return success(mapper.apply(get()));
            } catch (Throwable throwable) {
                return failure(throwable);
            }
        }
    }

    //like @map but gets Consumer
    default Try<T> andThen(Consumer<? super T> consumer) {
        return andThenTry(consumer::accept);
    }

    default Try<T> andThenTry(CheckedConsumer<? super T> consumer) {
        if (isFailure()) {
            return this;
        } else {
            try {
                consumer.accept(get());
                return success(null);
            } catch (Throwable e) {
                return failure(e);
            }
        }
    }

    //like @map but gets Runnable
    default Try<T> andThen(Runnable runnable) {
        return andThenTry(runnable::run);
    }

    default Try<T> andThenTry(CheckedRunnable runnable) {
        if (isFailure()) {
            return this;
        } else {
            try {
                runnable.run();
                return success(null);
            } catch (Throwable e) {
                return failure(e);
            }
        }
    }

    default <U> Try<U> flatMap(Function<? super T, ? extends Try<U>> mapper) {
        return flatMapTry(mapper::apply);
    }

    default <U> Try<U> flatMapTry(CheckedFunction<? super T, ? extends Try<U>> mapper) {
        if (isFailure()) {
            return (Try<U>) this;
        } else {
            try {
                Try<U> aTry = (Try<U>) mapper.apply(get());
                return aTry;
            } catch (Throwable throwable) {
                return failure(throwable);
            }
        }
    }

    /*** HOMEWORK ***/

    default Try<T> orElse(Try<? extends T> other) {
        throw new UnsupportedOperationException();
    }

    default Try<T> orElse(Supplier<? extends Try<? extends T>> supplier) {
        throw new UnsupportedOperationException();
    }

    default T getOrElseGet(Function<? super Throwable, ? extends T> other) {
        throw new UnsupportedOperationException();
    }

    default void orElseRun(Consumer<? super Throwable> action) {
        throw new UnsupportedOperationException();
    }

    default <X extends Throwable> T getOrElseThrow(Function<? super Throwable, X> exceptionProvider) throws X {
        throw new UnsupportedOperationException();
    }

    default Try<T> recover(Function<? super Throwable, ? extends T> f) {
        throw new UnsupportedOperationException();
    }

    default Try<T> recoverWith(Function<? super Throwable, ? extends Try<? extends T>> f) {
        throw new UnsupportedOperationException();
    }


}
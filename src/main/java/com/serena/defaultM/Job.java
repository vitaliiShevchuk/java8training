package com.serena.defaultM;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@FunctionalInterface
public interface Job<T> extends Function<Collection<Long>, List<T>>{

//    List<T> calculate(Collection<Long> ids);

    default Future<List<T>> calculateAsync(Collection<Long> ids) {
        return ES.submit(() -> apply(ids));
    }

    default Optional<List<T>> tryCalculate(Collection<Long> ids) {
        return Optional.ofNullable(apply(ids));
    }

    default Optional<List<T>> tryCalculateWithFilter(Collection<Long> ids, Predicate<Long> predicate) {
        return tryCalculate(
                ids
                        .stream()
                        .filter(predicate)
                        .collect(Collectors.toList())
        );
    }

    static void executeAll(Collection<Job<?>> jobs, Collection<Long> ids) {
        CompletableFuture<?>[] futures = jobs.stream()
                .map(job -> CompletableFuture.supplyAsync(() -> job.apply(ids)))
                .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures);
    }

    ExecutorService ES = Executors.newFixedThreadPool(2);
}

package com.serena.tryM;

import com.serena.optional.UsagePatterns;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.verification.checkers.NumberOfInvocationsChecker;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TryTest {

    private static final Logger LOG = LogManager.getLogger(TryTest.class);

    @Mock
    CheckedConsumer<String> failingConsumer;

    @Mock
    CheckedFunction<String, String> failingFunction;

    @Mock
    CheckedFunction<String, Try<String>> failingMFunction;

    @Mock
    CheckedSupplier<String> failingSupplier;

    @Mock
    CheckedRunnable failingRunnable;

    @Mock
    CheckedPredicate<String> failingPredicate;

    @Mock
    CheckedPredicate<String> truePredicate;

    @Mock
    CheckedPredicate<String> falsePredicate;

    Try<String> success = new Success<>("42");
    Try<String> failure = new Failure<>(new RuntimeException());

    @Before
    public void setUp() throws Throwable {
        doThrow(Exception.class).when(failingConsumer).accept(anyString());
        doThrow(Exception.class).when(failingSupplier).get();
        doThrow(Exception.class).when(failingRunnable).run();


        when(truePredicate.test(anyString())).thenReturn(true);
        when(falsePredicate.test(anyString())).thenReturn(false);
        when(failingPredicate.test(anyString())).thenThrow(Exception.class);
        when(failingMFunction.apply(anyString())).thenThrow(Exception.class);
        when(failingFunction.apply(anyString())).thenThrow(Exception.class);
    }

    @Test
    public void mapOverSuccess() throws Throwable {
        Try<String> aTry = success.mapTry(failingFunction);

        verify(failingFunction, times(1)).apply(anyString());

        assertTrue(aTry.isFailure());
    }

    @Test
    public void mapOverFailure() throws Throwable {
        Try<String> aTry = failure.mapTry(failingFunction);

        verifyZeroInteractions(failingFunction);

        assertTrue(aTry.equals(failure));
    }

    @Test
    public void andThenTryOverSuccess() throws Throwable {
        Try<String> aTry = success.andThenTry(failingConsumer);

        Try<String> aTry1 = success.andThenTry(System.out::print);

        verify(failingConsumer, times(1)).accept(anyString());

        assertTrue(aTry1.isSuccess());
        assertTrue(aTry.isFailure());
    }

    @Test
    public void andThenTryOverFailure() throws Throwable {
        Try<String> aTry = failure.andThenTry(failingConsumer);

        verifyZeroInteractions(failingConsumer);

        assertTrue(aTry.equals(failure));
    }

    @Test
    public void andThenTryRunnableOverSuccess() throws Throwable {
        Try<String> aTry = success.andThenTry(failingRunnable);
        Try<String> aTry1 = success.andThenTry(() -> { });

        verify(failingRunnable, times(1)).run();

        assertTrue(aTry1.isSuccess());
        assertTrue(aTry.isFailure());
    }

    @Test
    public void andThenTryRunnableOverFailure() throws Throwable {
        Try<String> aTry = failure.andThenTry(failingRunnable);

        verifyZeroInteractions(failingRunnable);

        assertTrue(aTry.equals(failure));
    }

    @Test
    public void flatMapTryOverSuccess() throws Throwable {
        Try<String> aTry = success.flatMapTry(failingMFunction);

        Try<String> success1 = Try.success("24");
        Try<String> aTry1 = success.flatMapTry(x -> success1);

        verify(failingMFunction, times(1)).apply(anyString());

        assertTrue(aTry.isFailure());
        assertTrue(aTry1.isSuccess());
    }

    @Test
    public void flatMapTryOverFailure() throws Throwable {
        Try<String> aTry = failure.flatMapTry(failingMFunction);

        verifyZeroInteractions(failingMFunction);

        assertTrue(aTry.equals(failure));
    }

    @Test
    public void filterTrySuccess() throws Throwable {
        Try<String> aTry = success.filterTry(truePredicate);
        Try<String> aTry1 = success.filterTry(falsePredicate);
        Try<String> aTry2 = success.filterTry(failingPredicate);

        verify(truePredicate, times(1)).test(anyString());
        verify(falsePredicate, times(1)).test(anyString());
        verify(failingPredicate, times(1)).test(anyString());

        assertTrue(aTry.equals(success));
        assertTrue(aTry1.isFailure());
        assertTrue(aTry2.isFailure());
    }

    @Test
    public void filterTryFailure() throws Throwable {
        Try<String> aTry = failure.filterTry(truePredicate);

        verifyZeroInteractions(truePredicate);

        assertTrue(aTry.equals(failure));
    }

    private String toString(Long n) throws Exception {
        return n.toString();
    }

    @Test
    public void testWithStream() {
        Random random = new Random();
        Optional<Try<String>> first = random.ints(1000).boxed()
                .map(x -> Try.of(() -> String.valueOf(x / 0)))
                .findFirst();



        first.ifPresent(tryA -> {
            if (tryA.isFailure());
                LOG.error(tryA.getCause());
        });
    }

}

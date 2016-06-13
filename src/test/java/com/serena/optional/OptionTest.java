package com.serena.optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OptionTest {

    final Option<Long> validOptional = Option.of(42L);
    final Option<Long> empty = Option.empty();

    @Mock
    Function<Long, String> mapFunctionMock;

    @Mock
    Function<Long, Option<String>> flatMapFunctionMock;

    @Mock
    Predicate<Long> predicateMock;

    @Before
    public void setUp() {
        when(mapFunctionMock.apply(anyLong())).thenReturn("42");
        when(flatMapFunctionMock.apply(anyLong())).thenReturn(Option.of("42"));
    }


    @Test
    public void testValidElse() {
        assertEquals(42L, validOptional.orElse(111L).longValue());
        assertEquals(42L, validOptional.orElseGet(() -> 111L).longValue());
    }

    @Test
    public void testValidFilter() {
        validOptional.filter(predicateMock);
        verify(predicateMock, times(1)).test(anyLong());
    }

    @Test
    public void testValidMap() {
        validOptional.map(mapFunctionMock);
        verify(mapFunctionMock, times(1)).apply(anyLong());
    }

    @Test
    public void testValidFlatMap() {
        validOptional.flatMap(flatMapFunctionMock);
        verify(flatMapFunctionMock, times(1)).apply(anyLong());
    }

    @Test
    public void testEmptyElse() {
        assertEquals(111L, empty.orElse(111L).longValue());
        assertEquals(111L, empty.orElseGet(() -> 111L).longValue());
    }

    @Test
    public void testEmptyFilter() {
        empty.filter(predicateMock);
        verifyZeroInteractions(predicateMock);
    }

    @Test
    public void testEmptyMap() {
        empty.map(mapFunctionMock);
        verifyZeroInteractions(mapFunctionMock);
    }

    @Test
    public void testEmptyFlatMap() {
        empty.flatMap(flatMapFunctionMock);
        verifyZeroInteractions(flatMapFunctionMock);
    }

}

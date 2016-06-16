package com.serena.defaultM;

import java.util.Arrays;
import java.util.Collection;

import static java.util.Arrays.asList;

/**
 * Created by vshevchuk on 6/16/2016.
 */
public class Mian {

    public static void main(String[] args) {

        Job<Collection<Long>> job = (Collection<Long> c ) -> asList(c);

        job
                .tryCalculateWithFilter(asList(1L, 2L, 3L, 4L, 5L), x -> x > 3);

    }

}

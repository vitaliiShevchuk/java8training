package com.serena.optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Creating {

    private static final Logger LOG = LogManager.getLogger(Creating.class);

    @Test
    public void of() {
        //Optional.of is strict
        //if user passes null instead of some value
        //it will throw NullPointerException
        Optional<Long> aLong = Optional.of(42L);

        LOG.info(aLong);

        assertTrue(aLong.isPresent() && aLong.get() == 42L);
    }

    @Test
    public void ofNullable() {
        //Optional.ofNullable is non-strict
        //if user passes null
        //it will return Optional without value inside
        Optional<Long> empty = Optional.ofNullable(null);

        LOG.info(empty);

        assertEquals(empty, Optional.empty());
    }

}

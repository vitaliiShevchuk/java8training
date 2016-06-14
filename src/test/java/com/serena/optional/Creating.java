package com.serena.optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Creating {

    private static final Logger LOG = LogManager.getLogger(Creating.class);




    /**
     *  Optional.of is strict
     *  if user passes null instead of some value
     *  it will throw NullPointerException
     */
    @Test(expected = NullPointerException.class)
    public void of() {

        Optional<Long> aLong = Optional.of(42L);

        LOG.info(aLong);

        assertTrue(aLong.isPresent() && aLong.get() == 42L);

        Optional.of(null);
    }

    @Test
    /**
     *  Optional.ofNullable is non-strict
     *  if user passes null
     *  it will return Optional without value inside
     */
    public void ofNullable() {

        Optional<Long> empty = Optional.ofNullable(null);

        LOG.info(empty);

        assertEquals(empty, Optional.empty());
    }

}

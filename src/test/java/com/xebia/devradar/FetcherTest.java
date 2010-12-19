package com.xebia.devradar;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertFalse;

public class FetcherTest {

    @Test
    public void should_return_no_empty_set() {
        Fetcher fetcher = new Fetcher();
        Set<Event> events = fetcher.fetch();
        assertFalse(events.isEmpty());
    }
}

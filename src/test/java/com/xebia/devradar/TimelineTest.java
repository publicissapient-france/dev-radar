package com.xebia.devradar;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TimelineTest {

    Timeline timeline = new Timeline();

    @Test
    public void events_should_be_empty_when_update_with_an_empty_collection() {
        timeline.update(Collections.<Event>emptyList());
        assertTrue(timeline.events.isEmpty());
    }

    @Test
    public void events_size_should_be_equal_to_max_size_after_update() {
        final List<Event> fetchedEvents = buildEvents(Timeline.MAX_SIZE + 3);
        timeline.update(fetchedEvents);
        assertThat(timeline.events.size(), equalTo(Timeline.MAX_SIZE));
    }

    @Test
    public void events_size_should_be_less_or_equal_to_max_size_after_update() {
        final List<Event> fetchedEvents = buildEvents(Timeline.MAX_SIZE - 3);
        timeline.update(fetchedEvents);
        assertTrue(timeline.events.containsAll(fetchedEvents));
        assertThat(timeline.events.size(), is(lessThanOrEqualTo(Timeline.MAX_SIZE)));
    }

    @Test
    public void events_should_be_ordered_by_date_after_update() {
        List<Event> fetchedEvents = Arrays.asList(
                new Event(3, "joe", "msg", "gravatar"),
                new Event(1, "joe", "msg", "gravatar"),
                new Event(2, "joe", "msg", "gravatar"));

        timeline.update(fetchedEvents);

        assertThat(timeline.events.get(0).timestamp, equalTo(3L));
        assertThat(timeline.events.get(1).timestamp, equalTo(2L));
        assertThat(timeline.events.get(2).timestamp, equalTo(1L));
    }

    static List<Event> buildEvents(int nbFetchedEvents) {
        final List<Event> fetchedEvents = new ArrayList<Event>();
        for (int i = 0; i < nbFetchedEvents; i++) {
            fetchedEvents.add(new Event(System.currentTimeMillis() + i, "joe", "committed something", "url gravatar"));
        }
        return fetchedEvents;
    }
}

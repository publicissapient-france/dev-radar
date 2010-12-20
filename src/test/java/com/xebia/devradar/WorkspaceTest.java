package com.xebia.devradar;

import org.junit.Test;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WorkspaceTest {
    
    Fetcher fetcher = mock(Fetcher.class, RETURNS_SMART_NULLS);
    Workspace workspace = new Workspace(fetcher);

    @Test
    public void events_with_same_timestamp_are_equals_for_a_workspace() {
        long timestamp = System.currentTimeMillis();
        Event event1 = new Event(timestamp, "joe", "msg de joe", "gravatarJoe");
        Event event2 = new Event(timestamp, "Nico", "msg de Nico", "gravatarNico");

        boolean firstAdded = workspace.events.add(event1);
        boolean secondAdded = workspace.events.add(event2);

        assertThat(workspace.events.size(), equalTo(1));
        assertTrue(firstAdded);
        assertFalse(secondAdded);
    }

    @Test
    public void events_should_be_ordered_by_date() {
        Set<Event> fetchedEvents = new HashSet<Event>();
        fetchedEvents.add(new Event(3, "joe", "msg", "gravatar"));
        fetchedEvents.add(new Event(1, "joe", "msg", "gravatar"));
        fetchedEvents.add(new Event(2, "joe", "msg", "gravatar"));

        workspace.events.addAll(fetchedEvents);
        Iterator<Event> eventIterator = workspace.events.iterator();

        assertThat(eventIterator.next().timestamp, equalTo(3L));
        assertThat(eventIterator.next().timestamp, equalTo(2L));
        assertThat(eventIterator.next().timestamp, equalTo(1L));
    }

    @Test
    public void after_polling_events_should_be_empty_when_fetcher_fetch_nothing(){
        when(fetcher.fetch()).thenReturn(Collections.<Event>emptySet());
        workspace.poll();
        assertTrue(workspace.events.isEmpty());
    }

    @Test
    public void after_polling_events_should_contain_fetched_events(){
        Set<Event> fetchedEvents = new HashSet<Event>();
        fetchedEvents.add(new Event(3, "joe", "msg", "gravatar"));
        fetchedEvents.add(new Event(1, "joe", "msg", "gravatar"));
        fetchedEvents.add(new Event(2, "joe", "msg", "gravatar"));
        when(fetcher.fetch()).thenReturn(fetchedEvents);
        workspace.poll();
        assertThat(workspace.events.size(), equalTo(fetchedEvents.size()));
    }

    @Test
    public void getEvents_should_returns_list_which_size_is_egal_to_max_retain_size() {
        final Set<Event> fetchedEvents = new HashSet<Event>();
        for (int i=0; i<Workspace.MAX_RETAIN_SIZE*2; i++) {
            fetchedEvents.add(new Event(System.currentTimeMillis() + i, "joe", "committed smthg", "url gravatar"));
        }

        workspace = new Workspace() {
            @Override
            void poll() {
                this.events.addAll(fetchedEvents);
            }
        };

        List<Event> events = workspace.getEvents();
        assertThat(events.size(), equalTo(Workspace.MAX_RETAIN_SIZE));
    }

    @Test
    public void getEvents_should_returns_list_which_size_is_less_to_max_retain_size() {
        final Set<Event> fetchedEvents = new HashSet<Event>();
        int nbFetchedEvents = Workspace.MAX_RETAIN_SIZE - 3;
        for (int i=0; i< nbFetchedEvents; i++) {
            fetchedEvents.add(new Event(new Date().getTime() + i, "joe", "committed smthg", "url gravatar"));
        }

        workspace = new Workspace() {
            @Override
            void poll() {
                this.events.addAll(fetchedEvents);
            }
        };

        List<Event> events = workspace.getEvents();
        assertThat(events.size(), equalTo(nbFetchedEvents));
    }
}

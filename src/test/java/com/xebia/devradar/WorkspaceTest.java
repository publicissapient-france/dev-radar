package com.xebia.devradar;

import org.junit.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WorkspaceTest {

    // About the use of Mockito.RETURNS_SMART_NULLS, see here : http://docs.mockito.googlecode.com/hg/org/mockito/Mockito.html#RETURNS_SMART_NULLS
    Fetcher fetcher = mock(Fetcher.class, RETURNS_SMART_NULLS);
    Timeline timeline = mock(Timeline.class, RETURNS_SMART_NULLS);
    Workspace workspace = new Workspace(fetcher, timeline);

    @Test
    public void events_should_contain_fetched_events_and_timeline_is_updated_after_polling() {

        List<Event> previousEvents = TimelineTest.buildEvents(5);
        // initialise events of the workspace
        workspace.events.addAll(previousEvents);

        List<Event> fetchedEvents = TimelineTest.buildEvents(3);
        when(fetcher.fetch()).thenReturn(new HashSet<Event>(fetchedEvents));

        workspace.poll();

        // assert all fetched events are stored
        assertTrue(workspace.events.containsAll(fetchedEvents));
        // assert previously stored events are not lost
        assertTrue(workspace.events.containsAll(previousEvents));
        verify(timeline).update(workspace.events);
    }
}

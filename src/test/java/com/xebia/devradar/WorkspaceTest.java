/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.xebia.devradar;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class WorkspaceTest {

    // About the use of Mockito.RETURNS_SMART_NULLS, see here : http://docs.mockito.googlecode.com/hg/org/mockito/Mockito.html#RETURNS_SMART_NULLS
    HudsonFetcher hudsonFetcher = mock(HudsonFetcher.class, RETURNS_SMART_NULLS);
    GitHubFetcher gitHubFetcher = mock(GitHubFetcher.class, RETURNS_SMART_NULLS);
    Timeline timeline = mock(Timeline.class, RETURNS_SMART_NULLS);
    Workspace workspace = new Workspace(Arrays.asList(hudsonFetcher, gitHubFetcher), timeline);

    @Test
    public void events_should_contain_fetched_events_and_timeline_is_updated_after_polling() {

        List<Event> previousEvents = TimelineTest.buildEvents(5);
        // initialise events of the workspace
        workspace.events.addAll(previousEvents);

        List<Event> fetchedEvents = TimelineTest.buildEvents(3);
        when(gitHubFetcher.fetch()).thenReturn(new HashSet<Event>(fetchedEvents));

        workspace.poll();

        // assert all fetched events are stored
        assertTrue(workspace.events.containsAll(fetchedEvents));
        // assert previously stored events are not lost
        assertTrue(workspace.events.containsAll(previousEvents));
        verify(timeline).update(workspace.events);
    }

    @Test
    public void workspace_should_poll_events_from_all_its_fetchers() {

        List<Event> previousEvents = TimelineTest.buildEvents(5);
        // initialise events of the workspace
        workspace.events.addAll(previousEvents);

        List<Event> gitHubFetchedEvents = TimelineTest.buildEvents(3);
        when(gitHubFetcher.fetch()).thenReturn(new HashSet<Event>(gitHubFetchedEvents));
        List<Event> hudsonFetchedEvents = TimelineTest.buildEvents(2);
        when(hudsonFetcher.fetch()).thenReturn(new HashSet<Event>(hudsonFetchedEvents));

        workspace.poll();

        // assert all fetched events are stored
        assertTrue(workspace.events.containsAll(gitHubFetchedEvents));
        assertTrue(workspace.events.containsAll(hudsonFetchedEvents));
        // assert previously stored events are not lost
        assertTrue(workspace.events.containsAll(previousEvents));
        verify(gitHubFetcher, times(1)).fetch();
        verify(hudsonFetcher, times(1)).fetch();
        verify(timeline).update(workspace.events);

    }
}

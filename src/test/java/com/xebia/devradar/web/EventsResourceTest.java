/**
 * Copyright (C) 2010 xebia-france <xebia-france@xebia.fr>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebia.devradar.web;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import com.xebia.devradar.domain.Event;

public class EventsResourceTest
{
    private EventRepository eventRepository;

    private EventsResource eventsResource;

    @Before
    public void init()
    {
        eventRepository = mock(EventRepository.class);
        eventsResource = new EventsResource(eventRepository);
    }

    @Test
    public void should_return_all_events()
    {
        when(eventRepository.getEventsForWorkspace(anyString())).thenReturn(someEventList());
        ModelAndView mav = eventsResource.getAllEvents("workspace");
        assertThat(mav.getViewName(), equalTo("workspace"));
        assertThat((String) mav.getModel().get("workspace"), equalTo("workspace"));
        @SuppressWarnings("unchecked")
        Set<Event> events = (Set<Event>) mav.getModel().get("events");
        assertThat(events, notNullValue());
        assertThat(events.size(), is(2));
    }

    private Set<Event> someEventList()
    {
        return new LinkedHashSet<Event>(
            Arrays.asList(
                new Event("svn", "Fix bug FOOBAR-42", new Date()), 
                new Event("svn", "Introducing Google Guava", new Date())));
    }
}

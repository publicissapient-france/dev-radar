package com.xebia.devradar;

import org.apache.commons.lang.builder.CompareToBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Timeline {
    static final int MAX_SIZE = 10;

    List<Event> events = new ArrayList<Event>();

    public List<Event> getEvents() {
        return events;
    }

    public void update(Collection<Event> workspaceEvents) {

        List<Event> workspaceSortedEvents = new ArrayList<Event>(workspaceEvents);
        Collections.sort(workspaceSortedEvents, Collections.reverseOrder(new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return new CompareToBuilder().append(o1.timestamp, o2.timestamp).toComparison();
            }
        }));

        if (workspaceSortedEvents.size() < MAX_SIZE) {
            this.events = workspaceSortedEvents;
        } else {
            this.events = workspaceSortedEvents.subList(0, MAX_SIZE);
        }
    }
}

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

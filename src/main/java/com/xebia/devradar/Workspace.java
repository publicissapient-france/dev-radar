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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.builder.CompareToBuilder;

/*
 * make fields visible for tests.
 */
public class Workspace {
    static final int MAX_RETAIN_SIZE = 10;

    /**
     * Set d'événements trié par date décroissante.
     */
    SortedSet<Event> events = new TreeSet<Event>(Collections.reverseOrder(new Comparator<Event>() {
        @Override
        public int compare(Event o1, Event o2) {
            return new CompareToBuilder().append(o1.timestamp, o2.timestamp).toComparison();
        }
    }));

    Fetcher fetcher;

    public Workspace() {
        this(new Fetcher());
    }

    public Workspace(Fetcher fetcher) {
        this.fetcher = fetcher;
    }

    void poll() {
        Set<Event> fetchedEvents = this.fetcher.fetch();
        this.events.addAll(fetchedEvents);
    }

    public List<Event> getEvents() {
        poll();

        if (this.events.size() < MAX_RETAIN_SIZE) {
            return new ArrayList<Event>(this.events);
        }
        return new ArrayList<Event>(this.events).subList(0, MAX_RETAIN_SIZE);
    }
}

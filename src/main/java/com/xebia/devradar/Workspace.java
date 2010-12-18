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

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class Workspace {

    Set<Event> events = new TreeSet<Event>(new Comparator<Event>() {
        @Override
        public int compare(Event event, Event event1) {
            return event1.date.compareTo(event.date);
        }
    });

    public Workspace() {
        this.events.add(new Event("01/01/2010", "nicolas", "nicolas a committé qqqch", "http://www.gravatar.com/avatar/4a89258a4759e47dab3266e9b9d76065.png"));
        this.events.add(new Event("02/01/2010", "cyrille", "cyille a committé qqqch", "http://www.gravatar.com/avatar/fd83e4fbdb11f925603ef60d25efcbb4"));
        this.events.add(new Event("03/01/2010", "jean-laurent", "jean-laurent a committé qqqch", "http://www.gravatar.com/avatar/649d3668d3ba68e75a3441dec9eac26e"));
        this.events.add(new Event("04/01/2010", "simon", "simon a committé qqqch", "http://www.gravatar.com/avatar/740b1444a71181776c42130408a4b848"));
        this.events.add(new Event("05/01/2010", "alexandre", "alexandre a committé qqqch", "http://www.gravatar.com/avatar/e96398d35fcd2cb3df072bcb28c9c917"));
        this.events.add(new Event("06/01/2010", "nicolas", "nicolas a committé qqqch", "http://www.gravatar.com/avatar/4a89258a4759e47dab3266e9b9d76065.png"));
        this.events.add(new Event("07/01/2010", "cyrille", "cyrille a committé qqqch", "http://www.gravatar.com/avatar/fd83e4fbdb11f925603ef60d25efcbb4"));
        this.events.add(new Event("08/01/2010", "jean-laurent", "jean-laurent a committé qqqch", "http://www.gravatar.com/avatar/649d3668d3ba68e75a3441dec9eac26e"));
        this.events.add(new Event("09/01/2010", "simon", "simon a committé qqqch", "http://www.gravatar.com/avatar/740b1444a71181776c42130408a4b848"));
        this.events.add(new Event("10/01/2010", "alexandre", "alexandre a committé qqqch", "http://www.gravatar.com/avatar/e96398d35fcd2cb3df072bcb28c9c917"));
    }

    public Set<Event> getEvents() {
        return events;
    }
}

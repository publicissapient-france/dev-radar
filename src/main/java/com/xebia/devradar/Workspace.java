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
import java.util.List;
import java.util.Set;

/*
 * make fields visible for tests.
 */
public class Workspace {

    String name;
    List<Event> events = new ArrayList<Event>();
    Timeline timeline;
    //GitHubFetcher fetcher;
    HudsonFetcher fetcher;

    public Workspace() {
        this(new HudsonFetcher(), new Timeline());
    }


    public Workspace(HudsonFetcher fetcher, Timeline timeline) {
        this.fetcher = fetcher;
        this.timeline = timeline;
        this.name = "Dev Radar";
    }

    void poll() {
        //Set<Event> fetchedEvents = this.fetcher.fetch("http://github.com/api/v2/json/commits/list/xebia-france/dev-radar/master");
        Set<Event> fetchedEvents = this.fetcher.fetch("http://fluxx.fr.cr:8080/hudson/job/dev-radar/api/json?tree=builds[result,culprits[fullName,absoluteUrl],timestamp]");
        this.events.addAll(fetchedEvents);
        this.timeline.update(this.events);
    }

    public String getName() {
        return name;
    }

    public Timeline getTimeline() {
        poll();
        return timeline;
    }
}

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

import java.util.*;

/*
 * make fields visible for tests.
 */
public class Workspace {

    String name;
    List<Event> events = new ArrayList<Event>();
    Timeline timeline;
    Collection<Pollable> fetchers;

    public Workspace() {
        this(Arrays.asList(new HudsonFetcher("http://fluxx.fr.cr:8080/hudson", "dev-radar"), new GitHubFetcher("http://github.com/api/v2/json/commits/list/xebia-france/dev-radar/master")), new Timeline());
    }


    public Workspace(Collection<Pollable> fetchers, Timeline timeline) {
        this.fetchers = fetchers;
        this.timeline = timeline;
        this.name = "Dev Radar";
    }

    void poll() {
        for (Pollable fetcher : fetchers) {
            this.events.addAll(fetcher.fetch());
        }
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

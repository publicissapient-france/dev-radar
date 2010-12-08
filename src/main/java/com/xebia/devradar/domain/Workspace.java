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
package com.xebia.devradar.domain;

import com.xebia.devradar.pollers.PollException;

import javax.persistence.*;
import java.util.*;


@Entity
@Access(AccessType.FIELD)
@NamedQueries({
        @NamedQuery(name="workspaceByName", query="select w from Workspace w where w.name = :name"),
        @NamedQuery(name="orderByName", query="from Workspace w order by w.name")
})
public class Workspace extends AbstractEntity {

    @Basic(optional = false)
    @Column(length = 50, unique=true)
    private String name;

    @Column(length = 256)
    private String pomUrl;

    @Column(length = 512)
    private String description;

    @OneToMany(mappedBy = "workspace", cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private Set<Event> events = new LinkedHashSet<Event>();

    @OneToMany(cascade=CascadeType.ALL,orphanRemoval=true)
    private Set<EventSource> eventSources = new LinkedHashSet<EventSource>();

    @OneToMany(mappedBy = "workspace", cascade=CascadeType.ALL,orphanRemoval=true)
    private Set<Badge> badges = new LinkedHashSet<Badge>();

    public Workspace() {
    }

    public Workspace(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Set<Event> getEvents() {
        return Collections.unmodifiableSet(events);
    }

    public void addEvent(final Event e) {
        e.setWorkspace(this);
        internalAddEvent(e);
    }

    void internalAddEvent(final Event e) {
        this.events.add(e);
    }

    public Set<EventSource> getEventSources() {
        return this.eventSources;
    }

    public void setEventSources(final Set<EventSource> eventSources) {
        this.eventSources = eventSources;
    }

    public void addEventSource(final EventSource e) {
        this.eventSources.add(e);
    }

    public Set<Badge> getBadges() {
        return Collections.unmodifiableSet(badges);
    }

    public void addBadge(final Badge b) {
        b.setWorkspace(this);
        internalAddBadge(b);
    }

    void internalAddBadge(final Badge b) {
        this.badges.add(b);
    }

    public String getPomUrl() {
        return this.pomUrl;
    }

    public void setPomUrl(final String pomUrl) {
        this.pomUrl = pomUrl;
    }


    public String getDescription() {
        return this.description;
    }


    public void setDescription(final String description) {
        this.description = description;
    }

    public List<Event> poll(final Date startDate, final Date endDate) throws PollException {
        final List<Event> events = new ArrayList<Event>();
        for (final EventSource source : this.eventSources) {
            events.addAll(source.poll(startDate, endDate));
        }
        return events;
    }
}

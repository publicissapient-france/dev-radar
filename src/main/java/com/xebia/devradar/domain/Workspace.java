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

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.xebia.devradar.pollers.PollException;


@Entity
@Access(AccessType.FIELD)
@NamedQueries({
    @NamedQuery(name="workspaceByName", query="from Workspace where name = :name"),
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

    @OneToMany(cascade=CascadeType.ALL,orphanRemoval=true)
    private Set<Event> events = new LinkedHashSet<Event>();

    @OneToMany(cascade=CascadeType.ALL,orphanRemoval=true)
    private Set<EventSource> eventSources = new LinkedHashSet<EventSource>();

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
        return this.events;
    }

    public void setEvents(final Set<Event> events) {
        this.events = events;
    }

    public void addEvent(final Event e) {
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

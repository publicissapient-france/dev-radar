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

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@Access(AccessType.FIELD)
@NamedQueries({
@NamedQuery(name="workspaceByName", query="from Workspace where name = :name"),
@NamedQuery(name="orderByName", query="from Workspace w order by w.name")
})
public class Workspace extends AbstractEntity {

    @Basic(optional = false)
    @Column(length = 50)
    private String name;

    @Column(length = 256)
    private String pomUrl;
    
    @Column(length = 512)
    private String description;
    
    @Column(length = 256)
    private String issueManagement;
    
    @Column(length = 256)
    private String ciManagement;
    
    @Column(length = 256)
    private String scm;
    
    @OneToMany(cascade=CascadeType.ALL,orphanRemoval=true)
    private Set<Event> events = new LinkedHashSet<Event>();
    
    @ElementCollection
    private Set<EventSource> eventSources = new LinkedHashSet<EventSource>();
    
    public Workspace() {
    }
    
    public Workspace(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Set<Event> getEvents() {
        return events;
    }
    
    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public void addEvent(Event e) {
        events.add(e);
    }
    
    public Set<EventSource> getEventSources() {
        return eventSources;
    }
    
    public void setEventSources(Set<EventSource> eventSources) {
        this.eventSources = eventSources;
    }

    public void addEventSource(EventSource e) {
        eventSources.add(e);
    }

    public String getPomUrl() {
        return pomUrl;
    }

    public void setPomUrl(String pomUrl) {
        this.pomUrl = pomUrl;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getIssueManagement() {
        return issueManagement;
    }


    public void setIssueManagement(String issueManagement) {
        this.issueManagement = issueManagement;
    }


    public String getCiManagement() {
        return ciManagement;
    }


    public void setCiManagement(String ciManagement) {
        this.ciManagement = ciManagement;
    }


    public String getScm() {
        return scm;
    }


    public void setScm(String scm) {
        this.scm = scm;
    }

}

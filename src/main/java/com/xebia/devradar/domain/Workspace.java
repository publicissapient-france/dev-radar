/**
 * Copyright (C) 2010 Xebia IT Architects <xebia@xebia.fr>
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

package com.xebia.devradar.domain;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@Access(AccessType.FIELD)
@NamedQuery(name="workspaceByName", query="from Workspace where name = :name")
public class Workspace extends AbstractEntity {

    @Basic(optional = false)
    @Column(length = 50, unique=true)
    private String name;

    @OneToMany(cascade=CascadeType.ALL,orphanRemoval=true)
    private Set<Event> events = new LinkedHashSet<Event>();
    
    public Workspace() {
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


}

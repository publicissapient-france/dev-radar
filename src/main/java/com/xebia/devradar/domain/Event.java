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

import com.xebia.devradar.EventType;

import java.util.Date;

import javax.persistence.*;


@Entity
@Access(AccessType.FIELD)
public class Event extends AbstractEntity {

    @Basic(optional = false)
    @Column(length = 500)
    private String message;

    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @ManyToOne(optional = false)
    private EventSource source;

    @ManyToOne(optional = false)
    private Workspace workspace;

    // Persist is temprary until part Profil <-> Event is implemented
    @ManyToOne(optional = true, cascade = CascadeType.PERSIST)
    private Profil profil;

    public Event() {
    }

    public Event(final EventSource source, final String message, final Date date, EventType eventType, Profil profil) {
        this.source = source;
        this.message = message;
        this.date = date;
        this.eventType = eventType;
        this.profil = profil;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public EventSource getSource() {
        return this.source;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public void setSource(final EventSource source) {
        this.source = source;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
        workspace.internalAddEvent(this);
    }

    public Profil getProfil() {
        return profil;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    @Override
    public String toString() {
        return "Event [date=" + this.date + ", message=" + this.message + ", source=" + this.source + "]";
    }
}

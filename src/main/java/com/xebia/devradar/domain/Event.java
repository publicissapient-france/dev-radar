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
import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;


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

    @Basic(optional = true)
    @Column(length = 500)
    private String gravatarUrl;

    public Event() {
    }

    public Event(final EventSource source, final String message, final Date date, final EventType eventType, final String author) {
        this.source = source;
        this.setMessage(message);
        this.date = date;
        this.eventType = eventType;
        this.gravatarUrl = mappingsGravatarUrl.get(author);
    }

    public String getMessage() {
        return this.message;
    }

    private void setMessage(final String message) {
        this.message = StringUtils.abbreviate(message, 500);
    }

    public Date getDate() {
        return this.date;
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

    public String getGravatarUrl() {
        return this.gravatarUrl;

    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
        workspace.internalAddEvent(this);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("date", this.date) //
                .append("message", StringUtils.abbreviate(this.message, 10)) //
                .append("gravatarUrl", this.gravatarUrl) //
                .append("source", this.source) //
                .toString();
    }

    private static final Map<String, String> mappingsGravatarUrl = new HashMap<String, String>(){{
        put("Cyrille Le Clerc", "http://www.gravatar.com/avatar/fd83e4fbdb11f925603ef60d25efcbb4");
        put("Jean-Laurent de Morlhon", "http://www.gravatar.com/avatar/649d3668d3ba68e75a3441dec9eac26e");
        put("mrenou", "http://www.gravatar.com/avatar/8c92fcdb7c7abc1a50732a93bc361b5e.png");
        put("simcap", "http://www.gravatar.com/avatar/740b1444a71181776c42130408a4b848");
        put("adutra", "http://www.gravatar.com/avatar/e96398d35fcd2cb3df072bcb28c9c917");
        put("pvardanega", "http://www.gravatar.com/avatar/fd83e4fbdb11f925603ef60d25efcbb4");
        put("jsmadja", "http://www.gravatar.com/avatar/fd83e4fbdb11f925603ef60d25efcbb4");
        put("Nicolas Griso", "http://www.gravatar.com/avatar/4a89258a4759e47dab3266e9b9d76065.png");
        put("Benoit Guerout", "http://www.gravatar.com/avatar/fd83e4fbdb11f925603ef60d25efcbb4");
    }};

}

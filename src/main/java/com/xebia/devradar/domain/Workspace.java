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

import javax.persistence.*;

import com.xebia.devradar.domain.dao.BadgeTypeRepository;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.xebia.devradar.pollers.PollException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.*;


@Configurable
@Entity
@Access(AccessType.FIELD)
@NamedQueries({
        @NamedQuery(name="workspaceByName", query="select w from Workspace w where w.name = :name"),
        @NamedQuery(name="orderByName", query="from Workspace w order by w.name")
})
public class Workspace extends AbstractEntity {

    @Transient
    @PersistenceContext
    private EntityManager entityManager;

    @Transient
    @Autowired
    private BadgeTypeRepository badgeTypeRepository;

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

    public void refreshBadges() {
        for (Badge badge : badges) {
            badge.refreshBadgeOwner();
        }
    }

    public void updateBadges(Long[] badgeTypeIds) {
        badges.clear();
        entityManager.flush();

        for (Long badgeTypeId : badgeTypeIds) {
            BadgeType badgeType = badgeTypeRepository.getBadgeTypeById(badgeTypeId);


            Badge badge = new Badge();
            badge.setBadgeType(badgeType);
            badge.setWorkspace(this);
            addBadge(badge);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("id", this.getId())
                .append("name", this.name) //
                .toString();
    }
}

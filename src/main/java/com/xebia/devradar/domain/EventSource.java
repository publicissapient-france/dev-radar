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

import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

import com.xebia.devradar.pollers.PollException;
import com.xebia.devradar.pollers.Poller;
import com.xebia.devradar.pollers.PollerDescriptor;
import com.xebia.devradar.pollers.PollerServiceLocator;

/**
 * Describes an Event Source for a {@link Workspace}.
 * @author Alexandre Dutra
 *
 */
@Entity
@Access(AccessType.FIELD)
public class EventSource extends AbstractEntity {

    @Basic(optional = false)
    @Column(length = 100)
    private Class<? extends Poller> pollerClass;

    @Basic(optional = false)
    @Column(length = 500)
    private String description;

    @Basic(optional = false)
    @Column(length = 4096)
    private URL url;

    @Basic(optional = true)
    @Column(length = 4096)
    private URL proxyUrl;

    //TODO proxy settings, authentication, etc.

    public EventSource() {
    }

    public EventSource(final URL url) {
        super();
        this.url = url;
    }

    public EventSource(final Class<? extends Poller> pollerClass, final String description, final URL url) {
        super();
        this.pollerClass = pollerClass;
        this.description = description;
        this.url = url;
    }

    public URL getUrl() {
        return this.url;
    }

    public void setUrl(final URL url) {
        this.url = url;
    }


    public Class<? extends Poller> getPollerClass() {
        return this.pollerClass;
    }


    public void setPollerClass(final Class<? extends Poller> pollerClass) {
        this.pollerClass = pollerClass;
    }


    public String getDescription() {
        return this.description;
    }


    public void setDescription(final String description) {
        this.description = description;
    }


    public URL getProxyUrl() {
        return this.proxyUrl;
    }


    public void setProxyUrl(final URL proxyUrl) {
        this.proxyUrl = proxyUrl;
    }

    public List<Event> poll(final Date startDate, final Date endDate) throws PollException{
        return this.getPollerDescriptor().createPoller(this).poll(startDate, endDate);
    }

    public PollerDescriptor getPollerDescriptor() {
        return PollerServiceLocator.getPollerDescriptor(this.getPollerClass());
    }

}

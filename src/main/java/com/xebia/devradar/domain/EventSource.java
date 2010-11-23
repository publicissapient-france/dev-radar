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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.xebia.devradar.pollers.PollException;
import com.xebia.devradar.pollers.Poller;

/**
 * Describes an Event Source for a <code>{@link Workspace}</code>.
 * An Event Source object basically encapsulates:
 * <ol>
 * <li>A <code>{@link PollerDescriptor}</code> object describing which <code>{@link Poller}</code>
 * should be used to poll the source;<li>
 * <li>Any network connection information necessary to interrogate the source,
 * such as the source URL, proxy settings, authentication, etc.</li>
 * </ol>
 * 
 * @author Alexandre Dutra
 *
 */
@Entity
@Access(AccessType.FIELD)
public class EventSource extends AbstractEntity {

    @ManyToOne(optional=false)
    private PollerDescriptor pollerDescriptor;

    @Basic(optional = false)
    @Column(length = 4096)
    private URL url;

    @Basic(optional = false)
    @Column(length = 500)
    private String description;

    @Basic(optional = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPollDate;

    /**
     * The user name and password, if the Event Source
     * needs HTTP authentication.
     */
    @Embedded
    private Authentication authentication;

    /**
     * Optioanl HTTP proxy.
     */
    @Embedded
    private Proxy proxy;

    /**
     * Event Source parameters. Parameters can be used
     * to supply additional information to <code>{@link Poller}</code>s,
     * e.g. a JIRA key, a Sonar project ID, etc.
     */
    @ElementCollection
    private Map<String, String> parameters = new HashMap<String, String>();

    public EventSource() {
    }

    public EventSource(
            final PollerDescriptor pollerDescriptor,
            final URL url,
            final String description) {
        super();
        this.pollerDescriptor = pollerDescriptor;
        this.url = url;
        this.description = description;
    }


    public PollerDescriptor getPollerDescriptor() {
        return this.pollerDescriptor;
    }

    public void setPollerDescriptor(final PollerDescriptor pollerDescriptor) {
        this.pollerDescriptor = pollerDescriptor;
    }

    public Date getLastPollDate() {
        return this.lastPollDate;
    }

    public void setLastPollDate(final Date lastPollDate) {
        this.lastPollDate = lastPollDate;
    }

    public URL getUrl() {
        return this.url;
    }

    public void setUrl(final URL url) {
        this.url = url;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Authentication getAuthentication() {
        return this.authentication;
    }

    public void setAuthentication(final Authentication authentication) {
        this.authentication = authentication;
    }

    public Proxy getProxy() {
        return this.proxy;
    }

    public void setProxy(final Proxy proxy) {
        this.proxy = proxy;
    }

    public Map<String, String> getParameters() {
        return this.parameters;
    }

    public void setParameters(final Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getParameter(final String key) {
        return this.parameters.get(key);
    }

    public void addParameter(final String key, final String value) {
        this.parameters.put(key, value);
    }

    public List<Event> poll(final Date startDate, final Date endDate) throws PollException{
        return this.getPollerDescriptor().createPoller().poll(this, startDate, endDate);
    }

}

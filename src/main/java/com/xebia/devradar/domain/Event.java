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

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Access(AccessType.FIELD)
public class Event extends AbstractEntity {

    @Basic(optional = false)
    @Column(length = 500)
    private String message;

    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne(optional=true)
    private EventSource source;

    public Event() {
    }

    public Event(final EventSource source, final String message, final Date date) {
        this.source = source;
        this.message = message;
        this.date = date;
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

    public void setSource(final EventSource source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "Event [date=" + this.date + ", message=" + this.message + ", source=" + this.source + "]";
    }



}

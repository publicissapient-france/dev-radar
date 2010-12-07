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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.xebia.devradar.pollers.Poller;


/**
 * An object describing how to poll an <code>{@link EventSource}</code>.
 * A <code>{@link PollerDescriptor}</code> encapsulates all the information necessary
 * to instantiate an appropriate <code>{@link Poller}</code>.
 * 
 * @author Alexandre Dutra
 *
 */
@Entity
public class PollerDescriptor extends AbstractEntity {

    /**
     * The Poller class that should be instantiated in order
     * to obtain a new Poller.
     */
    @Basic(optional=false)
    @Column(length=500, unique=true)
    private Class<? extends Poller> pollerClass;

    /**
     * The Poller name, used e.g. for displaying information about this Poller.
     */
    @Basic(optional=false)
    @Column(length=50, unique=true)
    private String name;

    /**
     * A full description of the Poller, for displaying purposes.
     */
    @Basic(optional=true)
    @Column(length=1000)
    private String description;


    public PollerDescriptor() {
        super();
    }


    public PollerDescriptor(final Class<? extends Poller> pollerClass, final String name, final String description) {
        super();
        this.pollerClass = pollerClass;
        this.name = name;
        this.description = description;
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

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Poller createPoller() {
        Poller poller;
        try {
            poller = this.pollerClass.newInstance();
        } catch (final InstantiationException e) {
            throw new IllegalStateException(e);
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        return poller;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("id", this.getId()) //
                .append("name", this.name) //
                .append("pollerClass", this.pollerClass) //
                .toString();
    }
}
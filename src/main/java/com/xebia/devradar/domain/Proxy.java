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

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This object describes an HTTP proxy.
 * 
 * @author Alexandre Dutra
 *
 */
@Embeddable
@Access(AccessType.FIELD)
public class Proxy {

    /**
     * The proxy host.
     */
    @Basic(optional = true)
    @Column(length = 255)
    private String host;

    /**
     * The proxy port.
     */
    @Basic(optional = true)
    private Integer port;

    /**
     * The user name and password, if the proxy
     * needs HTTP authentication.
     */
    @Embedded
    private Authentication authentication;

    public Proxy() {
    }

    public Proxy(final String host, final int port) {
        super();
        this.host = host;
        this.port = port;
    }

    public Proxy(final String host, final int port, final Authentication authentication) {
        super();
        this.host = host;
        this.port = port;
        this.authentication = authentication;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(final String proxyHost) {
        this.host = proxyHost;
    }

    public Integer getPort() {
        return this.port;
    }

    public void setPort(final Integer proxyPort) {
        this.port = proxyPort;
    }

    public Authentication getAuthentication() {
        return this.authentication;
    }

    public void setAuthentication(final Authentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("host", this.host)//
                .append("port", this.port)//
                .toString();
    }
}

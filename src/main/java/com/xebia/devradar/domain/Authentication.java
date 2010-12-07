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

import java.net.PasswordAuthentication;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This object holds a user name and a password, and should be used
 * if an Event Source needs HTTP authentication.
 * Can act as a bridge to <code>{@link PasswordAuthentication}</code> via
 * the method <code>{@link #getPasswordAuthentication()}</code>.
 * 
 * @author Alexandre Dutra
 *
 */
@Embeddable
@Access(AccessType.FIELD)
public class Authentication {

    /**
     * The user name, if the Event Source
     * needs HTTP authentication.
     */
    @Basic(optional = true)
    @Column(length = 100)
    private String username;

    /**
     * The password, if the Event Source
     * needs HTTP authentication.
     */
    @Basic(optional = true)
    @Column(length = 255)
    private char[] password;

    public Authentication() {
    }

    public Authentication(final String username, final char[] password) {
        super();
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public char[] getPassword() {
        return this.password;
    }

    public void setPassword(final char[] password) {
        this.password = password;
    }

    public PasswordAuthentication getPasswordAuthentication(){
        return new PasswordAuthentication(this.username, this.password);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("username", this.username)//
                .toString();
    }
}

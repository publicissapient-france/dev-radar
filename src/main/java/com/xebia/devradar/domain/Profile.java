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

import com.xebia.devradar.utils.GravatarUtils;

import javax.persistence.*;

@Entity
@Access(AccessType.FIELD)
@NamedQueries({
@NamedQuery(name = Profile.ORDER_BY_NAME, query = "from Profile p order by p.nickname")
})
public class Profile extends AbstractEntity {

    public static final String ORDER_BY_NAME = "Profile.orderByName";

    @Basic(optional = false)
    @Column(length = 30)
    private String nickname;

    @Basic(optional = false)
    @Column(length = 80)
    private String email;

    @Basic(optional = false)
    private String gravatarUrl;

    @Column(length = 30)
    private String aliasSCM;

    public Profile() {
    }

    public Profile(String nickname, String email) {
        this.nickname = nickname;
        this.aliasSCM = nickname;
        this.email = email;
        this.gravatarUrl = GravatarUtils.constructGravatarUrlFromEmail(email, false, true);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.gravatarUrl = GravatarUtils.constructGravatarUrlFromEmail(email, false, true);
    }

    public String getGravatarUrl() {
        return gravatarUrl;
    }

    public void setGravatarUrl(String gravatarUrl) {
        this.gravatarUrl = gravatarUrl;
    }

    public String getAliasSCM() {
        return aliasSCM;
    }

    public void setAliasSCM(String aliasSCM) {
        this.aliasSCM = aliasSCM;
    }
}

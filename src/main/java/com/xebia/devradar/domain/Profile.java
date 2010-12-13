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
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import com.xebia.devradar.utils.Gravatar;

@Entity
@Access(AccessType.FIELD)
@NamedQueries({
@NamedQuery(name = Profile.ORDER_BY_NAME, query = "from Profile p order by p.nickname")
})
public class Profile extends AbstractEntity {

    public static final String ORDER_BY_NAME = "Profile.orderByName";

    public static final Gravatar GRAVATAR = new Gravatar();

    @NotBlank
    @Size(min = 1, max = 30)
    private String nickname;

    @NotBlank
    @Email
    @Size(min = 1, max = 80)
    private String email;

    @NotBlank
    @URL
    private String gravatarUrl;

    @Size(min = 0, max = 30)
    private String aliasSCM;

    public Profile() {
    }

    public Profile(String nickname, String email) {
        this(nickname, email, nickname);
    }

    public Profile(String nickname, String email, String aliasSCM) {
        this(nickname,
            email,
            GRAVATAR.getUrl(email),
            aliasSCM);
    }

    private Profile(String nickname, String email, String gravatarUrl, String aliasSCM) {
        this.nickname = nickname;
        this.email = email;
        this.gravatarUrl = gravatarUrl;
        this.aliasSCM = aliasSCM;
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
        this.gravatarUrl = GRAVATAR.getUrl(email);
    }

    public String getGravatarUrl() {
        return gravatarUrl;
    }

    public String getAliasSCM() {
        return aliasSCM;
    }

    public void setAliasSCM(String aliasSCM) {
        this.aliasSCM = aliasSCM;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("id", this.getId()) //
                .append("nickname", this.nickname)//
                .toString();
    }
}

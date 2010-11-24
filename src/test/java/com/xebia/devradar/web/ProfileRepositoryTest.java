/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.xebia.devradar.web;

import com.xebia.devradar.domain.Profile;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xebia.devradar.persistence.AbstractRepositoryTests;
import com.xebia.devradar.persistence.DbUnitDataset;

public class ProfileRepositoryTest extends AbstractRepositoryTests {

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    public void createProfileShouldInsertOneRow() {
        final String nickName = "blebens";
        final String email = "beau@dentedreality.com.au";
        Profile profile = new Profile(nickName, email);
        profile = this.profileRepository.createProfile(profile);
        Assert.assertThat(profile, CoreMatchers.not(CoreMatchers.nullValue()));
        Assert.assertThat(profile.getNickname(), CoreMatchers.is(nickName));
        this.entityManager.flush();
        Assert.assertThat(this.countRowsInTable("PROFILE"), CoreMatchers.is(1));
    }

    @Test
    @DbUnitDataset("com/xebia/devradar/getProfile.xml")
    public void deleteProfileShouldDeleteOneRow() {
        final Profile profile = this.entityManager.find(Profile.class, 1L);
        this.profileRepository.deleteProfile(profile);
        this.entityManager.flush();
        Assert.assertThat(this.countRowsInTable("PROFILE"), CoreMatchers.is(0));
    }
}

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

import java.util.Set;

import com.xebia.devradar.EventType;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import com.xebia.devradar.domain.Event;
import com.xebia.devradar.persistence.AbstractRepositoryTests;
import com.xebia.devradar.persistence.DbUnitDataset;

public class EventRepositoryTest extends AbstractRepositoryTests {

    @Autowired
    private EventRepository repository;

    /**
     * Expects the translated exception <code>{@link EmptyResultDataAccessException}</code>.
     */
    @Test(expected = EmptyResultDataAccessException.class)
    public void getEventsForWorkspaceShouldThrowException() {
        this.repository.getEventsForWorkspace("foo");
    }

    @Test
    @DbUnitDataset("com/xebia/devradar/getEventsForWorkspaceShouldReturnTwoEvents.xml")
    public void getEventsForWorkspaceShouldReturnTwoEvents() {
        final Set<Event> eventsForWorkspace = this.repository.getEventsForWorkspace("TEST");
        Assert.assertThat(eventsForWorkspace, CoreMatchers.not(CoreMatchers.nullValue()));
        Assert.assertThat(eventsForWorkspace.size(), CoreMatchers.is(2));
    }

    @Test
    @DbUnitDataset("com/xebia/devradar/getProfilWhoHaveMaxEventTypeShouldReturnBill.xml")
    public void getProfilWhoHaveMaxEventTypeShouldReturnBill() {
        final Long  profil = this.repository.getProfilIdWhoHaveMaxEventType(1L, EventType.COMMIT);
        Assert.assertThat(profil, CoreMatchers.is(2L));
    }

}

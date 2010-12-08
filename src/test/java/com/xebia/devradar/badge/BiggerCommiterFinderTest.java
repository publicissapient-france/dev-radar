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
package com.xebia.devradar.badge;

import com.xebia.devradar.EventType;
import com.xebia.devradar.web.EventRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class BiggerCommiterFinderTest {

    private BiggerCommiterFinder biggerCommiterFinder;

    private EventRepository eventRepository;

    @Before
    public void init() {
        eventRepository = mock(EventRepository.class);
        biggerCommiterFinder = new BiggerCommiterFinder(eventRepository);
    }

    @Test
    public void should_find_badge_owner_for_workspace() {
        Mockito.when(eventRepository.getProfilIdWhoHaveMaxEventType(1L, EventType.COMMIT)).thenReturn(42L);

        Long profilId = biggerCommiterFinder.findBadgeOwnerForWorkspace(1L);
        
        assertThat(profilId, is(42L));
    }
}

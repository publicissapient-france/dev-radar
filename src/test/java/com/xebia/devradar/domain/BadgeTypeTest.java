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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BadgeTypeTest {

    @Test
    public void should_refresh_profile_with_dsl() {
        EntityManager entityManager = mock(EntityManager.class);
        Query query = mock(Query.class);

        BadgeType badgeType = new BadgeType();
        badgeType.setEntityManager(entityManager);
        badgeType.setDslQuery("select e.profile.id from Event e where e.workspace.id = :workspaceId and e.eventType = 'COMMIT' group by e.profile.id order by count(e.id) desc");
        badgeType.setName("badgeTypeName");

        Workspace workspace = new Workspace();
        workspace.setId(12L);

        Profile profileExpected = new Profile();
        profileExpected.setId(21L);
        profileExpected.setNickname("bill");

        List<Long> profiles = Arrays.asList(profileExpected.getId());

        when(entityManager.createQuery(badgeType.getDslQuery())).thenReturn(query);
        when(query.setParameter("workspaceId", 12L)).thenReturn(query);
        when(query.setMaxResults(1)).thenReturn(query);
        when(query.getResultList()).thenReturn(profiles);
        when(entityManager.find(Profile.class, 21L)).thenReturn(profileExpected);

        Profile profileActual = badgeType.getBadgeOwnerOfWorkspace(workspace);

        assertThat(profileActual, is(profileExpected));
        assertThat(profileActual.getId(), is(21L));
        assertThat(profileActual.getNickname(), is("bill"));
    }
}

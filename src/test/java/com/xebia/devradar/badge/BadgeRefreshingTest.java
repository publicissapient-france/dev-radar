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

import com.xebia.devradar.domain.Badge;
import com.xebia.devradar.persistence.AbstractRepositoryTests;
import com.xebia.devradar.persistence.DbUnitDataset;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class BadgeRefreshingTest extends AbstractRepositoryTests {

    @Test
    @DbUnitDataset("com/xebia/devradar/shouldFindBadgeBiggerCommiterWithDsl.xml")
    public void should_find_bigger_commiter_with_dsl() {
        Badge badge = entityManager.find(Badge.class, 1L);

        badge.refreshBadgeOwner();
        assertThat(badge.getGravatarUrl(), is("GOOD GRAV URL"));
    }

    @Test
    @DbUnitDataset("com/xebia/devradar/shouldFindBadgeBiggerCommiterWithClass.xml")
    public void should_find_bigger_commiter_profil_with_class() {
        Badge badge = entityManager.find(Badge.class, 1L);

        badge.refreshBadgeOwner();
        assertThat(badge.getGravatarUrl(), is("GOOD GRAV URL"));
    }

    @Test
    @DbUnitDataset("com/xebia/devradar/shouldFindNoBadgeWithDsl.xml")
    public void should_find_no_profil_with_dsl() {
        Badge badge = entityManager.find(Badge.class, 1L);

        badge.refreshBadgeOwner();
        assertThat(badge.getGravatarUrl(), nullValue());
    }

    @Test
    @DbUnitDataset("com/xebia/devradar/shouldFindNoBadgeWithClass.xml")
    public void should_find_no_profil_with_class() {
        Badge badge = entityManager.find(Badge.class, 1L);

        badge.refreshBadgeOwner();
        assertThat(badge.getGravatarUrl(), nullValue());
    }
}

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
package com.xebia.devradar.domain.dao;

import com.xebia.devradar.domain.BadgeType;
import com.xebia.devradar.persistence.AbstractRepositoryTests;
import com.xebia.devradar.persistence.DbUnitDataset;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BadgeTypeRepositoryTest extends AbstractRepositoryTests {

    @Autowired
    private BadgeTypeRepository badgeTypeRepository;

    public void testGetAll() throws Exception {
    }

    @Test
    @DbUnitDataset("com/xebia/devradar/shouldGetAllBadgeTypes.xml")
    public void shouldGetAllBadgeTypes() {
        final List<BadgeType> badgeTypes = this.badgeTypeRepository.getAll();
        assertThat(badgeTypes.size(), is(3));
    }

    @Test
    @DbUnitDataset("com/xebia/devradar/shouldGetBadgeTypeById.xml")
    public void shouldGetBadgeTypeId() {
        final BadgeType badgeType = this.badgeTypeRepository.getBadgeTypeById(2L);
        assertThat(badgeType.getId(), is(2L));
    }
}

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
package com.xebia.devradar.web.controller;

import com.xebia.devradar.domain.BadgeType;
import com.xebia.devradar.domain.Workspace;
import com.xebia.devradar.domain.dao.BadgeTypeRepository;
import com.xebia.devradar.web.WorkspaceRepository;
import com.xebia.devradar.web.model.badge.AssociateBadgeModel;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.support.SimpleSessionStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class BadgesControllerTest {

    private WorkspaceRepository workspaceRepository;

    private BadgesController badgesController;

    private BadgeTypeRepository badgeTypeRepository;

    @Before
    public void init() {
        workspaceRepository = mock(WorkspaceRepository.class);
        badgeTypeRepository = mock(BadgeTypeRepository.class);
        badgesController = new BadgesController(badgeTypeRepository, workspaceRepository);
    }

    @Test
    public void should_init_associate_badge_type_model() {
        Model model = new ExtendedModelMap();
        List<BadgeType> badgeTypes = new ArrayList<BadgeType>();
        Long[] badgeTypeIdsArray = new Long[] { 21L, 42L, 53L};
        List<Long> badgeTypeIds = Arrays.asList(badgeTypeIdsArray);

        when(badgeTypeRepository.getAll()).thenReturn(badgeTypes);
        when(badgeTypeRepository.getBadgeTypeIdsByWorkspaceId(1L)).thenReturn(badgeTypeIds);

        String view = badgesController.initCreateBadgeTypeForm(1L, model);

        assertThat(model.asMap().get(AssociateBadgeModel.ASSOCIATE_BADGE_MODEL) instanceof AssociateBadgeModel, is(true));
        AssociateBadgeModel associateBadgeModel = (AssociateBadgeModel) model.asMap().get(AssociateBadgeModel.ASSOCIATE_BADGE_MODEL);
        assertThat(associateBadgeModel.getBadgeTypeIds(), is(badgeTypeIdsArray));
        assertThat(associateBadgeModel.getWorkspaceId(), is(1L));
        assertThat(associateBadgeModel.getBadgeTypes(), is(badgeTypes));
        assertThat(view, is(AssociateBadgeModel.NAME));
    }

    @Test
    public void should_associate_badge_type_model() {
        AssociateBadgeModel associateBadgeModel = new AssociateBadgeModel();
        List<BadgeType> badgeTypes = new ArrayList<BadgeType>();
        Long[] badgeTypeIdsArray = new Long[] { 21L, 42L, 53L};
        Workspace workspace = mock(Workspace.class);
        SessionStatus status = new SimpleSessionStatus();

        associateBadgeModel.setWorkspaceId(1L);
        associateBadgeModel.setBadgeTypeIds(badgeTypeIdsArray);
        associateBadgeModel.setBadgeTypes(badgeTypes);

        when(workspaceRepository.getWorkspaceById(1L)).thenReturn(workspace);

        String view = badgesController.associateBadges(associateBadgeModel, status);

        verify(workspace, times(1)).updateBadges(badgeTypeIdsArray);

        assertThat(status.isComplete(), is(true));
        assertThat(view, is("redirect:/workspaces/null.html"));
    }
}

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
import com.xebia.devradar.web.model.badge.type.BadgeTypeFormModel;
import com.xebia.devradar.web.model.badge.type.BadgeTypeListModel;
import com.xebia.devradar.web.model.badge.type.BadgeTypeShowModel;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.support.SimpleSessionStatus;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class BadgeTypesControllerTest {

    private WorkspaceRepository workspaceRepository;

    private BadgeTypesController badgeTypesController;

    private BadgeTypeRepository badgeTypeRepository;

    @Before
    public void init() {
        workspaceRepository = mock(WorkspaceRepository.class);
        badgeTypeRepository = mock(BadgeTypeRepository.class);
        badgeTypesController = new BadgeTypesController(badgeTypeRepository, workspaceRepository);
    }

    @Test
    public void should_init_create_badge_type_model() {
        Model model = new ExtendedModelMap();

        String view = badgeTypesController.initCreateBadgeTypeForm(model);

        assertThat(model.asMap().get(BadgeTypeFormModel.BADGE_TYPE_FORM_MODEL) instanceof BadgeTypeFormModel, is(true));
        assertThat(view, is(BadgeTypeFormModel.NAME));
    }

    @Test
    public void should_create_badge_type() throws Exception {
        BadgeTypeFormModel badgeTypeFormModel = new BadgeTypeFormModel();
        badgeTypeFormModel.setBadgeType(mock(BadgeType.class));
        badgeTypeFormModel.setTestMode(false);
        BindingResult result = new MapBindingResult(new HashMap<Object, Object>(), "bindingResult");
        SessionStatus status = new SimpleSessionStatus();

        String view = badgeTypesController.createBadgeType(badgeTypeFormModel, result, status);

        verify(badgeTypeFormModel.getBadgeType(), times(1)).create();

        assertThat(status.isComplete(), is(true));
        assertThat(view, is("redirect:/badgeTypes/" + badgeTypeFormModel.getBadgeType().getId() +".html"));
    }

    @Test
    public void should_test_create_badge_type() throws Exception {
        BadgeTypeFormModel badgeTypeFormModel = new BadgeTypeFormModel();
        badgeTypeFormModel.setBadgeType(mock(BadgeType.class));
        badgeTypeFormModel.setTestMode(true);
        badgeTypeFormModel.setWorkspaceId(1L);
        Workspace workspace = new Workspace();
        String gravatarUrl = "gravatarUrl";
        BindingResult result = new MapBindingResult(new HashMap<Object, Object>(), "bindingResult");
        SessionStatus status = new SimpleSessionStatus();

        when(workspaceRepository.getWorkspaceById(1L)).thenReturn(workspace);
        when(badgeTypeFormModel.getBadgeType().getBadgeOwnerOfWorkspace(workspace)).thenReturn(gravatarUrl);

        String view = badgeTypesController.createBadgeType(badgeTypeFormModel, result, status);

        assertThat(badgeTypeFormModel.getGravatarUrl(), is(gravatarUrl));
        assertThat(status.isComplete(), is(false));
        assertThat(view, is(BadgeTypeFormModel.NAME));
    }

    @Test
    public void should_test_invalid_dsl_create_badge_type() throws Exception {
        BadgeTypeFormModel badgeTypeFormModel = new BadgeTypeFormModel();
        badgeTypeFormModel.setBadgeType(mock(BadgeType.class));
        badgeTypeFormModel.setTestMode(true);
        badgeTypeFormModel.setWorkspaceId(1L);
        Workspace workspace = new Workspace();
        BindingResult result = new MapBindingResult(new HashMap<Object, Object>(), "bindingResult");
        SessionStatus status = new SimpleSessionStatus();

        when(workspaceRepository.getWorkspaceById(1L)).thenReturn(workspace);
        when(badgeTypeFormModel.getBadgeType().getBadgeOwnerOfWorkspace(workspace)).thenThrow(new BadSqlGrammarException("task", "sql query", new SQLException("error msg")));

        String view = badgeTypesController.createBadgeType(badgeTypeFormModel, result, status);

        assertThat(result.getErrorCount(), is(1));
        assertThat(result.getFieldError("gravatarUrl").getDefaultMessage(), is("task; bad SQL grammar [sql query]; nested exception is java.sql.SQLException: error msg"));
        Assert.assertNull(badgeTypeFormModel.getGravatarUrl());
        assertThat(status.isComplete(), is(false));
        assertThat(view, is(BadgeTypeFormModel.NAME));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_list_badge_types() {
        List<BadgeType> badgeTypesReturned = new ArrayList<BadgeType>();

        badgeTypesReturned.add(new BadgeType());
        badgeTypesReturned.add(new BadgeType());

        when(badgeTypeRepository.getAll()).thenReturn(badgeTypesReturned);

        ModelAndView mav = badgeTypesController.listBadgeTypes();
        assertThat(mav.getViewName(), equalTo(BadgeTypeListModel.NAME));

        List<BadgeType> badgeTypes = (List<BadgeType>) mav.getModel().get(BadgeTypeListModel.BADGE_TYPES);
        assertThat(badgeTypes, notNullValue());
        assertThat(badgeTypes.size(), is(2));
    }

    @Test
    public void should_show_badge_type() throws Exception {
        Model model = new ExtendedModelMap();
        BadgeType badgeType = new BadgeType();

        when(badgeTypeRepository.getBadgeTypeById(1L)).thenReturn(badgeType);

        String view = badgeTypesController.showBadgeType(1L, model);

        assertThat(model.asMap().get(BadgeTypeShowModel.BADGE_TYPE) instanceof BadgeType, is(true));
        assertThat(view, is(BadgeTypeShowModel.NAME));
    }

    @Test
    public void should_delete_badge_type() throws Exception {
        BadgeType badgeType = mock(BadgeType.class);
        SessionStatus status = new SimpleSessionStatus();


        when(badgeTypeRepository.getBadgeTypeById(1L)).thenReturn(badgeType);

        String view = badgeTypesController.deleteBadgeType(1L, status);

        verify(badgeType, times(1)).delete();

        assertThat(status.isComplete(), is(true));
        assertThat(view, is("redirect:/badgeTypes/list.html"));
    }

    @Test
    public void should_init_edit_badge_type_model() {
        BadgeType badgeType = mock(BadgeType.class);
        List<Workspace> workspaces = new ArrayList<Workspace>();
        Model model = new ExtendedModelMap();

        when(badgeTypeRepository.getBadgeTypeById(1L)).thenReturn(badgeType);
        when(workspaceRepository.getAll()).thenReturn(workspaces);

        String view = badgeTypesController.initEditBadgeTypeForm(1L, model);

        assertThat(model.asMap().get(BadgeTypeFormModel.BADGE_TYPE_FORM_MODEL) instanceof BadgeTypeFormModel, is(true));
        assertThat(((BadgeTypeFormModel)model.asMap().get(BadgeTypeFormModel.BADGE_TYPE_FORM_MODEL)).getBadgeType(), is(badgeType));
        assertThat(((BadgeTypeFormModel)model.asMap().get(BadgeTypeFormModel.BADGE_TYPE_FORM_MODEL)).getWorkspaces(), is(workspaces));
        assertThat(view, is(BadgeTypeFormModel.NAME));
    }

    @Test
    public void should_edit_badge_type_model() {
        BadgeTypeFormModel badgeTypeFormModel = new BadgeTypeFormModel();
        BadgeType badgeType = mock(BadgeType.class);
        badgeTypeFormModel.setBadgeType(badgeType);
        badgeTypeFormModel.setTestMode(false);
        BindingResult result = new MapBindingResult(new HashMap<Object, Object>(), "bindingResult");
        SessionStatus status = new SimpleSessionStatus();

        String view = badgeTypesController.editBadgeType(badgeTypeFormModel, result, status);

        verify(badgeTypeFormModel.getBadgeType(), times(1)).update();

        assertThat(status.isComplete(), is(true));
        assertThat(view, is("redirect:/badgeTypes/" + badgeTypeFormModel.getBadgeType().getId() +".html"));
    }

    @Test
    public void should_test_edit_badge_type() throws Exception {
        BadgeTypeFormModel badgeTypeFormModel = new BadgeTypeFormModel();
        badgeTypeFormModel.setBadgeType(mock(BadgeType.class));
        badgeTypeFormModel.setTestMode(true);
        badgeTypeFormModel.setWorkspaceId(1L);
        Workspace workspace = new Workspace();
        String gravatarUrl = "gravatarUrl";
        BindingResult result = new MapBindingResult(new HashMap<Object, Object>(), "bindingResult");
        SessionStatus status = new SimpleSessionStatus();

        when(workspaceRepository.getWorkspaceById(1L)).thenReturn(workspace);
        when(badgeTypeFormModel.getBadgeType().getBadgeOwnerOfWorkspace(workspace)).thenReturn(gravatarUrl);

        String view = badgeTypesController.editBadgeType(badgeTypeFormModel, result, status);

        assertThat(badgeTypeFormModel.getGravatarUrl(), is(gravatarUrl));
        assertThat(status.isComplete(), is(false));
        assertThat(view, is(BadgeTypeFormModel.NAME));
    }

    @Test
    public void should_test_invalid_dsl_edit_badge_type() throws Exception {
        BadgeTypeFormModel badgeTypeFormModel = new BadgeTypeFormModel();
        badgeTypeFormModel.setBadgeType(mock(BadgeType.class));
        badgeTypeFormModel.setTestMode(true);
        badgeTypeFormModel.setWorkspaceId(1L);
        Workspace workspace = new Workspace();
        BindingResult result = new MapBindingResult(new HashMap<Object, Object>(), "bindingResult");
        SessionStatus status = new SimpleSessionStatus();

        when(workspaceRepository.getWorkspaceById(1L)).thenReturn(workspace);
        when(badgeTypeFormModel.getBadgeType().getBadgeOwnerOfWorkspace(workspace)).thenThrow(new BadSqlGrammarException("task", "sql query", new SQLException("error msg")));

        String view = badgeTypesController.editBadgeType(badgeTypeFormModel, result, status);

        assertThat(result.getErrorCount(), is(1));
        assertThat(result.getFieldError("gravatarUrl").getDefaultMessage(), is("task; bad SQL grammar [sql query]; nested exception is java.sql.SQLException: error msg"));
        Assert.assertEquals(badgeTypeFormModel.getGravatarUrl(), null);
        assertThat(status.isComplete(), is(false));
        assertThat(view, is(BadgeTypeFormModel.NAME));
    }
}

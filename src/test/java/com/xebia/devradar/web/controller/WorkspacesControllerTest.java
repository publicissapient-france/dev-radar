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

import com.xebia.devradar.domain.Event;
import com.xebia.devradar.domain.Workspace;
import com.xebia.devradar.pollers.PollersInvoker;
import com.xebia.devradar.utils.PomLoaderUtils;
import com.xebia.devradar.utils.WorkspaceFactory;
import com.xebia.devradar.web.WorkspaceRepository;
import com.xebia.devradar.web.model.WorkspaceListModel;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.support.SimpleSessionStatus;
import org.springframework.web.servlet.ModelAndView;

import java.net.URL;
import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

public class WorkspacesControllerTest {

    private WorkspaceRepository workspaceRepository;

    private WorkspacesController workspacesController;

    private PollersInvoker pollersInvoker;

    private WorkspaceFactory workspaceFactory;

    @Before
    public void init() {
        workspaceRepository = mock(WorkspaceRepository.class);
        workspaceFactory = mock(WorkspaceFactory.class);
        pollersInvoker = mock(PollersInvoker.class);
        workspacesController = new WorkspacesController(workspaceRepository, workspaceFactory, pollersInvoker);
    }

    @Test
    public void should_init_create_workspace_model() {
        Model model = new ExtendedModelMap();

        String view = workspacesController.initCreateWorkspaceForm(model);

        assertThat(model.asMap().get("workspace") instanceof Workspace, is(true));
        assertThat(view, is("workspaces/form"));
    }

    @Test
    public void should_create_workspace() throws Exception {
        Workspace workspace = getWorkspace();
        BindingResult result = new MapBindingResult(new HashMap<Object, Object>(), "bindingResult");
        SessionStatus status = new SimpleSessionStatus();

        when(workspaceRepository.createWorkspace(workspace)).thenReturn(workspace);
        when(workspaceFactory.create(PomLoaderUtils.create(new URL(workspace.getPomUrl())))).thenReturn(workspace);

        String view = workspacesController.createWorkspace(workspace, result, status);

        assertThat(status.isComplete(), is(true));
        assertThat(view, is("redirect:/workspaces/" + workspace.getId()+".html"));
    }

    @Test
    public void should_not_create_workspace_causes_form_errors() throws Exception {
        Workspace workspace = getWorkspace();
        BindingResult result = new MapBindingResult(new HashMap<Object, Object>(), "bindingResult");
        SessionStatus status = new SimpleSessionStatus();

        result.addError(new ObjectError("field", "default message"));

        when(workspaceRepository.createWorkspace(workspace)).thenReturn(workspace);
        when(workspaceFactory.create(PomLoaderUtils.create(new URL(workspace.getPomUrl())))).thenReturn(workspace);

        String view = workspacesController.createWorkspace(workspace, result, status);

        assertThat(status.isComplete(), is(false));
        assertThat(view, is("workspaces/form"));
    }

    @Test
    public void should_init_edit_workspace_model() {
        Workspace workspace = getWorkspace();
        Model model = new ExtendedModelMap();

        when(workspaceRepository.getWorkspaceById(workspace.getId())).thenReturn(workspace);

        String view = workspacesController.initEditWorkspaceForm(workspace.getId(), model);

        assertThat((Workspace) model.asMap().get("workspace"), is(workspace));
        assertThat(view, is("workspaces/form"));
    }

    @Test
    public void should_edit_workspace() throws Exception {
        Workspace workspace = getWorkspace();
        BindingResult result = new MapBindingResult(new HashMap<Object, Object>(), "bindingResult");
        SessionStatus status = new SimpleSessionStatus();

        when(workspaceRepository.updateWorkspace(workspace)).thenReturn(workspace);

        String view = workspacesController.editWorkspace(workspace, result, status);

        assertThat(status.isComplete(), is(true));
        assertThat(view, is("redirect:/workspaces/" + workspace.getId() + ".html"));
    }

    @Test
    // pom AND name must be valid ?
    public void should_not_edit_workspace_cause_invalid_pom_url_and_invalid_name() throws Exception {
        Workspace workspace = getWorkspace();
        BindingResult result = new MapBindingResult(new HashMap<Object, Object>(), "bindingResult");
        SessionStatus status = new SimpleSessionStatus();

        workspace.setPomUrl("");
        workspace.setName("");

        when(workspaceRepository.updateWorkspace(workspace)).thenReturn(workspace);

        String view = workspacesController.editWorkspace(workspace, result, status);

        assertThat(result.getErrorCount(), is(2));
        assertThat(result.getFieldError("pomUrl"), notNullValue());
        assertThat(result.getFieldError("name"), notNullValue());
        assertThat(status.isComplete(), is(false));
        assertThat(view, is("workspaces/form"));
    }

    @Test
    @Ignore
    public void should_not_edit_workspace_cause_workspace_doesn_t_exist() throws Exception {
        //TODO
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_return_all_workspaces() {
        when(workspaceRepository.getAllWorkspaces()).thenReturn(someWorkspaceList());

        ModelAndView mav = workspacesController.listWorkspaces();
        assertThat(mav.getViewName(), equalTo(WorkspaceListModel.NAME));

        List<Workspace> workspaces = (List<Workspace>) mav.getModel().get(WorkspaceListModel.WORKSPACES);
        assertThat(workspaces, notNullValue());
        assertThat(workspaces.size(), is(2));
    }

    @Test
    public void should_show_workspace() throws Exception {
        Model model = new ExtendedModelMap();
        Workspace workspace = getWorkspace();

        when(workspaceRepository.getWorkspaceById(workspace.getId())).thenReturn(workspace);

        String view = workspacesController.showWorkspace(workspace.getId(), model);

        assertThat(model.asMap().get("workspace") instanceof Workspace, is(true));
        assertThat(view, is("workspaces/show"));
    }

    @Test
    @Ignore
    public void should_not_show_workspace_cause_workspace_doesn_t_exist() throws Exception {
        //TODO
    }

    @Test
    //TODO use workspace identifier
    public void should_delete_workspace() throws Exception {
        Model model = new ExtendedModelMap();
        Workspace workspace = getWorkspace();
        SessionStatus status = new SimpleSessionStatus();

        when(workspaceRepository.getWorkspaceById(workspace.getId())).thenReturn(workspace);

        String view = workspacesController.deleteWorkspace(workspace, status);

        assertThat(status.isComplete(), is(true));
        assertThat(view, is("redirect:/workspaces/list.html"));
    }

    @Test
    @Ignore
    public void should_not_delete_workspace_cause_workspace_doesn_t_exist() throws Exception {
        //TODO
    }


    @Test
    public void should_poll_workspace() throws Exception {
        Model model = new ExtendedModelMap();
        Workspace workspace = mock(Workspace.class);
        SessionStatus status = new SimpleSessionStatus();
        List<Event> events = Arrays.asList(new Event[] {new Event(), new Event(), new Event()});

        when(workspaceRepository.getWorkspaceById(1L)).thenReturn(workspace);
        when(workspace.poll(any(Date.class), any(Date.class))).thenReturn(events);

        String view = workspacesController.poll(1L, model);

        verify(pollersInvoker).pollWorkspace(any(Date.class), any(Date.class), eq(workspace));

        assertThat(view, is("workspaces/show"));

    }

    @Test
    @Ignore
    public void should_not_poll_workspace_cause_workspace_doesn_t_exist() throws Exception {
        //TODO
    }

    @Test
    public void should_refresh_badges_owners() throws Exception {
        Model model = new ExtendedModelMap();
        Workspace workspace = mock(Workspace.class);

        when(workspaceRepository.getWorkspaceById(1L)).thenReturn(workspace);

        String view = workspacesController.refreshBadgesOwners(1L, model);

        verify(workspace, times(1)).refreshBadges();

        assertThat(((Workspace)model.asMap().get("workspace")), is(workspace));
        assertThat(view, is("workspaces/show"));
    }

    private Workspace getWorkspace() {
        Workspace workspace = new Workspace();

        workspace.setId(1L);
        workspace.setDescription("description");
        workspace.setName("name");
        workspace.setPomUrl("https://github.com/xebia-france/dev-radar/raw/master/pom.xml");
        return workspace;
    }

    private List<Workspace> someWorkspaceList() {
        List<Workspace> workspaces = new ArrayList<Workspace>();
        workspaces.add(new Workspace());
        workspaces.add(new Workspace());
        return workspaces;
    }
}

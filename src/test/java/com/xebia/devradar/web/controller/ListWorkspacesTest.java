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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.xebia.devradar.web.controller.workspaces.ListWorkspaces;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import com.xebia.devradar.domain.Workspace;
import com.xebia.devradar.web.WorkspaceRepository;
import com.xebia.devradar.web.model.WorkspaceListModel;

public class ListWorkspacesTest {
    private WorkspaceRepository workspaceRepository;

    private ListWorkspaces listWorkspaces;

    @Before
    public void init() {
        workspaceRepository = mock(WorkspaceRepository.class);
        listWorkspaces = new ListWorkspaces(workspaceRepository);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_return_all_workspaces() {
        when(workspaceRepository.getAllWorkspaces()).thenReturn(someWorkspaceList());
        
        ModelAndView mav = listWorkspaces.getAllWorkspaces();
        assertThat(mav.getViewName(), equalTo(WorkspaceListModel.NAME));
        
        List<Workspace> workspaces = (List<Workspace>) mav.getModel().get(WorkspaceListModel.WORKSPACES);
        assertThat(workspaces, notNullValue());
        assertThat(workspaces.size(), is(2));
    }

    private List<Workspace> someWorkspaceList() {
        List<Workspace> workspaces = new ArrayList<Workspace>();
        workspaces.add(new Workspace());
        workspaces.add(new Workspace());
        return workspaces;
    }
}

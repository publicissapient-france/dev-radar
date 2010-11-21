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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xebia.devradar.domain.EventSource;
import com.xebia.devradar.domain.Type;
import com.xebia.devradar.domain.Workspace;
import com.xebia.devradar.persistence.AbstractRepositoryTests;
import com.xebia.devradar.persistence.DbUnitDataset;

public class WorkspaceRepositoryTest extends AbstractRepositoryTests {

    @Autowired
    private WorkspaceRepository repository;

    @Test
    public void createWorkspaceShouldInsertOneRow() throws MalformedURLException {
        final String workspaceName = "TEST";
        final Workspace w = this.repository.createWorkspace(workspaceName);
        Assert.assertThat(w, CoreMatchers.not(CoreMatchers.nullValue()));
        Assert.assertThat(w.getName(), CoreMatchers.is(workspaceName));
        w.addEventSource(new EventSource(Type.SVN, new URL("http://test.com/svn")));
        w.addEventSource(new EventSource(Type.JIRA, new URL("http://test.com/jira")));
        this.entityManager.flush();
        Assert.assertThat(this.countRowsInTable("WORKSPACE"), CoreMatchers.is(1));
        Assert.assertThat(this.countRowsInTable("WORKSPACE_EVENTSOURCES"), CoreMatchers.is(2));
    }

    @Test
    @DbUnitDataset("com/xebia/devradar/dataset1.xml")
    public void deleteWorkspaceShouldDeleteOrphanEvents() {
        final Workspace w = this.entityManager.find(Workspace.class, 1L);
        this.repository.deleteWorkspace(w);
        this.entityManager.flush();
        Assert.assertThat(this.countRowsInTable("WORKSPACE"), CoreMatchers.is(0));
        Assert.assertThat(this.countRowsInTable("WORKSPACE_EVENTSOURCES"), CoreMatchers.is(0));
        Assert.assertThat(this.countRowsInTable("EVENT"), CoreMatchers.is(0));
    }
    
    @Test
    @DbUnitDataset("com/xebia/devradar/workspaceShouldGetWorkspacesOrdered.xml")
    public void shouldGetWorkspacesOrdered() {
        List<Workspace> workspaces = this.repository.getAllWorkspaces();
        Assert.assertThat(workspaces.size(), CoreMatchers.is(5));
        Assert.assertThat(workspaces.get(0).getName(), CoreMatchers.is("WORKSPACE A"));
        Assert.assertThat(workspaces.get(1).getName(), CoreMatchers.is("WORKSPACE B"));
        Assert.assertThat(workspaces.get(2).getName(), CoreMatchers.is("WORKSPACE C"));
        Assert.assertThat(workspaces.get(3).getName(), CoreMatchers.is("WORKSPACE D"));
        Assert.assertThat(workspaces.get(4).getName(), CoreMatchers.is("WORKSPACE E"));
    }
}

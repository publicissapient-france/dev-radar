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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xebia.devradar.domain.Event;
import com.xebia.devradar.domain.EventSource;
import com.xebia.devradar.domain.PollerDescriptor;
import com.xebia.devradar.domain.Workspace;
import com.xebia.devradar.persistence.AbstractRepositoryTests;
import com.xebia.devradar.persistence.DbUnitDataset;

public class WorkspaceRepositoryTest extends AbstractRepositoryTests {

    private static final String AN_AUTHOR = "a. nonyme";

    @Autowired
    private WorkspaceRepository repository;

    @Test
    @DbUnitDataset("com/xebia/devradar/createWorkspaceShouldInsertOneRow.xml")
    public void createWorkspaceShouldInsertOneRow() throws MalformedURLException {
        final String workspaceName = "TEST";
        Workspace w = new Workspace(workspaceName);
        w = this.repository.createWorkspace(w);
        assertThat(w, not(nullValue()));
        assertThat(w.getName(), is(workspaceName));
        final PollerDescriptor jiraPollerDescriptor = this.entityManager.find(PollerDescriptor.class, 1L);
        final PollerDescriptor svnPollerDescriptor = this.entityManager.find(PollerDescriptor.class, 2L);
        final EventSource svnSource = new EventSource(svnPollerDescriptor, new URL("http://test.com/svn"), "Foo Project Subversion Trunk");
        w.addEventSource(svnSource);
        final EventSource jiraSource = new EventSource(jiraPollerDescriptor, new URL("http://test.com/jira"), "Foo Project Jira Issues");
        w.addEventSource(jiraSource);
        w.addEvent(new Event(svnSource, "User Joe committed something", new Date(), AN_AUTHOR));
        w.addEvent(new Event(jiraSource, "User Joe closed jira FOO-1234", new Date(), AN_AUTHOR));
        this.entityManager.flush();
        assertThat(this.countRowsInTable("WORKSPACE"), is(1));
        assertThat(this.countRowsInTable("WORKSPACE_EVENTSOURCE"), is(2));
        assertThat(this.countRowsInTable("EVENTSOURCE"), is(2));
        assertThat(this.countRowsInTable("WORKSPACE_EVENT"), is(2));
        assertThat(this.countRowsInTable("EVENT"), is(2));
    }

    @Test
    @DbUnitDataset("com/xebia/devradar/deleteWorkspaceShouldDeleteOrphans.xml")
    public void deleteWorkspaceShouldDeleteOrphans() {
        final Workspace w = this.entityManager.find(Workspace.class, 1L);
        this.repository.deleteWorkspace(w);
        this.entityManager.flush();
        //this should clean the tables below...
        assertThat(this.countRowsInTable("WORKSPACE"), is(0));
        assertThat(this.countRowsInTable("WORKSPACE_EVENTSOURCE"), is(0));
        assertThat(this.countRowsInTable("EVENTSOURCE"), is(0));
        assertThat(this.countRowsInTable("WORKSPACE_EVENT"), is(0));
        assertThat(this.countRowsInTable("EVENT"), is(0));
        //but should keep those rows
        assertThat(this.countRowsInTable("POLLERDESCRIPTOR"), is(2));
    }

    @Test
    @DbUnitDataset("com/xebia/devradar/workspaceShouldGetWorkspacesOrdered.xml")
    public void shouldGetWorkspacesOrdered() {
        final List<Workspace> workspaces = this.repository.getAllWorkspaces();
        assertThat(workspaces.size(), is(5));
        assertThat(workspaces.get(0).getName(), is("WORKSPACE A"));
        assertThat(workspaces.get(1).getName(), is("WORKSPACE B"));
        assertThat(workspaces.get(2).getName(), is("WORKSPACE C"));
        assertThat(workspaces.get(3).getName(), is("WORKSPACE D"));
        assertThat(workspaces.get(4).getName(), is("WORKSPACE E"));
    }
}

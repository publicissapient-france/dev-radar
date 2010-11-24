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
package com.xebia.devradar.utils;

import java.util.Set;

import static org.junit.Assert.*;
import org.junit.Test;

import com.xebia.devradar.domain.EventSource;
import com.xebia.devradar.domain.Workspace;
import com.xebia.devradar.pollers.Poller;
import com.xebia.devradar.pollers.git.GitHubPoller;
import com.xebia.devradar.pollers.hudson.HudsonPoller;
import com.xebia.devradar.pollers.jira.JiraPoller;
import com.xebia.devradar.pollers.svn.SvnPoller;


public class WorkspaceFactoryTest {

    private static final WorkspaceFactory factory = new WorkspaceFactory();
    
    private static final String VALID_GIT_SCM = "http://github.com/api/v2/xml/commits/list/xebia-france/dev-radar/master";
    private static final String VALID_SVN_SCM = "http://guava-libraries.googlecode.com/svn/trunk/";
    private static final String VALID_HUDSON_CI = "http://www.xebia.fr/hudson";
    private static final String VALID_JIRA_ISSUE = "http://issues.xebia.fr/jira";
    
    private Pom pom = new Pom();
    
    @Test
    public void should_create_workspace_with_svn_poller() {
        pom.setScm(VALID_SVN_SCM);
        assertWorkspaceContainsEventSource(VALID_SVN_SCM, SvnPoller.class);
    }
    
    @Test
    public void should_create_workspace_with_git_poller() {
        pom.setScm(VALID_GIT_SCM);
        assertWorkspaceContainsEventSource(VALID_GIT_SCM, GitHubPoller.class);
    }

    @Test
    public void should_create_workspace_with_hudson_poller() {
        pom.setCiManagement(VALID_HUDSON_CI);
        assertWorkspaceContainsEventSource(VALID_HUDSON_CI, HudsonPoller.class);
    }
    
    @Test
    public void should_create_workspace_with_jira_poller() {
        pom.setIssueManagement(VALID_JIRA_ISSUE);
        assertWorkspaceContainsEventSource(VALID_JIRA_ISSUE, JiraPoller.class);
    }
    
    private void assertWorkspaceContainsEventSource(String eventSourceUrl, Class<? extends Poller> eventSourcePoller) {
        Workspace workspace = factory.create(pom);
        Set<EventSource> eventSources = workspace.getEventSources();
        EventSource eventSource = eventSources.iterator().next();
        assertEquals(eventSourceUrl, eventSource.getUrl().toString());
        assertEquals(eventSourcePoller, eventSource.getPollerDescriptor().getPollerClass());
    }
}

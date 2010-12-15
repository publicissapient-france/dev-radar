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

import org.springframework.util.StringUtils;

import com.xebia.devradar.domain.EventSource;
import com.xebia.devradar.domain.Workspace;
import com.xebia.devradar.pollers.DefaultPollerProvider;
import com.xebia.devradar.pollers.Poller;
import com.xebia.devradar.pollers.git.GitHubPoller;
import com.xebia.devradar.pollers.hudson.HudsonPoller;
import com.xebia.devradar.pollers.jira.JiraPoller;
import com.xebia.devradar.pollers.svn.SvnPoller;

public class WorkspaceFactory {

    private static final DefaultPollerProvider POLLER_PROVIDERS = new DefaultPollerProvider();
    
    public Workspace create(Pom pom) {
        Workspace workspace = new Workspace();
        addEventSources(workspace, pom);
        return workspace;
    }

    private void addEventSources(Workspace workspace, Pom pom) {
        EventSource scmEventSource = findEventSource(workspace, pom.getScm());
        EventSource ciManagement = findEventSource(workspace, pom.getCiManagement());
        EventSource issueManagement = findEventSource(workspace, pom.getIssueManagement());
        
        addEventSources(workspace, ciManagement);
        addEventSources(workspace, scmEventSource);
        addEventSources(workspace, issueManagement);
    }

    private void addEventSources(Workspace workspace, EventSource ... eventSources) {
        for (EventSource source:eventSources) {
            if (null != source) {
                workspace.addEventSource(source);
            }
        }
    }

    private EventSource findEventSource(Workspace workspace, String url) {
        if (StringUtils.hasLength(url)) {
            Class<? extends Poller> pollerClass = findPollerClassFromUrl(url);
            if (pollerClass != null) {
                EventSource source = new EventSource();
                source.setPollerDescriptor(POLLER_PROVIDERS.getPollerDescriptor(pollerClass));
                source.setUrl(url);
                return source;
            }
        }

        return null;
    }

    private Class<? extends Poller> findPollerClassFromUrl(String url) {
        Class<? extends Poller> poller = null;
        if (isSubversionScm(url)) {
            poller = SvnPoller.class;
        } else if (isGitHubScm(url)) {
            poller = GitHubPoller.class;
        } else if (isHudsonCiManagement(url)) {
            poller = HudsonPoller.class;
        } else if (isJiraIssueManagement(url)) {
            poller = JiraPoller.class;
        }
        return poller;
    }

    private boolean isSubversionScm(String scm) {
        return scm.contains("svn");
    }
    
    private boolean isGitHubScm(String scm) {
        return scm.contains("git");
    }
    
    private boolean isHudsonCiManagement(String ciManagement) {
        return ciManagement.contains("hudson");
    }
    
    private boolean isJiraIssueManagement(String issueManagement) {
        return issueManagement.contains("jira");
    }
}

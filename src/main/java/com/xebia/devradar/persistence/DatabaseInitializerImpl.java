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
package com.xebia.devradar.persistence;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.xebia.devradar.domain.Event;
import com.xebia.devradar.domain.EventSource;
import com.xebia.devradar.domain.Workspace;
import com.xebia.devradar.pollers.hudson.HudsonPoller;
import com.xebia.devradar.pollers.jira.JiraPoller;
import com.xebia.devradar.pollers.svn.SvnPoller;

/**
 * @author Alexandre Dutra
 */
public class DatabaseInitializerImpl implements DatabaseInitializer {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void initDatabase() {

        @SuppressWarnings("unchecked")
        final
        List<Workspace> results = this.entityManager
        .createNamedQuery("workspaceByName")
        .setParameter("name", "default").getResultList();

        if (results.isEmpty()) {

            final Workspace defaultWorkspace = new Workspace();
            defaultWorkspace.setName("default");

            EventSource hudsonSource;
            EventSource svnSource;
            EventSource jiraSource;
            try {
                hudsonSource = new EventSource(HudsonPoller.class, "Hudson Job for Foo Project (trunk)", new URL("http://hudson.example/com"));
                svnSource = new EventSource(SvnPoller.class, "Trunk of Foo Project", new URL("http://svn.example/com"));
                jiraSource = new EventSource(JiraPoller.class, "JIRA Issues for Foo Project", new URL("http://jira.example/com"));
            } catch (final MalformedURLException e) {
                throw new IllegalStateException(e);
            }

            defaultWorkspace.addEventSource(hudsonSource);
            defaultWorkspace.addEventSource(svnSource);
            defaultWorkspace.addEventSource(jiraSource);

            final Event hudsonEvent = new Event(hudsonSource, "Build #58 failed!", new Date());
            defaultWorkspace.addEvent(hudsonEvent);
            final Event svnEvent = new Event(svnSource, "User Joe commited Service.java", new Date());
            defaultWorkspace.addEvent(svnEvent);
            final Event jiraEvent = new Event(jiraSource, "User Joe closed Jira FOO-123", new Date());
            defaultWorkspace.addEvent(jiraEvent);

            this.entityManager.persist(defaultWorkspace);

        }

    }
}

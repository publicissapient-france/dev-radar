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
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.xebia.devradar.domain.Event;
import com.xebia.devradar.domain.EventSource;
import com.xebia.devradar.domain.PollerDescriptor;
import com.xebia.devradar.domain.Workspace;
import com.xebia.devradar.pollers.PollerServiceLocator;
import com.xebia.devradar.pollers.hudson.HudsonPoller;
import com.xebia.devradar.pollers.jira.JiraPoller;
import com.xebia.devradar.pollers.sonar.SonarPoller;
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
            
            Set<PollerDescriptor> supportedPollers = PollerServiceLocator.getSupportedPollers();
            for (PollerDescriptor pollerDescriptor : supportedPollers) {
                
                entityManager.persist(pollerDescriptor);
                
                EventSource source = null;
                Event event = null;
                try {
                    if(pollerDescriptor.getPollerClass().equals(HudsonPoller.class)){
                        source = new EventSource(pollerDescriptor, new URL("http://hudson.example.com/jobs/foo"), "Hudson Job for trunk of project Foo");
                        event = new Event(source, "Build #58 failed!", new Date());
                    } else if(pollerDescriptor.getPollerClass().equals(SvnPoller.class)){
                        source = new EventSource(pollerDescriptor, new URL("http://hudson.example.com/foo/trunk"), "Subversion repository of project Foo (trunk)");
                        event = new Event(source, "User Joe commited Service.java", new Date());
                    } else if(pollerDescriptor.getPollerClass().equals(JiraPoller.class)){
                        source = new EventSource(pollerDescriptor, new URL("http://jira.example.com/foo"), "JIRA issues for project Foo");
                        event = new Event(source, "User Joe closed Jira FOO-123", new Date());
                    } else if(pollerDescriptor.getPollerClass().equals(SonarPoller.class)){
                        source = new EventSource(pollerDescriptor, new URL("http://sonar.example.com/foo"), "Sonar metrics for project Foo");
                        event = new Event(source, "Code coverage alert < 40%", new Date());
                    }
                } catch (MalformedURLException e) {
                    throw new IllegalStateException(e);
                }
                if(source != null){
                    defaultWorkspace.addEventSource(source);
                    defaultWorkspace.addEvent(event);
                } else {
                    //TODO warning
                }
            }

            this.entityManager.persist(defaultWorkspace);

        }

    }
}

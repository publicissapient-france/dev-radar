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
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.xebia.devradar.domain.EventSource;
import com.xebia.devradar.domain.PollerDescriptor;
import com.xebia.devradar.domain.Workspace;
import com.xebia.devradar.pollers.PollerServiceLocator;
import com.xebia.devradar.pollers.git.GitHubPoller;
import com.xebia.devradar.pollers.hudson.HudsonPoller;

/**
 * @author Alexandre Dutra
 */
public class DatabaseInitializerImpl implements DatabaseInitializer {

    private static final String DEVRADAR_WORKSPACE_NAME = "Dev Radar";


    private static final String DEV_RADAR_HUDSON_URL = "http://fluxx.fr.cr:9080/hudson/job/dev-radar/";


    private static final String DEV_RADAR_GIT_HUB_URL = "http://github.com/api/v2/xml/commits/list/xebia-france/dev-radar/master";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void initDatabase() {

        @SuppressWarnings("unchecked")
        final List<Workspace> results = this.entityManager
        .createNamedQuery("workspaceByName")
        .setParameter("name", DEVRADAR_WORKSPACE_NAME).getResultList();

        if (results.isEmpty()) {

            final Workspace defaultWorkspace = new Workspace();
            defaultWorkspace.setName(DEVRADAR_WORKSPACE_NAME);

            final Set<PollerDescriptor> supportedPollers = PollerServiceLocator.getSupportedPollers();
            for (final PollerDescriptor pollerDescriptor : supportedPollers) {
                this.entityManager.persist(pollerDescriptor);
            }

            try {
                Query query = this.entityManager.createQuery("from PollerDescriptor pd where pd.pollerClass = :pollerClass");
                query.setParameter("pollerClass", HudsonPoller.class);
                PollerDescriptor pollerDescriptor = (PollerDescriptor) query.getSingleResult();
                EventSource source = new EventSource(pollerDescriptor, new URL(DEV_RADAR_HUDSON_URL), "Hudson nightly build");
                defaultWorkspace.addEventSource(source);

                query = this.entityManager.createQuery("from PollerDescriptor pd where pd.pollerClass = :pollerClass");
                query.setParameter("pollerClass", GitHubPoller.class);
                pollerDescriptor = (PollerDescriptor) query.getSingleResult();
                source = new EventSource(pollerDescriptor, new URL(DEV_RADAR_GIT_HUB_URL), "GitHub master branch");
                defaultWorkspace.addEventSource(source);
            } catch (final MalformedURLException e) {
                //should not occur
            }

            this.entityManager.persist(defaultWorkspace);

        }

    }
}

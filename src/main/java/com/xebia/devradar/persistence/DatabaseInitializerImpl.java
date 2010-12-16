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

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private static final Log LOGGER = LogFactory.getLog(DatabaseInitializerImpl.class.getName());

    private static final String DEVRADAR_WORKSPACE_NAME = "Dev Radar";

    private static final String DEV_RADAR_HUDSON_URL = "http://fluxx.fr.cr:9080/hudson/job/dev-radar/";

    private static final String DEV_RADAR_GIT_HUB_URL = "http://github.com/api/v2/xml/commits/list/xebia-france/dev-radar/master";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void initDatabase() throws DatabaseInitializationException {

        LOGGER.info("-----------------------------------------------------------");
        LOGGER.info("Dev Radar Initializing Database");

        @SuppressWarnings("unchecked")
        final List<Workspace> results = this.entityManager
        .createNamedQuery("workspaceByName")
        .setParameter("name", DEVRADAR_WORKSPACE_NAME).getResultList();

        if (results.isEmpty()) {

            LOGGER.info("Dev Radar Database not yet configured: initializing");
            this.init();

        } else {

            LOGGER.info("Dev Radar Database already configured: updating");
            this.update();
        }

        LOGGER.info("Dev Radar Database successfully initialized");
        LOGGER.info("-----------------------------------------------------------");
    }

    private void init() {

        final Workspace defaultWorkspace = new Workspace();
        defaultWorkspace.setName(DEVRADAR_WORKSPACE_NAME);

        final Set<PollerDescriptor> supportedPollers = PollerServiceLocator.getSupportedPollers();
        for (final PollerDescriptor pollerDescriptor : supportedPollers) {
            this.entityManager.persist(pollerDescriptor);
        }

        Query query = this.entityManager.createQuery("from PollerDescriptor pd where pd.pollerClass = :pollerClass");
        query.setParameter("pollerClass", HudsonPoller.class);
        PollerDescriptor pollerDescriptor = (PollerDescriptor) query.getSingleResult();
        EventSource source = new EventSource(pollerDescriptor, DEV_RADAR_HUDSON_URL, "Hudson nightly build");
        defaultWorkspace.addEventSource(source);

        query = this.entityManager.createQuery("from PollerDescriptor pd where pd.pollerClass = :pollerClass");
        query.setParameter("pollerClass", GitHubPoller.class);
        pollerDescriptor = (PollerDescriptor) query.getSingleResult();
        source = new EventSource(pollerDescriptor, DEV_RADAR_GIT_HUB_URL, "GitHub master branch");
        defaultWorkspace.addEventSource(source);

        this.entityManager.persist(defaultWorkspace);
    }


    private void update() throws DatabaseInitializationException {
        @SuppressWarnings("unchecked")
        final
        List<PollerDescriptor> persistentPollers = this.entityManager.createQuery("from PollerDescriptor").getResultList();

        final Set<PollerDescriptor> supportedPollers = PollerServiceLocator.getSupportedPollers();
        for (final PollerDescriptor supportedPoller : supportedPollers) {
            boolean skip = false;
            for (final PollerDescriptor persistentPoller : persistentPollers) {
                if(persistentPoller.getPollerClass().equals(supportedPoller.getPollerClass())){
                    skip = true;
                    break;
                }
            }
            if(!skip){
                this.entityManager.persist(supportedPoller);
            }
        }

        for (final PollerDescriptor persistentPoller : persistentPollers) {
            boolean supported = false;
            for (final PollerDescriptor supportedPoller : supportedPollers) {
                if(persistentPoller.getPollerClass().equals(supportedPoller.getPollerClass())){
                    supported = true;
                    break;
                }
            }
            if(!supported){
                throw new DatabaseInitializationException("The following poller class cannot be instantiated: " + persistentPoller.getPollerClass().getName());
            }
        }
    }

}

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

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.xebia.devradar.domain.Event;
import com.xebia.devradar.domain.Workspace;

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
        List<Workspace> results = (List<Workspace>) entityManager
            .createNamedQuery("workspaceByName")
            .setParameter("name", "default").getResultList();

        if (results.isEmpty()) {

            Workspace defaultWorkspace = new Workspace();
            defaultWorkspace.setName("default");
            defaultWorkspace.addEvent(new Event("Example", "This is a dummy event!", new Date()));
            defaultWorkspace.addEvent(new Event("Example 2", "This is another dummy event!", new Date()));
            
            entityManager.persist(defaultWorkspace);

        }

    }
}

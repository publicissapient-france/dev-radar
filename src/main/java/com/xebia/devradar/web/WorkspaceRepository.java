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
package com.xebia.devradar.web;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.xebia.devradar.domain.Workspace;

/**
 * Repository for <code>{@link Workspace}</code> instances.
 * 
 */
@Repository
public class WorkspaceRepository {

    
    public WorkspaceRepository() {
        super();
    }

    public WorkspaceRepository(EntityManager entityManager) {
        this();
        this.entityManager = entityManager;
    }

    @PersistenceContext
    private EntityManager entityManager;

    public Workspace createWorkspace(Workspace workspace) {
        entityManager.persist(workspace);
        return workspace;
    }

    public void deleteWorkspace(final Workspace workspace) {
        entityManager.remove(getWorkspaceById(workspace.getId()));
    }
    
    public Workspace getWorkspaceByName(String name) {
        Query query = entityManager.createNamedQuery("workspaceByName");

        query.setParameter("name", name);
        return (Workspace) query.getSingleResult();
    }

    public Workspace findWorkspaceByName(String workspaceName) {
        return (Workspace) this.entityManager.createNamedQuery("workspaceByName").setParameter("name", workspaceName).getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public List<Workspace> getAll() {
        return this.entityManager.createQuery("from Workspace").getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Workspace> getAllWorkspaces() {
        Query query = entityManager.createNamedQuery("orderByName");
        return query.getResultList();
    }
    
    public Workspace getWorkspaceById(Long id) {
        return entityManager.find(Workspace.class, id);
    }
    
    public Workspace updateWorkspace(Workspace workspace) {
        return entityManager.merge(workspace);
    }
    
}

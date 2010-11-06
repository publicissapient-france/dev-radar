/**
 * Copyright (C) 2010 xebia-france <xebia-france@xebia.fr>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebia.devradar.web;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.springframework.stereotype.Repository;

import com.xebia.devradar.domain.Event;
import com.xebia.devradar.domain.Workspace;

/**
 * Repository for Event
 * 
 * @author Jean-Laurent de Morlhon
 */
@Repository
public class EventRepository {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public Set<Event> getEventsForWorkspace(String workspaceName) {

        Workspace workspace = (Workspace) entityManager
            .createNamedQuery("workspaceByName")
            .setParameter("name", workspaceName).getSingleResult();

        return workspace.getEvents();

    }
}

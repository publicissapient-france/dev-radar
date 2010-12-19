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
package com.xebia.devradar.domain.dao;

import com.xebia.devradar.domain.Badge;
import com.xebia.devradar.domain.BadgeType;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Repository for <code>{@link com.xebia.devradar.domain.BadgeType}</code> instances.
 *
 */
@Repository
public class BadgeTypeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<BadgeType> getAll() {
        return this.entityManager.createQuery("from BadgeType").getResultList();
    }

    public BadgeType getBadgeTypeById(Long badgeTypeId) {
        return this.entityManager.find(BadgeType.class, badgeTypeId);
    }

    public List<Long> getBadgeTypeIdsByWorkspaceId(Long workspaceId) {
        Query query = this.entityManager.createQuery("select bt.id from Badge b join b.badgeType bt where b.workspace.id = :workspaceId");

        query.setParameter("workspaceId", workspaceId);
        return query.getResultList();
    }
}

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
package com.xebia.devradar.badge;

import com.xebia.devradar.domain.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
public class BadgesOwnersRefresher implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @PersistenceContext
    private EntityManager entityManager;

    public void refreshBadgesToWorkspace(Workspace workspace) {
        for (Badge badge : workspace.getBadges()) {
            BadgeType badgeType = badge.getBadgeType();
            Long profilId;

            if (badge.getBadgeType().getDslQuery() != null) {
                profilId = refreshWithDsl(workspace, badgeType);
            } else {
                profilId = refreshWithOwnerFinder(workspace, badgeType);
            }
            if (profilId != null) {
                Profil profil = entityManager.find(Profil.class, profilId);

                if (profil == null) {
                    throw new IllegalStateException("the dsl query doesn't return a valid profil.");
                }
                badge.setProfil(profil);
            } else {
                badge.setProfil(null);
            }
        }
    }

    private Long refreshWithOwnerFinder(Workspace workspace, BadgeType badgeType) {
        BadgeOwnerFinder badgeOwnerFinder = (BadgeOwnerFinder) BeanFactoryUtils.beanOfType(applicationContext, badgeType.getOwnerFinderClass());

        return badgeOwnerFinder.findBadgeOwnerForWorkspace(workspace.getId());
    }

    private Long refreshWithDsl(Workspace workspace, BadgeType badgeType) {
        Query query = entityManager.createQuery(badgeType.getDslQuery());
        for (DslParameter dslParameter : badgeType.getDslParameters()) {
            query.setParameter(dslParameter.getName(), dslParameter.getType().getObjectValue(dslParameter));
        }
        query.setParameter("workspaceId", workspace.getId());
        List<Long> profilIds = query.setMaxResults(1).getResultList();

        if (profilIds.size() == 0) {
            return null;
        } else {
            return profilIds.get(0);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

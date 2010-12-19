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
package com.xebia.devradar.domain;

import com.xebia.devradar.badge.BadgeOwnerFinder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.List;

@Configurable
@Entity
public class BadgeType extends AbstractEntity  implements ApplicationContextAware, InitializingBean {

    private static final Log LOGGER = LogFactory.getLog(BadgeType.class);

    @Transient
    @PersistenceContext
    private EntityManager entityManager;

    @Transient
    private ApplicationContext applicationContext;

    @Basic(optional = false)
    @Column(unique = true)
    private String name;

    private String dslQuery;

    private Class ownerFinderClass;

    public BadgeType() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDslQuery() {
        return dslQuery;
    }

    public void setDslQuery(String dslQuery) {
        this.dslQuery = dslQuery;
    }

    public Class getOwnerFinderClass() {
        return ownerFinderClass;
    }

    public void setOwnerFinderClass(Class ownerFinderClass) {
        ownerFinderClass = ownerFinderClass;
    }

    public void create() {
        entityManager.persist(this);
    }

    public void update() {
        entityManager.merge(this);
    }

    public void delete() {
        entityManager.remove(this);
    }

    public String getBadgeOwnerOfWorkspace(Workspace workspace) {
        String gravatarUrl = null;

        if (getDslQuery() != null) {
            gravatarUrl = refreshWithDsl(workspace);
        } else {
            gravatarUrl = refreshWithOwnerFinder(workspace);
        }
        return gravatarUrl;
    }

    private String refreshWithOwnerFinder(Workspace workspace) {
        BadgeOwnerFinder badgeOwnerFinder = (BadgeOwnerFinder) BeanFactoryUtils.beanOfType(applicationContext, getOwnerFinderClass());

        return badgeOwnerFinder.findBadgeOwnerForWorkspace(workspace.getId());
    }

    private String refreshWithDsl(Workspace workspace) {
        Query query = entityManager.createQuery(getDslQuery());

        query.setParameter("workspaceId", workspace.getId());
        List<String> gravatarUrls = query.setMaxResults(1).getResultList();


        if (gravatarUrls.size() == 0) {
            return null;
        } else {
            return gravatarUrls.get(0);
        }
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(entityManager);
        Assert.notNull(applicationContext);
    }
}

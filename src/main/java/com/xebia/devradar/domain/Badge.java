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

import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.*;

@Configurable
@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"BADGETYPE_ID", "WORKSPACE_ID"}))
public class Badge extends AbstractEntity {

    @Transient
    @PersistenceContext
    private EntityManager entityManager;

    @ManyToOne(optional = false)
    private BadgeType badgeType;

    private String gravatarUrl;

    @ManyToOne(optional = false)
    private Workspace workspace;

    public Badge() {

    }

    public Badge(BadgeType badgeType) {
        this.badgeType = badgeType;
    }

    public BadgeType getBadgeType() {
        return badgeType;
    }

    public void setBadgeType(BadgeType badgeType) {
        this.badgeType = badgeType;
    }

    public String getGravatarUrl() {
        return gravatarUrl;
    }

    public void setGravatarUrl(String gravatarUrl) {
        this.gravatarUrl = gravatarUrl;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
        workspace.internalAddBadge(this);
    }

    public void refreshBadgeOwner() {
        setGravatarUrl(badgeType.getBadgeOwnerOfWorkspace(workspace));
    }

    public void delete() {
        entityManager.remove(this);
    }

    public void create() {
        entityManager.persist(this);
    }
}

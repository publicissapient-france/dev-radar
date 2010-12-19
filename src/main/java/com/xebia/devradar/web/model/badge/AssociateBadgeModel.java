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
package com.xebia.devradar.web.model.badge;

import com.xebia.devradar.domain.BadgeType;

import java.util.List;

public class AssociateBadgeModel {

    public static final String NAME = "badges/form";
    public static final String ASSOCIATE_BADGE_MODEL = "associateBadgeModel";
    public static final String WORKSPACE_ID = "workspaceId";

    private Long[] badgeTypeIds;

    private Long workspaceId;

    private List<BadgeType> badgeTypes;

    public Long[] getBadgeTypeIds() {
        return badgeTypeIds;
    }

    public void setBadgeTypeIds(Long[] badgeTypeIds) {
        this.badgeTypeIds = badgeTypeIds;
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

    public List<BadgeType> getBadgeTypes() {
        return badgeTypes;
    }

    public void setBadgeTypes(List<BadgeType> badgeTypes) {
        this.badgeTypes = badgeTypes;
    }
}

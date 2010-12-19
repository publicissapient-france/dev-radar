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
package com.xebia.devradar.web.controller;

import com.xebia.devradar.domain.Badge;
import com.xebia.devradar.domain.BadgeType;
import com.xebia.devradar.domain.Workspace;
import com.xebia.devradar.domain.dao.BadgeTypeRepository;
import com.xebia.devradar.web.WorkspaceRepository;
import com.xebia.devradar.web.model.badge.AssociateBadgeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Controller
@RequestMapping("/workspaces/{workspaceId}/badges")
@SessionAttributes({"associateBadgeModel"})
@Transactional
public class BadgesController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private BadgeTypeRepository badgeTypeRepository;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    public BadgesController(BadgeTypeRepository badgeTypeRepository, WorkspaceRepository workspaceRepository) {
        this.badgeTypeRepository = badgeTypeRepository;
        this.workspaceRepository = workspaceRepository;
    }

    public BadgesController() {
    }

    @RequestMapping(value = "associate", method = RequestMethod.GET)
    public String initCreateBadgeTypeForm(@PathVariable(AssociateBadgeModel.WORKSPACE_ID) Long workspaceId, Model model) {
        AssociateBadgeModel associateBadgeModel = new AssociateBadgeModel();

        associateBadgeModel.setBadgeTypes(badgeTypeRepository.getAll());
        associateBadgeModel.setWorkspaceId(workspaceId);
        associateBadgeModel.setBadgeTypeIds(badgeTypeRepository.getBadgeTypeIdsByWorkspaceId(workspaceId).toArray(new Long[] {}));
        model.addAttribute(AssociateBadgeModel.ASSOCIATE_BADGE_MODEL, associateBadgeModel);
        return AssociateBadgeModel.NAME;
    }

    @RequestMapping(value = "associate", method = RequestMethod.POST)
    public String associateBadges(@ModelAttribute(AssociateBadgeModel.ASSOCIATE_BADGE_MODEL) AssociateBadgeModel associateBadgeModel, SessionStatus status) {
        Workspace workspace = workspaceRepository.getWorkspaceById(associateBadgeModel.getWorkspaceId());

        workspace.updateBadges(associateBadgeModel.getBadgeTypeIds());

        status.setComplete();
        return "redirect:/workspaces/" + workspace.getId() + ".html";
    }
}

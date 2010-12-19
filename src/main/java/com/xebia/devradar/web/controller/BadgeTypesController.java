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

import com.xebia.devradar.domain.BadgeType;
import com.xebia.devradar.domain.Profile;
import com.xebia.devradar.domain.dao.BadgeTypeRepository;
import com.xebia.devradar.web.WorkspaceRepository;
import com.xebia.devradar.web.model.badge.type.BadgeTypeDeleteModel;
import com.xebia.devradar.web.model.badge.type.BadgeTypeListModel;
import com.xebia.devradar.web.model.badge.type.BadgeTypeFormModel;
import com.xebia.devradar.web.model.badge.type.BadgeTypeShowModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/badgeTypes")
@SessionAttributes({"badgeType", "badgeTypeFormModel"})
@Transactional
public class BadgeTypesController {

    @Autowired
    private BadgeTypeRepository badgeTypeRepository;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    private static final String DEFAULT_DSL_QUERY = "select e.profile.id from Event e where e.workspace.id = :workspaceId and e.eventType = 'COMMIT' group by e.profile.id order by count(e.id) desc";

    public BadgeTypesController(BadgeTypeRepository badgeTypeRepository, WorkspaceRepository workspaceRepository) {
        this.badgeTypeRepository = badgeTypeRepository;
        this.workspaceRepository = workspaceRepository;
    }

    public BadgeTypesController() {
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView listBadgeTypes() {
        List<BadgeType> badgeTypes = badgeTypeRepository.getAll();
        ModelAndView mav = new ModelAndView(BadgeTypeListModel.NAME);

        mav.addObject(BadgeTypeListModel.BADGE_TYPES, badgeTypes);
        return mav;
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String initCreateBadgeTypeForm(Model model) {
        BadgeTypeFormModel badgeTypeFormModel = new BadgeTypeFormModel();

        badgeTypeFormModel.setBadgeType(new BadgeType());
        badgeTypeFormModel.setWorkspaces(workspaceRepository.getAll());
        badgeTypeFormModel.getBadgeType().setDslQuery(DEFAULT_DSL_QUERY);
        model.addAttribute(BadgeTypeFormModel.BADGE_TYPE_FORM_MODEL, badgeTypeFormModel);
        return BadgeTypeFormModel.NAME;
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String createBadgeType(@ModelAttribute(BadgeTypeFormModel.BADGE_TYPE_FORM_MODEL) BadgeTypeFormModel badgeTypeFormModel, BindingResult result, SessionStatus status) {
        if (testBadgeOwner(badgeTypeFormModel, result)) {
            return BadgeTypeFormModel.NAME;
        } else {
            badgeTypeFormModel.getBadgeType().create();
            status.setComplete();
            return "redirect:/badgeTypes/" + badgeTypeFormModel.getBadgeType().getId()+".html";
        }
    }

    @RequestMapping(value = "/{" + BadgeTypeFormModel.BADGE_TYPE_ID + "}/edit", method = RequestMethod.GET)
    public String initEditBadgeTypeForm(@PathVariable(BadgeTypeFormModel.BADGE_TYPE_ID) Long badgeTypeId, Model model) {
        BadgeTypeFormModel badgeTypeFormModel = new BadgeTypeFormModel();
        BadgeType badgeType = badgeTypeRepository.getBadgeTypeById(badgeTypeId);

        badgeTypeFormModel.setBadgeType(badgeType);
        badgeTypeFormModel.setWorkspaces(workspaceRepository.getAll());
        model.addAttribute(BadgeTypeFormModel.BADGE_TYPE_FORM_MODEL, badgeTypeFormModel);
        return BadgeTypeFormModel.NAME;
    }

    @RequestMapping(value = "/{" + BadgeTypeFormModel.BADGE_TYPE_ID + "}/edit", method = { RequestMethod.PUT, RequestMethod.POST })
    public String editBadgeType(@ModelAttribute(BadgeTypeFormModel.BADGE_TYPE_FORM_MODEL) BadgeTypeFormModel badgeTypeFormModel, BindingResult result,
                                SessionStatus status) {
        if (result.hasErrors()) {
            return BadgeTypeFormModel.NAME;
        } else {
            if (testBadgeOwner(badgeTypeFormModel, result)) {
                return BadgeTypeFormModel.NAME;
            } else {
                badgeTypeFormModel.getBadgeType().update();
                status.setComplete();
                return "redirect:/badgeTypes/" + badgeTypeFormModel.getBadgeType().getId() + ".html";
            }
        }
    }

    private Boolean testBadgeOwner(BadgeTypeFormModel badgeTypeFormModel, BindingResult result) {
        if (badgeTypeFormModel.getTestMode()) {
            try {
                Profile profile = badgeTypeFormModel.getBadgeType().getBadgeOwnerOfWorkspace(workspaceRepository.getWorkspaceById(badgeTypeFormModel.getWorkspaceId()));
                badgeTypeFormModel.setProfile(profile);
            } catch (DataAccessException e) {
                result.addError(new FieldError(BadgeTypeFormModel.BADGE_TYPE_FORM_MODEL, "profile", e.getMessage()));
            }
            return true;
        }
        return false;
    }

    @RequestMapping(value = "/{" + BadgeTypeShowModel.BADGE_TYPE_ID + "}", method = RequestMethod.GET)
    public String showBadgeType(@PathVariable(BadgeTypeShowModel.BADGE_TYPE_ID) Long badgeTypeId, Model model) {
        BadgeType badgeType = badgeTypeRepository.getBadgeTypeById(badgeTypeId);

        model.addAttribute(BadgeTypeShowModel.BADGE_TYPE, badgeType);
        return BadgeTypeShowModel.NAME;
    }

    @RequestMapping(value = "/{" + BadgeTypeShowModel.BADGE_TYPE_ID + "}/delete", method = { RequestMethod.PUT, RequestMethod.POST })
    @Transactional(readOnly=false)
    public String deleteBadgeType(@PathVariable(BadgeTypeDeleteModel.BADGE_TYPE_ID) Long badgeTypeId, SessionStatus status) {
        BadgeType badgeType = badgeTypeRepository.getBadgeTypeById(badgeTypeId);

        badgeType.delete();
        status.setComplete();
        return "redirect:/badgeTypes/list.html";
    }
}

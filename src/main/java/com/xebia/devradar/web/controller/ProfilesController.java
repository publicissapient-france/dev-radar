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


import com.xebia.devradar.domain.Profile;
import com.xebia.devradar.web.ProfileRepository;
import com.xebia.devradar.web.model.ProfileListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/profiles")
@SessionAttributes("profile")
@Transactional
public class ProfilesController {

    private ProfileRepository profileRepository;

    public ProfilesController() {

    }

    @Autowired
    public ProfilesController(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String initCreateProfileForm(Model model) {
        Profile profile = new Profile();
        model.addAttribute("profile", profile);
        return "profiles/form";
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    @Transactional(readOnly = false)
    public String createProfile(@ModelAttribute("profile") @Valid Profile profile, BindingResult result, SessionStatus status) {
        if (result.hasErrors()) {
            return "profiles/form";
        } else {
            profileRepository.createProfile(profile);
            status.setComplete();
            return "redirect:/profiles/" + profile.getId()+".html";
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public ModelAndView listProfiles() {
        List<Profile> profiles = profileRepository.getAllProfiles();
        ModelAndView mav = new ModelAndView(ProfileListModel.NAME);
        mav.addObject(ProfileListModel.PROFILES, profiles);
        return mav;
    }

    @RequestMapping(value = "/{profileId}", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String showProfile(@PathVariable("profileId") final Long profileId, final Model model) {
        final Profile profile = this.profileRepository.getProfileById(profileId);
        model.addAttribute("profile", profile);
        return "profiles/show";
    }

    @RequestMapping(value = "/{profileId}/delete", method = { RequestMethod.PUT, RequestMethod.POST })
    @Transactional(readOnly = false)
    public String deleteProfile(@ModelAttribute("profile") final Profile profile, final SessionStatus status) {
        this.profileRepository.deleteProfile(profile);
        status.setComplete();
        return "redirect:/profiles/list.html";
    }

    @RequestMapping(value = "/{profileId}/edit", method = RequestMethod.GET)
    public String initEditProfileForm(@PathVariable("profileId") Long profileId, Model model) {
        Profile profile = this.profileRepository.getProfileById(profileId);
        model.addAttribute("profile", profile);
        return "profiles/form";
    }

    @RequestMapping(value = "/{profileId}/edit", method = { RequestMethod.PUT, RequestMethod.POST })
    public String editProfile(@ModelAttribute("profile") @Valid Profile profile, BindingResult result,
                              SessionStatus status) {
        if (result.hasErrors()) {
            return "profiles/form";
        } else {
            this.profileRepository.updateProfile(profile);
            status.setComplete();
            return "redirect:/profiles/" + profile.getId() + ".html";
        }
    }
}

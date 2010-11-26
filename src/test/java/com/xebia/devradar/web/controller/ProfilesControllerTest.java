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
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.support.SimpleSessionStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProfilesControllerTest {

    private ProfileRepository profileRepository;

    private ProfilesController profilesController;

    @Before
    public void init() {
        profileRepository = mock(ProfileRepository.class);
        profilesController = new ProfilesController(profileRepository);
    }

    @Test
    public void should_init_create_profile_model() {
        Model model = new ExtendedModelMap();

        String view = profilesController.initCreateProfileForm(model);

        assertThat(model.asMap().get("profile") instanceof Profile, is(true));
        assertThat(view, is("profiles/form"));
    }

    @Test
    public void should_create_profile() throws Exception {
        Profile profile = getProfile();
        BindingResult result = new MapBindingResult(new HashMap<Object, Object>(), "bindingResult");
        SessionStatus status = new SimpleSessionStatus();

        when(profileRepository.createProfile(profile)).thenReturn(profile);

        String view = profilesController.createProfile(profile, result, status);

        assertThat(status.isComplete(), is(true));
        assertThat(view, is("redirect:/profiles/" + profile.getId()+".html"));
    }

    @Test
    public void should_init_edit_profile_model() {
        Profile profile = getProfile();
        Model model = new ExtendedModelMap();

        when(profileRepository.getProfileById(profile.getId())).thenReturn(profile);

        String view = profilesController.initEditProfileForm(profile.getId(), model);

        assertThat((Profile) model.asMap().get("profile"), is(profile));
        assertThat(view, is("profiles/form"));
    }

    @Test
    public void should_edit_profile() throws Exception {
        Profile profile = getProfile();
        BindingResult result = new MapBindingResult(new HashMap<Object, Object>(), "bindingResult");
        SessionStatus status = new SimpleSessionStatus();

        when(profileRepository.updateProfile(profile)).thenReturn(profile);

        String view = profilesController.editProfile(profile, result, status);

        assertThat(status.isComplete(), is(true));
        assertThat(view, is("redirect:/profiles/" + profile.getId() + ".html"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_return_all_profiles() {
        when(profileRepository.getAllProfiles()).thenReturn(someProfilesList());

        ModelAndView mav = profilesController.listProfiles();
        assertThat(mav.getViewName(), equalTo(ProfileListModel.NAME));

        List<Profile> profiles = (List<Profile>) mav.getModel().get(ProfileListModel.PROFILES);
        assertThat(profiles, notNullValue());
        assertThat(profiles.size(), is(2));
    }

    @Test
    public void should_show_profile() throws Exception {
        Model model = new ExtendedModelMap();
        Profile profile = getProfile();

        when(profileRepository.getProfileById(profile.getId())).thenReturn(profile);

        String view = profilesController.showProfile(profile.getId(), model);

        assertThat(model.asMap().get("profile") instanceof Profile, is(true));
        assertThat(view, is("profiles/show"));
    }

    @Test
    public void should_delete_profile() throws Exception {
        Model model = new ExtendedModelMap();
        Profile profile = getProfile();
        SessionStatus status = new SimpleSessionStatus();

        when(profileRepository.getProfileById(profile.getId())).thenReturn(profile);

        String view = profilesController.deleteProfile(profile, status);

        assertThat(status.isComplete(), is(true));
        assertThat(view, is("redirect:/profiles/list.html"));
    }

    private Profile getProfile() {
        return new Profile("blebens", "beau@dentedreality.com.au");
    }

    private List<Profile> someProfilesList() {
        List<Profile> profiles = new ArrayList<Profile>();
        profiles.add(new Profile());
        profiles.add(new Profile());
        return profiles;
    }
}

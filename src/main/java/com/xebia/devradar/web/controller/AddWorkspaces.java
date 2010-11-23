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


import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.xebia.devradar.domain.Workspace;
import com.xebia.devradar.utils.Pom;
import com.xebia.devradar.utils.PomLoaderUtils;
import com.xebia.devradar.utils.WorkspaceFactory;
import com.xebia.devradar.validation.WorkspaceValidator;
import com.xebia.devradar.web.WorkspaceRepository;

@Controller
@RequestMapping("/workspaces/new")
@SessionAttributes("workspace")
@Transactional
public class AddWorkspaces {

    private WorkspaceRepository workspaceRepository;

    private WorkspaceFactory workspaceFactory = new WorkspaceFactory();
    
    public AddWorkspaces() {
        
    }
    
    @Autowired
    public AddWorkspaces(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String setupForm(Model model) {
        Workspace workspace = new Workspace();
        model.addAttribute("workspace", workspace);
        return "workspaces/form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processSubmit(@ModelAttribute("workspace") Workspace workspace, BindingResult result, SessionStatus status) {
        new WorkspaceValidator().validate(workspace, result);
        if (result.hasErrors()) {
            return "workspaces/form";
        } else {
            createWorkspace(workspace, result);           
            workspaceRepository.createWorkspace(workspace);
            status.setComplete();
            return "redirect:/workspaces/" + workspace.getId()+".html";
        }
    }

    private void createWorkspace(Workspace workspace, BindingResult result) {
        String customName = workspace.getName();
        String customDescription = workspace.getDescription();
        createWorkspaceFromPom(workspace, result);
        if (StringUtils.hasLength(customName)) {
            workspace.setName(customName);
        }
        if (StringUtils.hasLength(customDescription)) {
            workspace.setDescription(customDescription);
        }
    }

    private void createWorkspaceFromPom(Workspace workspace, BindingResult result) {
        if (StringUtils.hasLength(workspace.getPomUrl())) {
            try {
                Pom pom = PomLoaderUtils.create(new URL(workspace.getPomUrl()));
                workspace = workspaceFactory.create(pom);
            } catch (Exception e) {
                result.rejectValue("pomUrl", "invalid-url", "invalid url");
            }
        }
    }
}

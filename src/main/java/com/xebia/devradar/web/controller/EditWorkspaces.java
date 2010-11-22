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

import com.xebia.devradar.web.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.xebia.devradar.domain.Workspace;
import com.xebia.devradar.validation.WorkspaceValidator;

@Controller
@RequestMapping("/workspaces/{workspaceId}/edit")
@SessionAttributes("workspace")
@Transactional
public class EditWorkspaces {

    private WorkspaceRepository workspaceRepository;

    public EditWorkspaces() {
        
    }
    
    @Autowired
    public EditWorkspaces(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String setupForm(@PathVariable("workspaceId") Long workspaceId, Model model) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        model.addAttribute("workspace", workspace);
        return "workspaces/form";
    }

    @RequestMapping(method = { RequestMethod.PUT, RequestMethod.POST })
    public String processSubmit(@ModelAttribute("workspace") Workspace workspace, BindingResult result,
            SessionStatus status) {
        new WorkspaceValidator().validate(workspace, result);
        if (result.hasErrors()) {
            return "workspaces/form";
        } else {
            workspaceRepository.updateWorkspace(workspace);
            status.setComplete();
            return "redirect:/workspaces/" + workspace.getId() + ".html";
        }
    }
}

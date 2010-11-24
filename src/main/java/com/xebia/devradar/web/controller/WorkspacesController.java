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

import com.xebia.devradar.domain.Event;
import com.xebia.devradar.domain.Workspace;
import com.xebia.devradar.pollers.PollException;
import com.xebia.devradar.utils.Pom;
import com.xebia.devradar.utils.PomLoaderUtils;
import com.xebia.devradar.utils.WorkspaceFactory;
import com.xebia.devradar.validation.WorkspaceValidator;
import com.xebia.devradar.web.WorkspaceRepository;
import com.xebia.devradar.web.model.WorkspaceListModel;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import java.net.URL;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/workspaces")
@SessionAttributes("workspace")
@Transactional
public class WorkspacesController {

    @Autowired
    private WorkspaceRepository workspaceRepository;

    private WorkspaceFactory workspaceFactory = new WorkspaceFactory();

    public WorkspacesController() {
    }

    public WorkspacesController(WorkspaceRepository workspaceRepository, WorkspaceFactory workspaceFactory) {
        this.workspaceRepository = workspaceRepository;
        this.workspaceFactory = workspaceFactory;
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String initCreateWorkspaceForm(Model model) {
        Workspace workspace = new Workspace();
        model.addAttribute("workspace", workspace);
        return "workspaces/form";
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String createWorkspace(@ModelAttribute("workspace") Workspace workspace, BindingResult result, SessionStatus status) {
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

    @RequestMapping(value = "/{workspaceId}/edit", method = RequestMethod.GET)
    public String initEditWorkspaceForm(@PathVariable("workspaceId") Long workspaceId, Model model) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        model.addAttribute("workspace", workspace);
        return "workspaces/form";
    }

    @RequestMapping(value = "/{workspaceId}/edit", method = { RequestMethod.PUT, RequestMethod.POST })
    public String editWorkspace(@ModelAttribute("workspace") Workspace workspace, BindingResult result,
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

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView listWorkspaces() {
        List<Workspace> workspaces = workspaceRepository.getAllWorkspaces();
        ModelAndView mav = new ModelAndView(WorkspaceListModel.NAME);
        mav.addObject(WorkspaceListModel.WORKSPACES, workspaces);
        return mav;
    }

    @RequestMapping(value = "/{workspaceId}", method = RequestMethod.GET)
    public String showWorkspace(@PathVariable("workspaceId") final Long workspaceId, final Model model) {
        final Workspace workspace = this.workspaceRepository.getWorkspaceById(workspaceId);
        model.addAttribute("workspace", workspace);
        return "workspaces/show";
    }

    @RequestMapping(value = "/{workspaceId}/delete", method = { RequestMethod.PUT, RequestMethod.POST })
    @Transactional(readOnly=false)
    public String deleteWorkspace(@ModelAttribute("workspace") final Workspace workspace, final SessionStatus status) {
        this.workspaceRepository.deleteWorkspace(workspace);
        status.setComplete();
        return "redirect:/workspaces/list.html";
    }

    @RequestMapping(value = "/{workspaceId}/poll", method = RequestMethod.GET)
    public String poll(@PathVariable("workspaceId") Long workspaceId, Model model) throws PollException {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        Date end = new Date();
        Date start = DateUtils.addDays(end, -7);
        List<Event> events = workspace.poll(start, end);
        for (Event event : events) {
            workspace.addEvent(event);
        }
        model.addAttribute("workspace", workspace);
        return "workspaces/show";
    }
}

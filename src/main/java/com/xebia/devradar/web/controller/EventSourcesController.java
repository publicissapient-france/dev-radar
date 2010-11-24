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

import com.xebia.devradar.domain.EventSource;
import com.xebia.devradar.domain.PollerDescriptor;
import com.xebia.devradar.domain.Workspace;
import com.xebia.devradar.web.EventSourceRepository;
import com.xebia.devradar.web.PollerDescriptorRepository;
import com.xebia.devradar.web.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.beans.PropertyEditorSupport;
import java.util.List;

@Controller
@RequestMapping("/workspaces/{workspaceId}/eventSources")
@SessionAttributes("eventSource")
@Transactional
public class EventSourcesController {

    @Autowired
    private PollerDescriptorRepository pollerDescriptorRepository;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private EventSourceRepository eventSourceRepository;

    public EventSourcesController() {
    }

    @InitBinder
    protected void initBinder(final ServletRequestDataBinder binder) {
        binder.registerCustomEditor(PollerDescriptor.class, new PropertyEditorSupport(){
            @Override
            public void setAsText(final String text) throws IllegalArgumentException {
                final PollerDescriptor pd = EventSourcesController.this.pollerDescriptorRepository.getPollerDescriptorById(Long.parseLong(text));
                this.setValue(pd);
            }

        });
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String initCreateEventSourceForm(final Model model) {
        final EventSource eventSource = new EventSource();
        model.addAttribute("eventSource", eventSource);
        final List<PollerDescriptor> pollerDescriptors = this.pollerDescriptorRepository.getAll();
        model.addAttribute("pollerDescriptors", pollerDescriptors);
        return "event-sources/form";
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String createEventSource(
            @PathVariable("workspaceId") final Long workspaceId,
            @ModelAttribute("eventSource") final EventSource eventSource,
            final BindingResult result, final SessionStatus status) {
        //new WorkspaceValidator().validate(eventSource, result);
        if (result.hasErrors()) {
            return "event-sources/form";
        } else {
            final Workspace workspace = this.workspaceRepository.getWorkspaceById(workspaceId);
            workspace.addEventSource(eventSource);
            status.setComplete();
            return "redirect:/workspaces/" + workspace.getId()+".html";
        }
    }

    @RequestMapping(value = "/{eventSourceId}/edit", method = RequestMethod.GET)
    public String initEditEventSourceForm(@PathVariable("eventSourceId") final Long eventSourceId, final Model model) {
        final EventSource eventSource = this.eventSourceRepository.getEventSourceById(eventSourceId);
        model.addAttribute("eventSource", eventSource);
        final List<PollerDescriptor> pollerDescriptors = this.pollerDescriptorRepository.getAll();
        model.addAttribute("pollerDescriptors", pollerDescriptors);
        return "event-sources/form";
    }

    @RequestMapping(value = "/{eventSourceId}/edit", method = { RequestMethod.PUT, RequestMethod.POST })
    public String editEventSource(
            @PathVariable("workspaceId") final Long workspaceId,
            @ModelAttribute("eventSource") final EventSource eventSource,
            final BindingResult result, final SessionStatus status) {

        //new WorkspaceValidator().validate(eventSource, result);
        if (result.hasErrors()) {
            return "event-sources/form";
        } else {
            this.eventSourceRepository.updateEventSource(eventSource);
            status.setComplete();
            return "redirect:/workspaces/" + workspaceId +".html";
        }
    }
}

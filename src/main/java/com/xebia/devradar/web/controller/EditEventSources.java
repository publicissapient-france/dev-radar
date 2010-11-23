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


import java.beans.PropertyEditorSupport;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.xebia.devradar.domain.EventSource;
import com.xebia.devradar.domain.PollerDescriptor;
import com.xebia.devradar.web.EventSourceRepository;
import com.xebia.devradar.web.PollerDescriptorRepository;

@Controller
@RequestMapping("/workspaces/{workspaceId}/eventSources/{eventSourceId}/edit")
@SessionAttributes("eventSource")
@Transactional
public class EditEventSources {

    @Autowired
    private PollerDescriptorRepository pollerDescriptorRepository;

    @Autowired
    private EventSourceRepository eventSourceRepository;

    public EditEventSources() {
    }


    @InitBinder
    protected void initBinder(final ServletRequestDataBinder binder) {
        binder.registerCustomEditor(PollerDescriptor.class, new PropertyEditorSupport(){
            @Override
            public void setAsText(final String text) throws IllegalArgumentException {
                final PollerDescriptor pd = EditEventSources.this.pollerDescriptorRepository.getPollerDescriptorById(Long.parseLong(text));
                this.setValue(pd);
            }

        });
    }

    @RequestMapping(method = RequestMethod.GET)
    public String setupForm(@PathVariable("eventSourceId") final Long eventSourceId, final Model model) {
        final EventSource eventSource = this.eventSourceRepository.getEventSourceById(eventSourceId);
        model.addAttribute("eventSource", eventSource);
        final List<PollerDescriptor> pollerDescriptors = this.pollerDescriptorRepository.getAll();
        model.addAttribute("pollerDescriptors", pollerDescriptors);
        return "workspaces/eventSources/form";
    }

    @RequestMapping(method = { RequestMethod.PUT, RequestMethod.POST })
    public String processSubmit(
        @PathVariable("workspaceId") final Long workspaceId,
        @ModelAttribute("eventSource") final EventSource eventSource,
        final BindingResult result, final SessionStatus status) {

        //new WorkspaceValidator().validate(eventSource, result);
        if (result.hasErrors()) {
            return "workspaces/eventSources/form";
        } else {
            this.eventSourceRepository.updateEventSource(eventSource);
            status.setComplete();
            return "redirect:/workspaces/" + workspaceId +".html";
        }
    }

}

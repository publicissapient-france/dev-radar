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

import javax.servlet.http.HttpServletRequest;

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
import com.xebia.devradar.web.WorkspaceRepository;

@Controller
@RequestMapping("/workspaces/{workspaceId}/eventSources/{eventSourceId}/edit")
@SessionAttributes("eventSource")
@Transactional
public class EditEventSources {

    @Autowired
    private PollerDescriptorRepository pollerDescriptorRepository;

    @Autowired
    private EventSourceRepository eventSourceRepository;
    
    @Autowired
    private WorkspaceRepository workspaceRepository;

    public EditEventSources() {
    }
    
    
    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        binder.registerCustomEditor(PollerDescriptor.class, new PropertyEditorSupport(){
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                PollerDescriptor pd = pollerDescriptorRepository.getPollerDescriptorById(Long.parseLong(text));
                setValue(pd);
            }
            
        });
    }

    @RequestMapping(method = RequestMethod.GET)
    public String setupForm(@PathVariable("workspaceId") Long workspaceId, @PathVariable("eventSourceId") Long eventSourceId, Model model) {
        EventSource eventSource = eventSourceRepository.getEventSourceById(eventSourceId);
        model.addAttribute("eventSource", eventSource);
        final List<PollerDescriptor> pollerDescriptors = pollerDescriptorRepository.getAll();
        model.addAttribute("pollerDescriptors", pollerDescriptors);
        return "workspaces/eventSources/form";
    }

    @RequestMapping(method = { RequestMethod.PUT, RequestMethod.POST })
    public String processSubmit(
        @PathVariable("workspaceId") Long workspaceId, 
        @ModelAttribute("eventSource") EventSource eventSource, 
        BindingResult result, SessionStatus status) {
        
        //new WorkspaceValidator().validate(eventSource, result);
        if (result.hasErrors()) {
            return "workspaces/eventSources/form";
        } else {
            eventSourceRepository.updateEventSource(eventSource);
            status.setComplete();
            return "redirect:/workspaces/" + workspaceId +".html";
        }
    }

}

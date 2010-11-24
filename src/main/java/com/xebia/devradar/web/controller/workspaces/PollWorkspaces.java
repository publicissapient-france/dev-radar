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
package com.xebia.devradar.web.controller.workspaces;


import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.xebia.devradar.domain.Event;
import com.xebia.devradar.domain.Workspace;
import com.xebia.devradar.pollers.PollException;
import com.xebia.devradar.web.WorkspaceRepository;

@Controller
@RequestMapping("/workspaces/{workspaceId}/poll")
@SessionAttributes("eventSource")
@Transactional
public class PollWorkspaces {

    @Autowired
    private WorkspaceRepository workspaceRepository;

    public PollWorkspaces() {
    }

    @RequestMapping(method = RequestMethod.GET)
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

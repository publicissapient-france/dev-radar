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
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.support.SimpleSessionStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class EventSourcesControllerTest {

    private WorkspaceRepository workspaceRepository;

    private EventSourcesController eventSourcesController;

    private EventSourceRepository eventSourceRepository;

    private PollerDescriptorRepository pollerDescriptorRepository;

    @Before
    public void init() {
        workspaceRepository = mock(WorkspaceRepository.class);
        eventSourceRepository = mock(EventSourceRepository.class);
        pollerDescriptorRepository = mock(PollerDescriptorRepository.class);
        eventSourcesController = new EventSourcesController(pollerDescriptorRepository, workspaceRepository, eventSourceRepository);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_init_create_event_sources_model() {
        Model model = new ExtendedModelMap();
        List<PollerDescriptor> pollerDescriptors = Arrays.asList(new PollerDescriptor(), new PollerDescriptor(), new PollerDescriptor());

        when(pollerDescriptorRepository.getAll()).thenReturn(pollerDescriptors);


        String view = eventSourcesController.initCreateEventSourceForm(model);

        assertThat(model.asMap().size(), is(2));
        assertThat(model.asMap().get("eventSource") instanceof EventSource, is(true));
        assertThat(((List<PollerDescriptor>)model.asMap().get("pollerDescriptors")).size(), is(3));
        assertThat(((List<PollerDescriptor>)model.asMap().get("pollerDescriptors")).get(0), is(pollerDescriptors.get(0)));
        assertThat(((List<PollerDescriptor>)model.asMap().get("pollerDescriptors")).get(1), is(pollerDescriptors.get(1)));
        assertThat(((List<PollerDescriptor>)model.asMap().get("pollerDescriptors")).get(2), is(pollerDescriptors.get(2)));
        assertThat(view, is("event-sources/form"));
    }

    @Test
    public void should_create_source_event() throws Exception {
        Workspace workspace = getWorkspace();
        BindingResult result = new MapBindingResult(new HashMap<Object, Object>(), "bindingResult");
        SessionStatus status = new SimpleSessionStatus();
        EventSource eventSource = new EventSource();
        eventSource.setId(42L);

        when(workspaceRepository.getWorkspaceById(workspace.getId())).thenReturn(workspace);

        String view = eventSourcesController.createEventSource(getWorkspace().getId(), eventSource, result, status);

        assertThat(workspace.getEventSources().size(), is(1));
        assertThat(workspace.getEventSources().iterator().next(), is(eventSource));
        assertThat(status.isComplete(), is(true));
        assertThat(view, is("redirect:/workspaces/" + workspace.getId()+".html"));
    }

    @Test
    public void should_not_create_source_event_causes_form_errors() throws Exception {
        Workspace workspace = getWorkspace();
        BindingResult result = new MapBindingResult(new HashMap<Object, Object>(), "bindingResult");
        SessionStatus status = new SimpleSessionStatus();
        EventSource eventSource = new EventSource();
        eventSource.setId(42L);

        result.addError(new ObjectError("field", "default message"));

        String view = eventSourcesController.createEventSource(getWorkspace().getId(), eventSource, result, status);

        assertThat(workspace.getEventSources().size(), is(0));
        assertThat(status.isComplete(), is(false));
        assertThat(view, is("event-sources/form"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_init_edit_event_source_model() {
        Model model = new ExtendedModelMap();
        List<PollerDescriptor> pollerDescriptors = Arrays.asList(new PollerDescriptor(), new PollerDescriptor(), new PollerDescriptor());
        EventSource eventSource = new EventSource();

        eventSource.setId(1L);

        when(eventSourceRepository.getEventSourceById(eventSource.getId())).thenReturn(eventSource);
        when(pollerDescriptorRepository.getAll()).thenReturn(pollerDescriptors);

        String view = eventSourcesController.initEditEventSourceForm(eventSource.getId(), model);

        assertThat(model.asMap().size(), is(2));
        assertThat(((EventSource)model.asMap().get("eventSource")), is(eventSource));
        assertThat(((List<PollerDescriptor>)model.asMap().get("pollerDescriptors")).size(), is(3));
        assertThat(((List<PollerDescriptor>)model.asMap().get("pollerDescriptors")).get(0), is(pollerDescriptors.get(0)));
        assertThat(((List<PollerDescriptor>)model.asMap().get("pollerDescriptors")).get(1), is(pollerDescriptors.get(1)));
        assertThat(((List<PollerDescriptor>)model.asMap().get("pollerDescriptors")).get(2), is(pollerDescriptors.get(2)));
        assertThat(view, is("event-sources/form"));
    }

    @Test
    public void should_edit_event_source() throws Exception {
        Workspace workspace = getWorkspace();
        BindingResult result = new MapBindingResult(new HashMap<Object, Object>(), "bindingResult");
        SessionStatus status = new SimpleSessionStatus();
        EventSource eventSource = new EventSource();
        eventSource.setId(42L);

        when(eventSourceRepository.updateEventSource(eventSource)).thenReturn(eventSource);

        String view = eventSourcesController.editEventSource(getWorkspace().getId(), eventSource, result, status);

        verify(eventSourceRepository, atLeastOnce()).updateEventSource(eventSource);

        assertThat(status.isComplete(), is(true));
        assertThat(view, is("redirect:/workspaces/" + workspace.getId() +".html"));
    }

    @Test
    public void should_not_edit_source_event_causes_form_errors() throws Exception {
        BindingResult result = new MapBindingResult(new HashMap<Object, Object>(), "bindingResult");
        SessionStatus status = new SimpleSessionStatus();
        EventSource eventSource = new EventSource();
        eventSource.setId(42L);

        result.addError(new ObjectError("field", "default message"));

        String view = eventSourcesController.editEventSource(getWorkspace().getId(), eventSource, result, status);

        assertThat(status.isComplete(), is(false));
        assertThat(view, is("event-sources/form"));
    }

    private Workspace getWorkspace() {
        Workspace workspace = new Workspace();

        workspace.setId(1L);
        workspace.setDescription("description");
        workspace.setName("name");
        workspace.setPomUrl("https://github.com/xebia-france/dev-radar/raw/master/pom.xml");
        return workspace;
    }
}

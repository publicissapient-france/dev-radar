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
package com.xebia.devradar.validation;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.validation.Errors;

import com.xebia.devradar.domain.Workspace;


public class WorkspaceValidatorTest {

    
    private WorkspaceValidator validator;
    @Mock private Errors errorsMock;

    @Test
    public void should_reject_if_name_and_pomUrl_for_workspace_are_both_empty_string() throws Exception {
        Workspace workspace = new Workspace();
        workspace.setName(""); workspace.setPomUrl("");
        validator.validate(workspace, errorsMock);
        verify(errorsMock).rejectValue("pomUrl", "required", "required");
        verify(errorsMock).rejectValue("name", "required", "required");
    }

    @Test
    public void should_reject_if_name_and_pomUrl_for_workspace_are_both_null_string() throws Exception {
        Workspace workspace = new Workspace();
        workspace.setName(null); workspace.setPomUrl(null);
        validator.validate(workspace, errorsMock);
        verify(errorsMock).rejectValue("pomUrl", "required", "required");
        verify(errorsMock).rejectValue("name", "required", "required");
    }
    
    @Test
    public void should_reject_if_name_and_pomUrl_for_workspace_are_both_whitespace_string() throws Exception {
        Workspace workspace = new Workspace();
        workspace.setName("   "); workspace.setPomUrl(" ");
        validator.validate(workspace, errorsMock);
        verify(errorsMock).rejectValue("pomUrl", "required", "required");
        verify(errorsMock).rejectValue("name", "required", "required");
    }

    @Test
    public void should_not_reject_if_name_for_workspace_has_text() throws Exception {
        Workspace workspace = new Workspace();
        workspace.setName("z"); workspace.setPomUrl(null);
        validator.validate(workspace, errorsMock);
        verifyNoMoreInteractions(errorsMock);
    }

    @Test
    public void should_not_reject_if_pomUrl_for_workspace_has_text() throws Exception {
        Workspace workspace = new Workspace();
        workspace.setName(null); workspace.setPomUrl("zr");
        validator.validate(workspace, errorsMock);
        verifyNoMoreInteractions(errorsMock);
    }
    
    
    @Before
    public void initBeforeTest() throws Exception {
        initMocks(this);
        validator = new WorkspaceValidator();
    }
}

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

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

import com.xebia.devradar.domain.Workspace;

public class WorkspaceValidator {

    public void validate(Workspace workspace, Errors errors) {
        String name = workspace.getName();
        String pomUrl = workspace.getPomUrl();
        
        if (StringUtils.isBlank(pomUrl) && StringUtils.isBlank(name)) {
            errors.rejectValue("pomUrl", "required", "required");
            errors.rejectValue("name", "required", "required");
        }
    }

}

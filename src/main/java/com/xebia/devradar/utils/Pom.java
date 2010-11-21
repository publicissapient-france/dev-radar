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
package com.xebia.devradar.utils;

public class Pom {

    private String name;
    private String description;
    private String issueManagement;
    private String ciManagement;
    private String scm;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getIssueManagement() {
        return issueManagement;
    }
    public void setIssueManagement(String issueManagement) {
        this.issueManagement = issueManagement;
    }
    public String getCiManagement() {
        return ciManagement;
    }
    public void setCiManagement(String ciManagement) {
        this.ciManagement = ciManagement;
    }
    public String getScm() {
        return scm;
    }
    public void setScm(String scm) {
        this.scm = scm;
    }
}

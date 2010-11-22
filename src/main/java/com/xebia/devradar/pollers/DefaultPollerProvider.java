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
package com.xebia.devradar.pollers;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.xebia.devradar.domain.PollerDescriptor;
import com.xebia.devradar.pollers.hudson.HudsonPoller;
import com.xebia.devradar.pollers.jira.JiraPoller;
import com.xebia.devradar.pollers.sonar.SonarPoller;
import com.xebia.devradar.pollers.svn.SvnPoller;

/**
 * @author Alexandre Dutra
 *
 */
public class DefaultPollerProvider implements PollerProvider {

    private static Map<Class<? extends Poller>, PollerDescriptor> POLLERS = new HashMap<Class<? extends Poller>, PollerDescriptor>();

    static {
        POLLERS.put(
            SvnPoller.class,
            new PollerDescriptor(SvnPoller.class,
                "Standard Subversion Poller",
            "A general-purpose Poller for Subversion Repositories. Uses SVNKit library behind the scenes."));
        POLLERS.put(
            JiraPoller.class,
            new PollerDescriptor(JiraPoller.class,
                "Standard JIRA Poller",
            "A general-purpose Poller for Jira Projects."));
        POLLERS.put(
            HudsonPoller.class,
            new PollerDescriptor(HudsonPoller.class,
                "Standard Hudson Poller",
            "A general-purpose Poller for Hudson Jobs. Monitors Hudson builds and reports failures and successes."));
        POLLERS.put(
            SonarPoller.class,
            new PollerDescriptor(SonarPoller.class,
                "Standard Sonar Poller",
            "A general-purpose Poller for Sonar."));
    }

    public Set<PollerDescriptor> getSupportedPollers(){
        return Collections.unmodifiableSet(new HashSet<PollerDescriptor>(POLLERS.values()));
    }

    @Override
    public boolean isPollerSupported(final Class<? extends Poller> pollerClass) {
        return POLLERS.containsKey(pollerClass);
    }

    @Override
    public PollerDescriptor getPollerDescriptor(final Class<? extends Poller> pollerClass) {
        return POLLERS.get(pollerClass);
    }

}
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
package com.xebia.devradar.pollers.jira;


import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.xebia.devradar.domain.Authentication;
import com.xebia.devradar.domain.Event;
import com.xebia.devradar.domain.EventSource;
import com.xebia.devradar.pollers.PollException;


/**
 * @author Alexandre Dutra
 *
 */
public class JiraPollerTest {

    private static final String JIRA_SOAP_SERVICE_URL = "http://jira.atlassian.com/rpc/soap/jirasoapservice-v2";

    private static final String LOGIN_NAME = "soaptester";

    private static final String LOGIN_PASSWORD = "soaptester";

    private static final String PROJECT_KEY = "TST";

    private JiraPoller poller;

    private EventSource eventSource;

    private final Date end = new Date();

    private final Date start = DateUtils.addMonths(this.end, -1);

    @Before
    public void setUp() throws PollException, MalformedURLException {
        this.eventSource = new EventSource();
        this.eventSource.setUrl(JIRA_SOAP_SERVICE_URL);
        final Authentication auth = new Authentication(LOGIN_NAME, LOGIN_PASSWORD.toCharArray());
        this.eventSource.setAuthentication(auth);
        this.eventSource.addParameter(JiraPoller.JIRA_PROJECT_KEY_PARAM, PROJECT_KEY);
        this.poller = new JiraPoller();
    }

    @Test @Ignore
    public void testJira() throws PollException {
        final List<Event> events = this.poller.poll(this.eventSource, this.start, this.end);
        for (final Event event : events) {
            //TODO delete this
            System.out.println(event.getMessage());
            Assert.assertNotNull("Event message should not be null", event.getMessage());
        }
    }

}

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
package com.xebia.devradar.pollers.git;


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
import com.xebia.devradar.domain.Proxy;
import com.xebia.devradar.pollers.PollException;


/**
 * @author Alexandre Dutra
 *
 */
public class GitHubPollerTest {

    private static final String GIT_HUB_URL = "http://github.com/api/v2/xml/commits/list/xebia-france/dev-radar/master";

    //not necessary
    //    private static final String LOGIN_NAME = "adutra";
    //
    //    private static final String LOGIN_PASSWORD = "xxxxxx";

    private GitHubPoller poller;

    private EventSource eventSource;

    private final Date end = new Date();

    private final Date start = DateUtils.addMonths(this.end, -1);

    @Before
    public void setUp() throws PollException, MalformedURLException {
        this.eventSource = new EventSource();
        this.eventSource.setUrl(GIT_HUB_URL);
        //this.eventSource.setAuthentication(new Authentication(LOGIN_NAME, LOGIN_PASSWORD.toCharArray()));
        this.eventSource.setProxy(
                new Proxy("proxy.gicm.net", 3128, new Authentication("continuum", "continuum".toCharArray())));
        this.poller = new GitHubPoller();
    }

    @Test @Ignore
    public void testGitHub() throws PollException {
        final List<Event> events = this.poller.poll(this.eventSource, this.start, this.end);
        for (final Event event : events) {
            //TODO delete this
            System.out.println(event.getMessage());
            Assert.assertNotNull("Event message should not be null", event.getMessage());
        }
    }

}

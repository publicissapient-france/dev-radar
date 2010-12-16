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
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xebia.devradar.domain.Event;
import com.xebia.devradar.domain.EventSource;
import com.xebia.devradar.pollers.PollException;
import com.xebia.devradar.pollers.Poller;
import com.xebia.devradar.pollers.jira.generated.JiraSoapService;
import com.xebia.devradar.pollers.jira.generated.RemoteException;
import com.xebia.devradar.pollers.jira.generated.RemoteIssue;

/**
 * This client is built following the indications given here:
 * http://confluence.atlassian.com/display/JIRA/Creating+a+SOAP+Client
 * http://confluence.atlassian.com/display/JIRA/Remote+API+%28SOAP%29+Examples
 * 
 * FIXME this works only for Jira > 4.0
 * A possible lead for a more generic client:
 * http://stackoverflow.com/questions/764282/how-can-jira-soap-api-not-have-this-method
 * 
 * @author Alexandre Dutra
 *
 */
public class JiraPoller implements Poller {

    public static final String JIRA_PROJECT_KEY_PARAM = "PROJECT_KEY";

    private static final String JQL_QUERY_TEMPLATE = "project = \"{0}\" and updated > \"{1}\" and updated < \"{2}\" order by updated desc";

    private static final String EVENT_MESSAGE_TEMPLATE = "Issue #{0} updated on {1}";

    private static final Log LOGGER = LogFactory.getLog(JiraPoller.class);

    private FastDateFormat jiraDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm");

    public List<Event> poll(final EventSource source, final Date startDate, final Date endDate) throws PollException {
        try {

            LOGGER.info("polling source: " + source);

            URL url;
            try {
                url = new URL(source.getUrl());
            } catch (MalformedURLException e) {
                throw new PollException("Failure to parse URL: " + source.getUrl(), e);
            }
            final JiraSOAPSession soapSession = new JiraSOAPSession(url);

            soapSession.connect(
                    source.getAuthentication().getUsername(),
                    new String(source.getAuthentication().getPassword()));

            // the JIRA SOAP Service and authentication token are used to make authentication calls
            final JiraSoapService jiraSoapService = soapSession.getJiraSoapService();
            final String authToken = soapSession.getAuthenticationToken();

            final String projectKey = source.getParameter(JIRA_PROJECT_KEY_PARAM);

            final String query = this.buildJQLQuery(
                    projectKey,
                    startDate, endDate);

            final RemoteIssue[] issues = jiraSoapService.getIssuesFromJqlSearch(
                    authToken,
                    query,
                    50);

            final List<Event> events = new ArrayList<Event>();

            for (final RemoteIssue issue : issues) {
                final Date updated = issue.getUpdated().getTime();
                final String message = MessageFormat.format(
                        EVENT_MESSAGE_TEMPLATE,
                        issue.getId(),
                        updated
                );
                final String reporter = issue.getReporter();
                final Event event = new Event(source, message, updated, reporter);
                events.add(event);
            }

            return events;

        } catch (final RemoteException e) {
            LOGGER.error(e);
            throw new PollException(e);

        } catch (final java.rmi.RemoteException e) {
            LOGGER.error(e);
            throw new PollException(e);

        }

    }
    
    private String buildJQLQuery(final String projectKey, final Date start, final Date end) {
        //http://confluence.atlassian.com/display/JIRA/Advanced+Searching
        return MessageFormat.format(
                JQL_QUERY_TEMPLATE,
                projectKey,
                jiraDateFormat.format(start),
                jiraDateFormat.format(end)
        );
    }

}

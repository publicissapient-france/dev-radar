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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xebia.devradar.EventType;
import com.xebia.devradar.domain.Profile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import org.joda.time.format.ISODateTimeFormat;

import com.xebia.devradar.domain.Event;
import com.xebia.devradar.domain.EventSource;
import com.xebia.devradar.pollers.PollException;
import com.xebia.devradar.pollers.Poller;
import com.xebia.devradar.utils.HttpUtils;

/**
 *
 * @see "http://develop.github.com/"
 * @author Alexandre Dutra
 *
 */
public class GitHubPoller implements Poller {

    // http://github.com/api/v2/xml/commits/list/xebia-france/dev-radar/master

    private static final Log LOGGER = LogFactory.getLog(GitHubPoller.class);

    public List<Event> poll(final EventSource source, final Date startDate, final Date endDate) throws PollException {
        LOGGER.info("polling source: " + source);

        try {

            final Document document = HttpUtils.getResponseAsDocument(
                    source.getUrl(),
                    source.getAuthentication(),
                    source.getProxy());

            @SuppressWarnings("unchecked")
            final List<Element> commits = XPath.selectNodes(document.getRootElement(), "/commits/commit");

            final List<Event> events = new ArrayList<Event>();
            for (final Element commit : commits) {

                final String commiter =
                        ((Element)XPath.selectSingleNode(commit, "committer/name")).getValue();
                final Date date = ISODateTimeFormat.dateTimeNoMillis().parseDateTime(
                        ((Element)XPath.selectSingleNode(commit, "committed-date")).getValue()).toDate();

                 Profile profil = getProfile(commiter);

                final Event event = new Event(source, commiter + " commited something.", date, EventType.COMMIT, profil);

                events.add(event);
            }

            return events;

        } catch (final JDOMException e) {
            throw new PollException(e);
        }

    }

    /*
     * TODO temporary method
     */
    private Profile getProfile(String commiter) {
        Profile profil = new Profile();

        profil.setNickname(commiter);
        if (commiter.equals("mrenou")) {
            profil.setId(1L);
        } else if (commiter.equals("adutra")) {
            profil.setId(2L);
        } else {
            profil.setId(3L);
            profil.setNickname("default");
        }
        profil.setEmail("test@xebia.com");
        profil.setGravatarUrl("http://www.google.com/pouet");
        profil.setAliasSCM("aliasScm");
        return profil;
    }
}

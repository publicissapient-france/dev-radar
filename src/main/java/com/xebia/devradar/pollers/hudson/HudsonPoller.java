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
package com.xebia.devradar.pollers.hudson;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

import com.xebia.devradar.domain.Event;
import com.xebia.devradar.domain.EventSource;
import com.xebia.devradar.pollers.PollException;
import com.xebia.devradar.pollers.Poller;
import com.xebia.devradar.utils.HttpUtils;

/**
 * @author Alexandre Dutra
 *
 */
public class HudsonPoller implements Poller {

    public List<Event> poll(final EventSource source, final Date startDate, final Date endDate) throws PollException {

        try {

            final Document jobDocument = HttpUtils.getResponseAsDocument(source.getUrl(), source.getAuthentication(), source.getProxy());

            if(jobDocument == null) {
                return null;
            }

            final List<Event> events = new ArrayList<Event>();

            @SuppressWarnings("unchecked")
            final List<Element> builds = XPath.selectNodes(jobDocument.getRootElement(), "/mavenModuleSet/build");
            if(builds != null && ! builds.isEmpty()){
                for (final Element build : builds) {
                    final String buildNumber = build.getChildText("number");
                    final URL buildUrl = this.transformUrl(build.getChildText("url"));
                    final Document buildDocument = HttpUtils.getResponseAsDocument(buildUrl, source.getAuthentication(), source.getProxy());;
                    final String timestamp = ((Element) XPath.selectSingleNode(buildDocument.getRootElement(), "/mavenModuleSetBuild/timestamp")).getText();
                    final Date date = new Date(Long.parseLong(timestamp));
                    if(date.before(endDate) && date.after(startDate)) {
                        final String result = ((Element) XPath.selectSingleNode(buildDocument.getRootElement(), "/mavenModuleSetBuild/result")).getText();
                        final Element culpritElement = (Element) XPath.selectSingleNode(buildDocument.getRootElement(), "/mavenModuleSetBuild/culprit/fullName");
                        final String culprit = culpritElement == null ? null : culpritElement.getText();
                        final Event event = new Event(
                                source,
                                MessageFormat.format("Build #{0}: {1} (culprit: {2})", buildNumber, result, culprit),
                                date);
                        events.add(event);
                    }
                }
            }

            return events;

        } catch (final JDOMException e) {
            throw new PollException(e);
        }
    }


    private URL transformUrl(final String original) throws PollException {
        try {
            return new URL(new URL(original), "api/xml");
        } catch (final MalformedURLException e) {
            throw new PollException(e);
        }
    }

}

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
package com.xebia.devradar.pollers.sonar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class SonarPoller implements Poller {

    private static final ThreadLocal<SimpleDateFormat> SONAR_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            //2010-09-08T00:04:26+0200
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        }
    };

    public List<Event> poll(final EventSource source, final Date startDate, final Date endDate) throws PollException {

        try {

            final Document jobDocument = HttpUtils.getResponseAsDocument(source.getUrl(), source.getAuthentication(), source.getProxy());

            if(jobDocument == null) {
                return null;
            }

            final Element root = jobDocument.getRootElement();

            final Element dateElement = (Element) XPath.selectSingleNode(root, "/resources/resource/date");
            final String dateStr = dateElement.getTextNormalize();
            final Date date = SONAR_DATE_FORMAT.get().parse(dateStr);

            final List<Event> events = new ArrayList<Event>();

            if(date.before(endDate) && date.after(startDate)) {

                @SuppressWarnings("unchecked")
                final List<Element> measures = XPath.selectNodes(root, "/resources/resource/msr");

                for(final Element measure: measures) {
                    /*
                     * <key>test_errors</key>
                     * <name>Unit test errors</name>
                     * <val>1.0</val>
                     * <frmt_val>1</frmt_val>
                     * <trend>0</trend>
                     * <var>0</var>
                     * <alert>OK</alert>
                     * <alert_text>Lines of code > 10000</alert_text>
                     */
                    final String name = measure.getChildTextNormalize("name");
                    final String alertText = measure.getChildTextNormalize("alert_text");
                    final Event event = new Event(source, name + " " + alertText, date, "sonar");
                    events.add(event);
                }

            }

            return events;

        } catch (final JDOMException e) {
            throw new PollException(e);
        } catch (final ParseException e) {
            throw new PollException(e);
        }
    }


}

/**
 * Copyright (C) 2010 xebia-france <xebia-france@xebia.fr>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebia.devradar.pollers.svn;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xebia.devradar.domain.Event;
import com.xebia.devradar.pollers.PollException;


/**
 * @author Alexandre Dutra
 *
 */
public class SvnPollerTest {

    private SvnPoller poller;

    @Before
    public void setUp() throws PollException, MalformedURLException {
        this.poller = new SvnPoller();
        final Calendar cal = Calendar.getInstance();
        final Date end = cal.getTime();
        cal.add(Calendar.MONTH, -1);
        final Date start = cal.getTime();
        this.poller.setUrl(new URL("http://svn.svnkit.com/repos/svnkit/branches/1.3.x"));
        this.poller.setStartDate(start);
        this.poller.setEndDate(end);
        this.poller.init();
    }

    @Test
    public void testSvnKitOneMonth() throws PollException {
        final List<Event> events = this.poller.poll();
        for (final Event event : events) {
            //TODO delete this
            System.out.println(event.getMessage());
            Assert.assertEquals("Event type should be Subversion", "Subversion", event.getType());
            Assert.assertNotNull("Event message should not be null", event.getMessage());
        }
    }

}

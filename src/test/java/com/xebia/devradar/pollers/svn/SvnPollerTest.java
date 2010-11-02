/**
 * 
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

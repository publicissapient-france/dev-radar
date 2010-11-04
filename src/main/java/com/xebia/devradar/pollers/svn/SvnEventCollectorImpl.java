package com.xebia.devradar.pollers.svn;

import java.util.ArrayList;
import java.util.List;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;

import com.xebia.devradar.domain.Event;

/**
 * @author Alexandre Dutra
 *
 */
public class SvnEventCollectorImpl implements SvnEventCollector {

    private final List<Event> events = new ArrayList<Event>();

    /* (non-Javadoc)
     * @see com.xebia.devradar.pollers.svn.SvnEventCollector#handleLogEntry(org.tmatesoft.svn.core.SVNLogEntry)
     */
    public void handleLogEntry(final SVNLogEntry logEntry) throws SVNException {
        //FIXME some log entries are apparently "dummy entries" with revision -1
        if(logEntry.getRevision() != -1L) {
            final Event event = new Event();
            event.setType("Subversion");
            final String message = logEntry.toString();
            event.setMessage(message);
            this.events.add(event);
        }
    }

    /* (non-Javadoc)
     * @see com.xebia.devradar.pollers.svn.SvnEventCollector#getCollectedEvents()
     */
    public List<Event> getCollectedEvents() {
        return this.events;
    }

}
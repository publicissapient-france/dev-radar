package com.xebia.devradar.pollers.svn;

import java.net.URL;
import java.util.Date;
import java.util.List;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNLogClient;
import org.tmatesoft.svn.core.wc.SVNRevision;

import com.xebia.devradar.domain.Event;
import com.xebia.devradar.pollers.PollException;
import com.xebia.devradar.pollers.Poller;

/**
 * @author Alexandre Dutra
 *
 */
public class SvnPoller implements Poller {

    private URL url;

    private Date startDate;

    private Date endDate;

    /**
     * FIXME is this client thread-safe?
     */
    private SVNLogClient logClient;

    private SVNURL svnUrl;


    public void setUrl(final URL url) {
        this.url = url;
    }

    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    public void init() throws PollException {
        DAVRepositoryFactory.setup();
        this.logClient = SVNClientManager.newInstance().getLogClient();
        try {
            this.svnUrl = SVNURL.parseURIDecoded(this.url.toExternalForm());
        } catch (final SVNException e) {
            throw new PollException("Bad url: " + this.url.toExternalForm(), e);
        }
    }

    /* (non-Javadoc)
     * @see com.xebia.devradar.pollers.svn.Poller#poll()
     */
    public List<Event> poll() throws PollException {
        try {
            final SvnEventCollector handler = this.newEventCollector();
            this.logClient.doLog(
                this.svnUrl, //repository URL
                null, //array of paths relative to <code>url</code>
                SVNRevision.HEAD, //a revision in which <code>paths</code> are first looked up in the repository
                SVNRevision.create(this.startDate),
                SVNRevision.create(this.endDate),
                false, //stopOnCopy
                true, //discoverChangedPaths
                true, //includeMergedRevisions
                -1, //limit
                null, //revisionProperties
                handler);
            return handler.getCollectedEvents();
        } catch (final SVNException e) {
            throw new PollException("Could not poll url: " + this.url.toExternalForm(), e);
        }
    }

    protected SvnEventCollector newEventCollector() {
        return new SvnEventCollectorImpl();
    }

}

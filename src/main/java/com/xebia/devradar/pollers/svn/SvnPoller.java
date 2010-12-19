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
package com.xebia.devradar.pollers.svn;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNLogClient;
import org.tmatesoft.svn.core.wc.SVNRevision;

import com.xebia.devradar.domain.Event;
import com.xebia.devradar.domain.EventSource;
import com.xebia.devradar.pollers.PollException;
import com.xebia.devradar.pollers.Poller;

/**
 * @author Alexandre Dutra
 *
 */
public class SvnPoller implements Poller {

    public List<Event> poll(final EventSource source, final Date startDate, final Date endDate) throws PollException {
        DAVRepositoryFactory.setup();
        //TODO proxy, authentication
        SVNLogClient logClient = SVNClientManager.newInstance().getLogClient();
        SVNURL svnUrl;
        try {
            svnUrl = SVNURL.parseURIDecoded(source.getUrl());
        } catch (final SVNException e) {
            throw new PollException("Bad url: " + source.getUrl(), e);
        }
        try {
            final List<Event> events = new ArrayList<Event>();
            logClient.doLog(
                svnUrl, //repository URL
                null, //array of paths relative to <code>url</code>
                SVNRevision.HEAD, //a revision in which <code>paths</code> are first looked up in the repository
                SVNRevision.create(startDate),
                SVNRevision.create(endDate),
                false, //stopOnCopy
                true, //discoverChangedPaths
                true, //includeMergedRevisions
                -1, //limit
                null, //revisionProperties
                new ISVNLogEntryHandler() {
                    @Override
                    public void handleLogEntry(final SVNLogEntry logEntry) throws SVNException {
                        //FIXME some log entries are apparently "dummy entries" with revision -1
                        if(logEntry.getRevision() != -1L) {
                            //TODO trim
                            final String message = logEntry.toString();

                            final String author = logEntry.getAuthor();
                            final Event event = new Event(source, message, logEntry.getDate(), null, author);
                            events.add(event);
                        }
                    }
                });
            return events;
        } catch (final SVNException e) {
            throw new PollException("Could not poll url: " + source.getUrl(), e);
        }
    }

}

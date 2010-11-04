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
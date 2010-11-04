package com.xebia.devradar.pollers.svn;

import java.util.List;

import org.tmatesoft.svn.core.ISVNLogEntryHandler;

import com.xebia.devradar.domain.Event;

/**
 * @author Alexandre Dutra
 *
 */
public interface SvnEventCollector extends ISVNLogEntryHandler {

    List<Event> getCollectedEvents();

}
package com.xebia.devradar.pollers;

import java.util.List;

import com.xebia.devradar.domain.Event;

/**
 * @author Alexandre Dutra
 *
 */
public interface Poller {

    public abstract List<Event> poll() throws PollException;

}
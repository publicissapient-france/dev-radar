package com.xebia.devradar;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;

public class Fetcher {

    public Set<Event> fetch() {
        Set<Event> events = new HashSet<Event>();
        int i = 0;
        
        Date now = new Date();
        events.add(new Event(DateUtils.addDays(now, -1 * i++), "nicolas", "nicolas a committé qqqch la story #1 concernant la création du workspace, du fetcher et de l'event bla bla blaa bla", "http://www.gravatar.com/avatar/4a89258a4759e47dab3266e9b9d76065.png"));
        events.add(new Event(DateUtils.addDays(now, -1 * i++), "cyrille", "cyille a committé qqqch", "http://www.gravatar.com/avatar/fd83e4fbdb11f925603ef60d25efcbb4"));
        events.add(new Event(DateUtils.addDays(now, -1 * i++), "jean-laurent", "jean-laurent a committé qqqch", "http://www.gravatar.com/avatar/649d3668d3ba68e75a3441dec9eac26e"));
        events.add(new Event(DateUtils.addDays(now, -1 * i++), "simon", "simon a committé qqqch", "http://www.gravatar.com/avatar/740b1444a71181776c42130408a4b848"));
        events.add(new Event(DateUtils.addDays(now, -1 * i++), "alexandre", "alexandre a committé qqqch", "http://www.gravatar.com/avatar/e96398d35fcd2cb3df072bcb28c9c917"));
        events.add(new Event(DateUtils.addDays(now, -1 * i++), "nicolas", "nicolas a committé qqqch", "http://www.gravatar.com/avatar/4a89258a4759e47dab3266e9b9d76065.png"));
        events.add(new Event(DateUtils.addDays(now, -1 * i++), "cyrille", "cyrille a committé qqqch", "http://www.gravatar.com/avatar/fd83e4fbdb11f925603ef60d25efcbb4"));
        events.add(new Event(DateUtils.addDays(now, -1 * i++), "jean-laurent", "jean-laurent a committé qqqch", "http://www.gravatar.com/avatar/649d3668d3ba68e75a3441dec9eac26e"));
        events.add(new Event(DateUtils.addDays(now, -1 * i++), "simon", "simon a committé qqqch", "http://www.gravatar.com/avatar/740b1444a71181776c42130408a4b848"));
        events.add(new Event(DateUtils.addDays(now, -1 * i++), "alexandre", "alexandre a committé qqqch", "http://www.gravatar.com/avatar/e96398d35fcd2cb3df072bcb28c9c917"));
        events.add(new Event(DateUtils.addDays(now, -1 * i++), "nicolas", "nicolas a committé qqqch", "http://www.gravatar.com/avatar/4a89258a4759e47dab3266e9b9d76065.png"));
        events.add(new Event(DateUtils.addDays(now, -1 * i++), "cyrille", "cyille a committé qqqch", "http://www.gravatar.com/avatar/fd83e4fbdb11f925603ef60d25efcbb4"));
        events.add(new Event(DateUtils.addDays(now, -1 * i++), "jean-laurent", "jean-laurent a committé qqqch", "http://www.gravatar.com/avatar/649d3668d3ba68e75a3441dec9eac26e"));
        events.add(new Event(DateUtils.addDays(now, -1 * i++), "simon", "simon a committé qqqch", "http://www.gravatar.com/avatar/740b1444a71181776c42130408a4b848"));
        events.add(new Event(DateUtils.addDays(now, -1 * i++), "alexandre", "alexandre a committé qqqch", "http://www.gravatar.com/avatar/e96398d35fcd2cb3df072bcb28c9c917"));
        events.add(new Event(DateUtils.addDays(now, -1 * i++), "nicolas", "nicolas a committé qqqch", "http://www.gravatar.com/avatar/4a89258a4759e47dab3266e9b9d76065.png"));
        events.add(new Event(DateUtils.addDays(now, -1 * i++), "cyrille", "cyrille a committé qqqch", "http://www.gravatar.com/avatar/fd83e4fbdb11f925603ef60d25efcbb4"));
        events.add(new Event(DateUtils.addDays(now, -1 * i++), "jean-laurent", "jean-laurent a committé qqqch", "http://www.gravatar.com/avatar/649d3668d3ba68e75a3441dec9eac26e"));
        events.add(new Event(DateUtils.addDays(now, -1 * i++), "simon", "simon a committé qqqch", "http://www.gravatar.com/avatar/740b1444a71181776c42130408a4b848"));
        events.add(new Event(DateUtils.addDays(now, -1 * i++), "alexandre", "alexandre a committé qqqch", "http://www.gravatar.com/avatar/e96398d35fcd2cb3df072bcb28c9c917"));
        return events;
    }
}

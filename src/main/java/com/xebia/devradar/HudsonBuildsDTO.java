package com.xebia.devradar;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HudsonBuildsDTO {

    public List<HudsonBuildDTO> builds;

    public Set<Event> toEvents(Map<String, String> defaultMails) {
        Set<Event> events = new HashSet<Event>();
        for (HudsonBuildDTO hudsonBuildDTO : builds) {
            events.addAll(hudsonBuildDTO.toEvents(defaultMails));
        }
        return events;
    }

    public Set<String> getUserNames() {
        Set<String> userNames = new HashSet<String>();
        for (HudsonBuildDTO hudsonBuildDTO : builds) {
            userNames.addAll(hudsonBuildDTO.getUserNames());
        }
        return userNames;
    }
}
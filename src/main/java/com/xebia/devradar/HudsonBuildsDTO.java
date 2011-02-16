package com.xebia.devradar;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Dto returned by a rest resources with tree filter :
 * hudson/job/<jobname>/api/json?tree=builds[actions[causes[userName]],result,culprits[fullName,absoluteUrl],timestamp,building
 *
 * Json example : {"builds":[{"actions":[{"causes":[{}]},{},{},{},{}],"building":false,"result":"SUCCESS","timestamp":1295779740666,"culprits":[{"absoluteUrl":"http://fluxx.fr.cr:8080/hudson/user/Nicolas%20Griso","fullName":"Nicolas Griso"}]}]}
 */
public class HudsonBuildsDTO {

    /**
     * List of hudson builds for a given job.
     */
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
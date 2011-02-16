package com.xebia.devradar;

import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HudsonBuildDTO {

    private static final String ANONYMOUS_USER = "anonymous";

    private static final String BUILD_FAILURE = "FAILURE";

    private static final String BUILD_UNSTABLE = "UNSTABLE";

    private static final String BUILD_SUCCESS = "SUCCESS";

    /**
     * true if the build is in progress.
     * One event will be create only if the build is finished.
     */
    public boolean building;

    /**
     * build result : FAILURE|UNSTABLE|SUCCESS|ABORTED
     * Used in the event description.
     */
    public String result;

    /**
     * Timestamp when the build has been executed.
     * Used to set event timestamp.
     */
    public Long timestamp;

    /**
     * List of users who partipating to a build trigered by a commit
     * Used to determine username(s) linked to a build. One Event for
     * Each user will be create.
     */
    public List<HudsonUserDTO> culprits;

    /**
     * Action which started a build.
     * Used to determine username(s) linked to a build. One Event for
     * Each user will be create.
     */
    public List<HudsonActionDTO> actions;

    public Set<Event> toEvents(Map<String, String> defaultMails) {
        Set<Event> events = new HashSet<Event>();
        if (isValidToEventTransformation()) {
            Set<String> userNames = getUserNames();
            if (userNames.size() == 0) {
                events.add(toEvent());
            }
            for (String userName : userNames) {
                events.add(toEvent(userName, defaultMails));
            }
        }
        return events;
    }

    private Event toEvent() {
        return toEvent(null, null);
    }

    private Event toEvent(String userName, Map<String, String> defaultMails) {
        String author = ANONYMOUS_USER;
        String usermail = null;
        EventLevel eventLevel = EventLevel.UNDIFINED;

        if (userName != null) {
            author = userName;
            usermail = defaultMails.get(userName);
        }

        if (BUILD_FAILURE.equals(result)) {
            eventLevel = EventLevel.ERROR;
        } else if (BUILD_UNSTABLE.equals(result)) {
            eventLevel = EventLevel.WARNING;
        } else if (BUILD_SUCCESS.equals(result)) {
            eventLevel = EventLevel.INFO;
        }
        return new Event(timestamp, author, "Build " + result, usermail, eventLevel);
    }

    public Set<String> getUserNames() {
        Set<String> userNames = new HashSet<String>();

        // commiter usernames
        if (culprits != null) {
            for (HudsonUserDTO hudsonUserDTO : culprits) {
                if (StringUtils.isNotBlank(hudsonUserDTO.fullName)) {
                    userNames.add(hudsonUserDTO.fullName);
                }
            }
        }

        // gui usernames
        if (actions != null) {
            for (HudsonActionDTO hudsonActionDTO : actions) {
                if (hudsonActionDTO.causes != null) {
                    for (HudsonCauseDTO hudsonCauseDTO : hudsonActionDTO.causes) {
                        if (StringUtils.isNotBlank(hudsonCauseDTO.userName)) {
                            userNames.add(hudsonCauseDTO.userName);
                        }
                    }
                }
            }
        }
        return userNames;
    }

    private boolean isValidToEventTransformation() {
        return (building == false);
    }

    static private class HudsonActionDTO {
        public List<HudsonCauseDTO> causes;
    }

    static private class HudsonCauseDTO {
        public String userName;
    }

    static private class HudsonUserDTO {
        public String fullName;
        public String absoluteUrl;
    }
}
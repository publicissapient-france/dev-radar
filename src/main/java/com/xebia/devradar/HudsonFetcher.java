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
package com.xebia.devradar;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.ClientFilter;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  Fetch a list of commit from github and transform each of them into <code>com.xebia.devradar.Event</code>
 */
public class HudsonFetcher implements Pollable {
    
    // default for test
    String url;
    String hudsonUrl;
    Client client;

    private static final String ANONYMOUS_USER = "anonymous";

    private static final String BUILD_FAILURE = "FAILURE";

    private static final String BUILD_UNSTABLE = "UNSTABLE";

    private static final String BUILD_SUCCESS = "SUCCESS";


    public HudsonFetcher(Client client, String hudsonUrl, String jobName) {
        this.client = client;
        this.hudsonUrl = hudsonUrl;
        this.url = hudsonUrl + "/job/" + jobName + "/api/json?tree=builds[actions[causes[userName]],result,culprits[fullName,absoluteUrl],timestamp,building]";
    }
    

    /**
     * Fetch a set of events from a hudson job specified by an url.
     * @return A set of <code>com.xebia.devradar.Event</code>
     */
    @Override
    public Set<Event> fetch() {
        HudsonBuildsDTO hudsonBuildsDTO = getHudsonBuilds();
        Set<Event> events = new HashSet<Event>();
        for (HudsonBuildDTO hudsonBuildDTO : hudsonBuildsDTO.builds) {
            if (isValidBuild(hudsonBuildDTO)) {
                events.addAll(transformBuildToEvents(hudsonBuildDTO));
            }
        }
        return events;
    }

    /*
     * Transform Builds to Events
     */

    private boolean isValidBuild(HudsonBuildDTO hudsonBuildDTO) {
        return (hudsonBuildDTO.building == false);
    }

    private Set<Event> transformBuildToEvents(HudsonBuildDTO hudsonBuildDTO) {
        Set<Event> events = new HashSet<Event>();
        Set<String> userNames = getUserNames(hudsonBuildDTO);
        if (userNames.size() == 0) {
            events.add(transformBuildToEvent(hudsonBuildDTO));
        }
        for (String userName : userNames) {
            events.add(transformBuildToEvent(hudsonBuildDTO, userName));
        }
        return events;
    }

    private Set<String> getUserNames(HudsonBuildDTO hudsonBuildDTO) {
        Set<String> userNames = new HashSet<String>();

        // commiter usernames
        if (hudsonBuildDTO.culprits != null) {
            for (HudsonUserDTO hudsonUserDTO : hudsonBuildDTO.culprits) {
                if (StringUtils.isNotBlank(hudsonUserDTO.fullName)) {
                    userNames.add(hudsonUserDTO.fullName);
                }
            }
        }

        // gui usernames
        if (hudsonBuildDTO.actions != null) {
            for (HudsonActionDTO hudsonActionDTO : hudsonBuildDTO.actions) {
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

    private Event transformBuildToEvent(HudsonBuildDTO hudsonBuildDTO) {
        return transformBuildToEvent(hudsonBuildDTO, null);
    }

    private Event transformBuildToEvent(HudsonBuildDTO hudsonBuildDTO, String userName) {
        String author = ANONYMOUS_USER;
        String usermail = null;
        EventLevel eventLevel = EventLevel.UNDIFINED;

        if (userName != null) {
            author = userName;
            usermail = getUserMail(userName);
        }

        if (BUILD_FAILURE.equals(hudsonBuildDTO.result)) {
            eventLevel = EventLevel.ERROR;
        } else if (BUILD_UNSTABLE.equals(hudsonBuildDTO.result)) {
            eventLevel = EventLevel.WARNING;
        } else if (BUILD_SUCCESS.equals(hudsonBuildDTO.result)) {
            eventLevel = EventLevel.INFO;
        }

        return new Event(hudsonBuildDTO.timestamp, author, "Build " + hudsonBuildDTO.result, usermail, eventLevel);
    }

    /*
     * Rest requests
     */

    /**
     * get another rest ressource to get the email of a user
     *
     * @param userName
     * @return
     */
    private String getUserMail(String userName) {
        String profilUrl = null;
        try {
            profilUrl = hudsonUrl + "/user/" + URLEncoder.encode(userName, "utf-8").replaceAll("\\+", "%20") + "/api/json";
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("utf-8 is not supported", e);
        }

        HudsonUserDetailDTO hudsonUserDetailDTO = client.resource(profilUrl).queryParam("tree", "property[address]").get(HudsonUserDetailDTO.class);
        return hudsonUserDetailDTO.property.get(0).address;
    }

    private HudsonBuildsDTO getHudsonBuilds() {
        return client.resource(url).get(HudsonBuildsDTO.class);
    }

    /*
     * Hudson API
     */

    private static class HudsonBuildsDTO {
        public List<HudsonBuildDTO> builds;
    }

    private static class HudsonBuildDTO {
        public boolean building;
        public String result;
        public Long timestamp;
        public List<HudsonUserDTO> culprits;
        public List<HudsonActionDTO> actions;

    }

    private static class HudsonActionDTO {
        public List<HudsonCauseDTO> causes;
    }

    private static class HudsonCauseDTO {
        public String userName;
    }

    private static class HudsonUserDTO {
        public String fullName;
        public String absoluteUrl;
    }

    private static class HudsonUserDetailDTO {
        public List<HudsonUserPropertyDTO> property;
    }

    private static class HudsonUserPropertyDTO {
        public String address;
    }
}
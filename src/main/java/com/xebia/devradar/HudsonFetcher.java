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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *  Fetch a list of commit from hudson and transform each of them into <code>com.xebia.devradar.Event</code>
 */
public class HudsonFetcher implements Pollable {

    // default for test
    String url;
    String hudsonUrl;
    Client client;

    private static final String API_JSON = "/api/json";
    private static final String TREE_PARAM_VALUE_BUILDS = "builds[actions[causes[userName]],result,culprits[fullName,absoluteUrl],timestamp,building]";
    private static final String TREE_PARAM_VALUE_USER = "property[address]";
    private static final String TREE_PARAM_KEY = "tree";

    public HudsonFetcher(Client client, String hudsonUrl, String jobName) {
        this.client = client;
        this.hudsonUrl = hudsonUrl;
        this.url = getJobBuildsUrl(hudsonUrl, jobName);
    }

    private String getJobBuildsUrl(String hudsonUrl, String jobName) {
        return hudsonUrl + "/job/" + jobName + API_JSON;
    }

    /**
     * Fetch a set of events from a hudson job specified by an url.
     * @return A set of <code>com.xebia.devradar.Event</code>
     */
    @Override
    public Set<Event> fetch() {
        HudsonBuildsDTO hudsonBuildsDTO = getHudsonBuilds();
        Map<String, String> defaultMails = extractDefaultMails(hudsonBuildsDTO);
        return hudsonBuildsDTO.toEvents(defaultMails);
    }

    /**
     * Construct a map to link default mails to usernames using ser rest ressource.
     *
     * @param hudsonBuildsDTO
     * @return A map of default mails by username.
     */
    private Map<String, String> extractDefaultMails(HudsonBuildsDTO hudsonBuildsDTO) {
        Map<String, String> defaultMails = new HashMap<String, String>();

        for (String userName : hudsonBuildsDTO.getUserNames()) {
            if (!defaultMails.containsKey(userName)) {
                defaultMails.put(userName, getUserMail(userName));
            }
        }
        return defaultMails;
    }


    private HudsonBuildsDTO getHudsonBuilds() {
        return client.resource(url).queryParam(TREE_PARAM_KEY, TREE_PARAM_VALUE_BUILDS).get(HudsonBuildsDTO.class);
    }

    /**
     * get another rest ressource to get the email of a user
     *
     * @param userName
     * @return
     */
    private String getUserMail(String userName) {
        String profilUrl = getUserDetailUrl(userName);
        HudsonUserDetailDTO hudsonUserDetailDTO = client.resource(profilUrl).queryParam(TREE_PARAM_KEY, TREE_PARAM_VALUE_USER).get(HudsonUserDetailDTO.class);

        return hudsonUserDetailDTO.getUserMail();
    }

    private String getUserDetailUrl(String userName) {
        String profilUrl = null;
        try {
            profilUrl = hudsonUrl + "/user/" + URLEncoder.encode(userName, "utf-8").replaceAll("\\+", "%20") + API_JSON;
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("utf-8 is not supported", e);
        }
        return profilUrl;
    }
}
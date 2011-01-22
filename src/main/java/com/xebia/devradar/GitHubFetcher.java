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
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.codehaus.jackson.map.util.StdDateFormat;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  Fetch a list of commit from github and transform each of them into <code>com.xebia.devradar.Event</code>
 */
public class GitHubFetcher {

    private static final String COM_SUN_JERSEY_API_JSON_POJOMAPPING_FEATURE = "com.sun.jersey.api.json.POJOMappingFeature";

    /**
     * Fetch a set of events from a github branch specified by an url.
     * @param url
     * @return A set of <code>com.xebia.devradar.Event</code>
     */
    public Set<Event> fetch(String url) {
        GithubCommitsDTO githubCommitsDTO = getGitHubCommits(url);
        Set<Event> events = new HashSet<Event>();

        for (GithubCommitDTO githubCommitDTO : githubCommitsDTO.commits) {
            events.add(transformCommitToEvent(githubCommitDTO));
        }
        return events;
    }

    private Event transformCommitToEvent(GithubCommitDTO githubCommitDTO) {
        DateTime dateTime = ISODateTimeFormat.dateTimeNoMillis().parseDateTime(githubCommitDTO.committed_date);
        return new Event(dateTime.getMillis(), githubCommitDTO.committer.name, githubCommitDTO.message, githubCommitDTO.committer.email);
    }

    private GithubCommitsDTO getGitHubCommits(String url) {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(COM_SUN_JERSEY_API_JSON_POJOMAPPING_FEATURE, true);

        GithubCommitsDTO githubCommitsDTO = buildJerseyClient(clientConfig)
                .resource(url).get(GithubCommitsDTO.class);
        return githubCommitsDTO;
    }

    Client buildJerseyClient(ClientConfig clientConfig) {
        return Client.create(clientConfig);
    }

    /*
     * GitHub API
     */

    private static class GithubCommitsDTO {
        public List<GithubCommitDTO> commits;
    }

    private static class GithubParentCommit {
        public String id;
    }

    private static class GithubCommitDTO {
        public List<GithubParentCommit> parents;
        public GithubPerson author;
        public String url;
        public String id;
        public String committed_date;
        public String authored_date;
        public String message;
        public String tree;
        public GithubPerson committer;
    }

    private static class GithubPerson {
        public String name;
        public String login;
        public String email;
    }
}
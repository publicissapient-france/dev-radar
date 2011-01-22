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
import com.sun.jersey.api.client.ClientHandler;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.client.urlconnection.HttpURLConnectionFactory;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class GitHubFetcherBisTest {

    private static GitHubFetcher fetcher = new GitHubFetcher() {
        @Override
        Client buildJerseyClient(ClientConfig clientConfig) {
            ClientHandler clientHandler = new URLConnectionClientHandler(new CustomHttpURLConnectionFactory());
            return new Client(clientHandler, clientConfig);
        }
    };

    @Test(expected = ClientHandlerException.class)
    public void should_throw_a_runtime_exception_if_cannot_get_commits() {
        String resource = getUriFromResourceAsString("/github/json/github-rest-stream-invalid.json");

        fetcher.fetch(resource);
    }

    @Test
    public void should_return_an_empty_set_if_no_commits_exist() {
        String resource = getUriFromResourceAsString("/github/json/github-rest-stream-0-commit.json");

        Set<Event> events = fetcher.fetch(resource);

        assertThat(events.isEmpty(), CoreMatchers.is(true));
    }

    @Test
    public void should_return_an_1_event_set_if_1_commit_exists() {
        String resource = getUriFromResourceAsString("/github/json/github-rest-stream-1-commit.json");

        Set<Event> events = fetcher.fetch(resource);

        assertThat(events.size(), CoreMatchers.is(1));
        Event event = events.iterator().next();
        assertThat(event.timestamp, equalTo(1294679740000L));
        assertThat(event.author, equalTo("Morgan Renou"));
        assertThat(event.message, equalTo("remove font-family Comic-SANS"));
        assertThat(event.gravatarUrl, equalTo("http://www.gravatar.com/avatar/8c92fcdb7c7abc1a50732a93bc361b5e?d=mm"));
    }

    @Test
    public void should_return_an_2_event_set_if_2_commit_exists() {
        String resource = getUriFromResourceAsString("/github/json/github-rest-stream-2-commit.json");

        Set<Event> events = fetcher.fetch(resource);

        assertThat(events.size(), CoreMatchers.is(2));
        for (Event event : events) {
            if (event.timestamp == 1294679740000L) {
                assertThat(event.timestamp, equalTo(1294679740000L));
                assertThat(event.author, equalTo("Morgan Renou"));
                assertThat(event.message, equalTo("remove font-family Comic-SANS"));
                assertThat(event.gravatarUrl, equalTo("http://www.gravatar.com/avatar/8c92fcdb7c7abc1a50732a93bc361b5e?d=mm"));
            } else {
                assertThat(event.timestamp, equalTo(1297361740000L));
                assertThat(event.author, equalTo("John Wayne"));
                assertThat(event.message, equalTo("other message"));
                assertThat(event.gravatarUrl, equalTo("http://www.gravatar.com/avatar/c5a735b4496c969101497cd826123798?d=mm"));
            }
        }
    }

    @Test
    public void should_return_35_event_set_if_35_commits_exist() {
        String resource = getUriFromResourceAsString("/github/json/github-rest-stream-35-commits.json");

        Set<Event> events = fetcher.fetch(resource);

        assertThat(events.size(), CoreMatchers.is(35));
    }

    private String getUriFromResourceAsString(String path) {
        try {
            return getClass().getResource(path).toURI().toString();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    static class CustomHttpURLConnectionFactory implements HttpURLConnectionFactory {

        @Override
        public HttpURLConnection getHttpURLConnection(URL url) throws IOException {
            return new HttpURLConnectionFromFile(url);
        }
    }

    static class HttpURLConnectionFromFile extends HttpURLConnection {

        protected HttpURLConnectionFromFile(URL url) {
            super(url);
        }

        @Override
        public int getResponseCode() throws IOException {
            return HTTP_OK;
        }

        @Override
        public Map<String, List<String>> getHeaderFields() {
            Map<String, List<String>> map = new HashMap<String, List<String>>();
            map.put("Content-Type", Arrays.asList("application/json; charset=utf-8"));
            return map;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return url.openStream();
        }

        @Override
        public void disconnect() {
        }

        @Override
        public boolean usingProxy() {
            return false;
        }

        @Override
        public void connect() throws IOException {
        }

    }
}



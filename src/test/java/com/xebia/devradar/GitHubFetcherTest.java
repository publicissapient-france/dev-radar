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
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.*;
import java.net.*;
import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Client.class, URLConnectionClientHandler.class})
public class GitHubFetcherTest {

    private GitHubFetcher fetcher = new GitHubFetcher();

    @Test(expected = ClientHandlerException.class)
    public void should_throw_a_runtime_exception_if_cannot_get_commits() {
        initJerseyForTest("/github/json/github-rest-stream-invalid.json");

        fetcher.fetch("");
    }

    @Test
    public void should_return_an_empty_set_if_no_commits_exist() {
        initJerseyForTest("/github/json/github-rest-stream-0-commit.json");

        Set<Event> events = fetcher.fetch("");

        assertThat(events.isEmpty(), CoreMatchers.is(true));
    }

    @Test
    public void should_return_an_1_event_set_if_1_commit_exists() {
        initJerseyForTest("/github/json/github-rest-stream-1-commit.json");

        Set<Event> events = fetcher.fetch("");

        assertThat(events.size(), CoreMatchers.is(1));
        Event event = events.iterator().next();
        assertThat(event.timestamp, equalTo(1294679740000L));
        assertThat(event.author, equalTo("Morgan Renou"));
        assertThat(event.message, equalTo("remove font-family Comic-SANS"));
        assertThat(event.gravatarUrl, equalTo("http://www.gravatar.com/avatar/8c92fcdb7c7abc1a50732a93bc361b5e?d=mm"));
    }

    @Test
    public void should_return_an_2_event_set_if_2_commit_exists() {
        initJerseyForTest("/github/json/github-rest-stream-2-commit.json");

        Set<Event> events = fetcher.fetch("");

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
        initJerseyForTest("/github/json/github-rest-stream-35-commits.json");

        Set<Event> events = fetcher.fetch("");

        assertThat(events.size(), CoreMatchers.is(35));
    }

    private void initJerseyForTest(String path) {
        try {URL url = this.getClass().getResource(path);
            URL urlSpied = PowerMockito.spy(url);
            URI uri = url.toURI();
            URI uriSpied = PowerMockito.spy(uri);
            PowerMockito.when(uriSpied.toURL()).thenReturn(urlSpied);
            PowerMockito.when(urlSpied.openConnection()).thenReturn((URLConnection)new HttpURLConnectionFromFile(url));
            PowerMockito.mockStatic(URI.class);
            PowerMockito.when(URI.create(Matchers.anyString())).thenReturn(uriSpied);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Jersey initialisation for test failed", e);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Jersey initialisation for test failed", e);
        } catch (IOException e) {
            throw new RuntimeException("Jersey initialisation for test failed", e);
        }
    }

    class HttpURLConnectionFromFile extends HttpURLConnection {

        protected HttpURLConnectionFromFile(URL url) {
            super(url);
        }

        @Override
        public int getResponseCode() throws IOException {
            return HTTP_OK;
        }

        @Override
        public String getContentType() {
            return super.getContentType();
        }

        @Override
        public Object getContent(Class[] classes) throws IOException {
            return super.getContent(classes);
        }

        @Override
        public Map<String, List<String>> getHeaderFields() {
            Map<String, List<String>> map = new HashMap<String, List<String>>();

            map.put(null, Arrays.asList("HTTP/1.1 200 OK"));
            map.put("Status", Arrays.asList("200 OK"));
            map.put("X-Runtime", Arrays.asList("61ms"));
            map.put("Etag", Arrays.asList("\"fe6b16385459c031ea491fa831a72583\""));
            map.put("Date", Arrays.asList("Sun, 16 Jan 2011 10:48:47 GMT"));
            map.put("Content-Length", Arrays.asList(new Integer(url.getFile().length()).toString()));
            map.put("Content-Type", Arrays.asList("application/json; charset=utf-8"));
            map.put("Connection", Arrays.asList("keep-alive"));
            map.put("Server", Arrays.asList("nginx/0.7.67"));
            map.put("Cache-Control", Arrays.asList("private, max-age=0, must-revalidate"));
            return map;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new FileInputStream(url.getFile());
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

        @Override
        public OutputStream getOutputStream() throws IOException {
            return new ByteArrayOutputStream();
        }
    }
}



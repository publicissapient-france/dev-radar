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
package com.xebia.devradar.utils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class GravatarUtilsTest {

    private static String EMAIL = "beau@dentedreality.com.au";
    private static String HASH = "205e460b479e2e5b48aec07710c08d50";
    private static String GRAVATAR_URL = "http://www.gravatar.com/avatar/";
    private static String SECURED_GRAVATAR_URL = "https://secure.gravatar.com/avatar/";
    private static String DEFAULT_IMAGE = "?d=mm";

    @Test
    public void should_construct_a_valid_not_secured_url_without_default_image() {
        String url = GravatarUtils.constructGravatarUrlFromEmail(EMAIL, false, false);
        assertNotNull(url);
        assertThat(url, equalTo(GRAVATAR_URL + HASH));
    }

    @Test
    public void should_construct_a_valid_not_secured_url_with_default_image() {
        String url = GravatarUtils.constructGravatarUrlFromEmail(EMAIL, false, true);
        assertNotNull(url);
        assertThat(url, equalTo(GRAVATAR_URL + HASH + DEFAULT_IMAGE));
    }

    @Test
    public void should_construct_a_valid_secured_url_without_default_image() {
        String url = GravatarUtils.constructGravatarUrlFromEmail(EMAIL, true, false);
        assertNotNull(url);
        assertThat(url, equalTo(SECURED_GRAVATAR_URL + HASH));
    }

    @Test
    public void should_construct_a_valid_secured_url_with_default_image() {
        String url = GravatarUtils.constructGravatarUrlFromEmail(EMAIL, true, true);
        assertNotNull(url);
        assertThat(url, equalTo(SECURED_GRAVATAR_URL + HASH + DEFAULT_IMAGE));
    }

    @Test
    public void when_email_is_null_should_construct_a_default_url() {
        String url = GravatarUtils.constructGravatarUrlFromEmail(null, false, true);
        assertNotNull(url);
        assertThat(url, equalTo(GRAVATAR_URL + DEFAULT_IMAGE));
    }

    @Test
    public void when_email_is_empty_should_construct_a_default_avatar_url() {
        String url = GravatarUtils.constructGravatarUrlFromEmail("", false, true);
        assertNotNull(url);
        assertThat(url, equalTo(GRAVATAR_URL + "d41d8cd98f00b204e9800998ecf8427e" + DEFAULT_IMAGE));
    }
}

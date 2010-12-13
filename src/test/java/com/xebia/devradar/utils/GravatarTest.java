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

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class GravatarTest {

    private String fallbackUrl = Gravatar.GRAVATAR_URL + Gravatar.DEFAULT_IMAGE;

    private Gravatar gravatar;

    @Before
    public void given() {
        gravatar = new Gravatar();
    }

    @Test
    public void when_email_is_valid_should_return_url_with_user_image() {

        String url = gravatar.getUrl("beau@dentedreality.com.au");

        assertNotNull(url);
        String beauMailAsMd5 = "205e460b479e2e5b48aec07710c08d50";
        String email = Gravatar.GRAVATAR_URL + beauMailAsMd5 + Gravatar.DEFAULT_IMAGE;
        assertThat(url, equalTo(email));
    }

    @Test
    public void when_email_is_null_should_construct_a_default_url() {
        String url = gravatar.getUrl(null);
        assertNotNull(url);
        assertThat(url, equalTo(fallbackUrl));
    }

    @Test
    public void when_email_is_empty_should_construct_a_default_url() {
        String url = gravatar.getUrl("");
        assertNotNull(url);
        assertThat(url, equalTo(fallbackUrl));
    }
}

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
package com.xebia.devradar.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class ProfilTest {

    private static String NICKNAME = "Beau Lebens";
    private static String EMAIL = "beau@dentedreality.com.au";
    private static String GRAVATAR_URL = "http://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50?d=mm";

    @Test
    public void should_create_profil() {
        Profil profil = new Profil("Beau Lebens", "beau@dentedreality.com.au");
        assertNotNull(profil);
        assertThat(profil.getNickname(), equalTo(NICKNAME));
        assertThat(profil.getEmail(), equalTo(EMAIL));
        assertThat(profil.getGravatarUrl(), equalTo(GRAVATAR_URL));
    }

    @Test
    public void should_modify_gravatar_url_while_modifying_email() {
        Profil profil = new Profil(NICKNAME, "test@free.fr");
        assertNotNull(profil);
        assertThat(profil.getGravatarUrl(), not(GRAVATAR_URL));
        profil.setEmail(EMAIL);
        assertThat(profil.getGravatarUrl(), equalTo(GRAVATAR_URL));
    }
}

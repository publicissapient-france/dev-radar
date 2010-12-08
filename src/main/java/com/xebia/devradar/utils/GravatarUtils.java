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

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GravatarUtils {

    private static final Log LOGGER = LogFactory.getLog(GravatarUtils.class.getName());

    private static final String SECURE_BASE_URL = "https://secure.gravatar.com/avatar/";
    private static final String BASE_URL = "http://www.gravatar.com/avatar/";
    private static final String DEFAULT_IMAGE = "?d=mm";


    /**
     * Construct an url that allows user to retrieve their gravatar image using user's email address.
     *
     * @param email
     *            User's email address
     * @param secure
     *            URL have to be secured
     * @param defaultImage
     *            Use the mystery-man default image if there is no matching image for the specified URL
     * @return The URL that returns the image matching to the specified email.
     */
    public static String constructGravatarUrlFromEmail(String email, boolean secure, boolean defaultImage)
            throws IllegalArgumentException {
        String trimedEmail = StringUtils.trim(email);
        String url = secure ? SECURE_BASE_URL : BASE_URL;
        String emailAsMd5Hex = trimedEmail != null ? asMd5Hex(trimedEmail) : "";

        return url + emailAsMd5Hex + (defaultImage ? DEFAULT_IMAGE : "");
    }

    private static String asMd5Hex(String email) {
        return DigestUtils.md5Hex(email);
    }

}

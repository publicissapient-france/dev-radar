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

    private static final String HASH_ALGORITHM = "MD5";
    private static final String HASH_ENCODING = "CP1252";

    private static String hex(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    }

    /**
     * Construct an url that allows user to retrieve their gravatar image using user's email address.
     *
     * @param email
     *          User's email address
     * @param secure
     *          URL have to be secured
     * @param defaultImage
     *          Use the mystery-man default image if there is no matching image for the specified URL
     * @return
     *          The URL that returns the image matching to the specified email.
     * @throws IllegalArgumentException
     */
    public static String constructGravatarUrlFromEmail(String email, boolean secure, boolean defaultImage) throws IllegalArgumentException {
        String trimedEmail = StringUtils.trim(email);
        String hash = null;
        if (trimedEmail == null || trimedEmail.length() == 0) {
            throw new IllegalArgumentException("email == null || email size = 0");
        }
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            hash = (secure ? SECURE_BASE_URL : BASE_URL)
                 + hex(md.digest(trimedEmail.toLowerCase().getBytes(HASH_ENCODING)))
                 + (defaultImage ? DEFAULT_IMAGE : "");
        } catch (NoSuchAlgorithmException e) {
            String msg = "Could not construct gravatar url, " + HASH_ALGORITHM + " algorithm not found!";
            LOGGER.error(msg, e);
        } catch (UnsupportedEncodingException e) {
            String msg = "Could not construct gravatar url, " + HASH_ENCODING + " encoding unsupported!";
            LOGGER.error(msg, e);
        }
        return hash;
    }
}

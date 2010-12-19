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

public class Gravatar {

    public static final String GRAVATAR_URL = "http://www.gravatar.com/avatar/";
    public static final String DEFAULT_IMAGE = "?d=mm";

    /**
     * Construct a gravatar url based on user's email address.
     *
     * @return The URL that returns the default image matching to the specified email.
     */
    public String getUrl(String email) {

        StringBuilder builder = new StringBuilder(GRAVATAR_URL);
        builder.append(asMd5Hex(email));
        builder.append(DEFAULT_IMAGE);

        return builder.toString();
    }

    private String asMd5Hex(String email) {

        if (email == null) {
            email = StringUtils.EMPTY;
        }
        return DigestUtils.md5Hex(email);
    }

}

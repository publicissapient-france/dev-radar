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

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;

public class PomLoaderUtils {

    public static Pom create(URL url) throws Exception {
        URLConnection urlconnection = url.openConnection();
        InputStream is = urlconnection.getInputStream();
        return createFromScanner(new Scanner(is));
    }

    public static Pom create(File file) throws Exception {
        return createFromScanner(new Scanner(file));
    }

    private static Pom createFromScanner(Scanner sc) throws Exception {
        StringBuilder builder = new StringBuilder();
        while (sc.hasNextLine()) {
            builder.append(sc.nextLine());
        }
        return createFromText(builder.toString());
    }

    private static Pom createFromText(String text) throws Exception {
        Document doc = DocumentHelper.parseText(text);
        String name = doc.valueOf("/*[name()='project']/*[name()='name']");
        String description = doc.valueOf("/*[name()='project']/*[name()='description']");
        String ciManagement = doc.valueOf("/*[name()='project']/*[name()='ciManagement']/*[name()='url']");
        String issueManagement = doc.valueOf("/*[name()='project']/*[name()='issueManagement']/*[name()='url']");
        String scm = doc.valueOf("/*[name()='project']/*[name()='scm']/*[name()='connection']");

        Pom pom = new Pom();
        pom.setName(name);
        pom.setDescription(description);
        pom.setCiManagement(ciManagement);
        pom.setIssueManagement(issueManagement);
        pom.setScm(scm);

        return pom;
    }

}

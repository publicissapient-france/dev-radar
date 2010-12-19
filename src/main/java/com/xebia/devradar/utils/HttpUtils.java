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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.xebia.devradar.domain.Authentication;
import com.xebia.devradar.domain.Proxy;
import com.xebia.devradar.pollers.PollException;

/**
 * 
 * @author Alexandre Dutra
 * 
 */
public class HttpUtils {

    public static Document getResponseAsDocument(
            final String url,
            final Authentication authentication,
            final Proxy proxy) throws PollException {

        final HttpClient client = new HttpClient();

        //TODO configure
        client.getParams().setSoTimeout(5*1000);

        if (authentication != null) {
        
            client.getParams().setAuthenticationPreemptive(true);
        
            final Credentials defaultcreds = new UsernamePasswordCredentials(
                    authentication.getUsername(),
                    String.copyValueOf(authentication.getPassword()));
        
            URL javaNetUrl;
            try {
                javaNetUrl = new URL(url);
            } catch (MalformedURLException e) {
                throw new PollException("Exception parsing URL: " + url, e);
            }
            final AuthScope authScope = new AuthScope(
                    javaNetUrl.getHost(),
                    javaNetUrl.getPort(),
                    AuthScope.ANY_REALM);
        
            client.getState().setCredentials(authScope, defaultcreds);
        }

        if(proxy != null) {
        
            client.getHostConfiguration().setProxy(proxy.getHost(), proxy.getPort());
        
            final Authentication proxyAuthentication = proxy.getAuthentication();
        
            if (proxyAuthentication != null) {
        
                final Credentials defaultcreds = new UsernamePasswordCredentials(
                        proxyAuthentication.getUsername(),
                        String.copyValueOf(proxyAuthentication.getPassword()));
        
                final AuthScope authScope = new AuthScope(
                        proxy.getHost(),
                        proxy.getPort(),
                        AuthScope.ANY_REALM);
        
                client.getState().setProxyCredentials(authScope, defaultcreds);
            }
        }

        final GetMethod get = new GetMethod(url);

        try {
            // execute the GET
            final int status = client.executeMethod(get);

            if (status != HttpStatus.SC_OK) {
                throw new PollException("Wrong HTTP status: " + status);
            }

            final SAXBuilder builder = new SAXBuilder();
            final Document document = builder.build(get.getResponseBodyAsStream());

            return document;

        } catch (final HttpException e) {
            throw new PollException("Unknown IO error while polling URL: " + url, e);
        } catch (final IOException e) {
            throw new PollException("Unknown IO error while polling URL: " + url, e);
        } catch (final JDOMException e) {
            throw new PollException("Unable to build document from URL: " + url, e);
        } finally {
            // release any connection resources used by the method
            get.releaseConnection();
        }
    }

}

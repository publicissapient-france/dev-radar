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
package com.xebia.devradar.pollers.jira;

import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xebia.devradar.pollers.jira.generated.JiraSoapService;
import com.xebia.devradar.pollers.jira.generated.JiraSoapServiceService;
import com.xebia.devradar.pollers.jira.generated.JiraSoapServiceServiceLocator;

/**
 * This represents a SOAP session with JIRA including that state of being logged in or not
 */
public class JiraSOAPSession {

    private static final Log LOGGER = LogFactory.getLog(JiraSOAPSession.class);

    private final JiraSoapServiceService jiraSoapServiceLocator;

    private JiraSoapService jiraSoapService;

    private String token;

    public JiraSOAPSession(final URL webServicePort) {
        this.jiraSoapServiceLocator = new JiraSoapServiceServiceLocator();
        try {
            if (webServicePort == null) {
                this.jiraSoapService = this.jiraSoapServiceLocator.getJirasoapserviceV2();
            } else {
                this.jiraSoapService = this.jiraSoapServiceLocator.getJirasoapserviceV2(webServicePort);
                LOGGER.info("SOAP Session service endpoint at " + webServicePort.toExternalForm());
            }
        } catch (final ServiceException e) {
            throw new RuntimeException("ServiceException during SOAPClient contruction", e);
        }
    }

    public JiraSOAPSession() {
        this(null);
    }

    public void connect(final String userName, final String password) throws RemoteException {
        LOGGER.info("Connnecting via SOAP as : " + userName);
        this.token = this.getJiraSoapService().login(userName, password);
        LOGGER.info("Connected");
    }

    public String getAuthenticationToken() {
        return this.token;
    }

    public JiraSoapService getJiraSoapService() {
        return this.jiraSoapService;
    }

    public JiraSoapServiceService getJiraSoapServiceLocator() {
        return this.jiraSoapServiceLocator;
    }
}

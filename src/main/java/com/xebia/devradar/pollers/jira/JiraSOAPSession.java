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

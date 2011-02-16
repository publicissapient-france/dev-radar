package com.xebia.devradar;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.api.json.JSONConfiguration;

public class HudsonFetcherFactory {

    public HudsonFetcher getHudsonFetcher(String hudsonUrl, String jobName) {
        ClientConfig clientConfig = getClientConfig();
        Client client = getClient(clientConfig);

        return new HudsonFetcher(client, hudsonUrl, jobName);
    }

    private Client getClient(ClientConfig clientConfig) {
        Client client = buildJerseyClient(clientConfig);
        client.addFilter(getClientFilterSettingJsonContentType());
        return client;
    }

    private ClientConfig getClientConfig() {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
        return clientConfig;
    }

    /**
     * Build a jersey client. Defined with package visibility to be overriden in test.
     * @param clientConfig
     * @return A jersey client
     */
    Client buildJerseyClient(ClientConfig clientConfig) {
        return Client.create(clientConfig);
    }

    /**
     * Hudson use the Content-Type : application/javascript which is not
     * accepted by jersey as json deserializer. So, this filter will
     * replace the existing content-type by "application/json;charset=UTF-8"
     *
     * @return client filter setting json content type
     */
    private ClientFilter getClientFilterSettingJsonContentType() {
        ClientFilter clientFilter = new ClientFilter() {
            @Override
            public ClientResponse handle(ClientRequest cr) throws ClientHandlerException {
                ClientResponse clientResponse = getNext().handle(cr);
                clientResponse.getHeaders().remove("Content-Type");
                clientResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
                return clientResponse;
            }
        };
        return clientFilter;
    }
}

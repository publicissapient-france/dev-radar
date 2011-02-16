package com.xebia.devradar;

import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import java.lang.annotation.Annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;


public class HudsonFetcherFactoryTest {

    @Test
    public void should_create_an_hudson_fetcher() {
        //given
        String hudsonUrl = "http://hudson.url.org/hudson";
        String jobName = "hudsonJob";
        HudsonFetcherFactory factory = new HudsonFetcherFactory();

        //when
        HudsonFetcher fetcher = factory.getHudsonFetcher(hudsonUrl, jobName);

        //then
        String buildsUrl = hudsonUrl + "/job/" + jobName + "/api/json";
        assertThat(fetcher.url, equalTo(buildsUrl));
        assertThat(fetcher.hudsonUrl, equalTo(hudsonUrl));
        assertThat(fetcher.client.getProviders(), notNullValue());

    }

    @Test
    public void should_client_get_a_message_body_reader() {
        //given
        String hudsonUrl = "http://hudson.url.org/hudson";
        String jobName = "hudsonJob";
        HudsonFetcherFactory factory = new HudsonFetcherFactory();

        //when
        HudsonFetcher fetcher = factory.getHudsonFetcher(hudsonUrl, jobName);

        //then
        String buildsUrl = hudsonUrl + "/job/" + jobName + "/api/json";
        assertThat(fetcher.client.getProviders().getMessageBodyWriter(HudsonBuildsDTO.class, HudsonBuildsDTO.class, new Annotation[] {}, MediaType.APPLICATION_JSON_TYPE), notNullValue());
    }

}

package com.xebia.devradar;

import org.junit.Test;

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
        String buildsUrl = hudsonUrl + "/job/" + jobName + "/api/json?tree=builds[actions[causes[userName]],result,culprits[fullName,absoluteUrl],timestamp,building]";
        assertThat(fetcher.url, equalTo(buildsUrl));
        assertThat(fetcher.hudsonUrl, equalTo(hudsonUrl));
        assertThat(fetcher.client, notNullValue());
    }

}

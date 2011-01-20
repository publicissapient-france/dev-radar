package com.xebia.devradar;

import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class EventTest {

    @Test
    public void event_should_convert_email_to_gravatar_url() {
        Event event = new Event(1L, "mrenou", "msg", "mrenou@gmail.com");
        assertThat(event.gravatarUrl, equalTo("http://www.gravatar.com/avatar/8c92fcdb7c7abc1a50732a93bc361b5e?d=mm"));
        event = new Event(new Date(), "mrenou", "msg", "mrenou@gmail.com");
        assertThat(event.gravatarUrl, equalTo("http://www.gravatar.com/avatar/8c92fcdb7c7abc1a50732a93bc361b5e?d=mm"));
    }
}

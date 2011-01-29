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
        event = new Event(new Date().getTime(), "mrenou", "msg", "mrenou@gmail.com");
        assertThat(event.gravatarUrl, equalTo("http://www.gravatar.com/avatar/8c92fcdb7c7abc1a50732a93bc361b5e?d=mm"));
    }

    @Test
    public void event_should_convert_from_empty_string_if_email_is_null() {
        Event event = new Event(1L, "mrenou", "msg", null);
        assertThat(event.gravatarUrl, equalTo("http://www.gravatar.com/avatar/d41d8cd98f00b204e9800998ecf8427e?d=mm"));
        event = new Event(new Date().getTime(), "mrenou", "msg", null);
        assertThat(event.gravatarUrl, equalTo("http://www.gravatar.com/avatar/d41d8cd98f00b204e9800998ecf8427e?d=mm"));
    }
}

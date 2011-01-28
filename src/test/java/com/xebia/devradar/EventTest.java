package com.xebia.devradar;

import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.nullValue;

public class EventTest {

    @Test
    public void event_should_convert_email_to_gravatar_url() {
        Event event = new Event(1L, "mrenou", "msg", "mrenou@gmail.com");
        assertThat(event.gravatarUrl, equalTo("http://www.gravatar.com/avatar/8c92fcdb7c7abc1a50732a93bc361b5e?d=mm"));
        event = new Event(new Date(), "mrenou", "msg", "mrenou@gmail.com");
        assertThat(event.gravatarUrl, equalTo("http://www.gravatar.com/avatar/8c92fcdb7c7abc1a50732a93bc361b5e?d=mm"));
    }

    @Test
    public void event_should_not_convert_if_email_is_null() {
        Event event = new Event(1L, "mrenou", "msg", null);
        assertThat(event.gravatarUrl, nullValue());
        event = new Event(new Date(), "mrenou", "msg", null);
        assertThat(event.gravatarUrl, nullValue());
    }
}

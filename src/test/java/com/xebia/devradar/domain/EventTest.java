/**
 * Copyright (C) 2010 Xebia IT Architects <xebia@xebia.fr>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebia.devradar.domain;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class EventTest
{
    private static final String SOME_TYPE = "type";

    private static final String SOME_MESSAGE = "message";

    @Test
    public void should_set_param_at_the_right_place()
    {
        Event event = new Event(SOME_TYPE, SOME_MESSAGE, new Date());
        assertThat(event.getType(), equalTo(SOME_TYPE));
        assertThat(event.getMessage(), equalTo(SOME_MESSAGE));
    }
}

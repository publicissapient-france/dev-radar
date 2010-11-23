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
package com.xebia.devradar.pollers;

import java.util.Date;
import java.util.List;

import com.xebia.devradar.domain.Event;
import com.xebia.devradar.domain.EventSource;

/**
 * @author Alexandre Dutra
 *
 */
public interface Poller {

    /**
     * Poll the specified <code>{@link EventSource}</code> and return all <code>{@link Event}</code>s occurred
     * between <code>startDate</code> and <code>endDate</code>.
     * @param source
     * @param startDate
     * @param endDate
     * @return the <code>{@link Event}</code>s occurred between <code>startDate</code> and <code>endDate</code>
     * @throws PollException
     */
    List<Event> poll(final EventSource source, Date startDate, Date endDate) throws PollException;

}
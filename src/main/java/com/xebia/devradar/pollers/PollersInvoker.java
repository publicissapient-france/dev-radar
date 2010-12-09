/*
 * Copyright 2008-2010 Xebia and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xebia.devradar.pollers;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xebia.devradar.domain.Event;
import com.xebia.devradar.domain.EventSource;
import com.xebia.devradar.domain.PollerDescriptor;
import com.xebia.devradar.domain.Workspace;
import com.xebia.devradar.web.WorkspaceRepository;

/**
 * <p>
 * Scheduled invoker of pollers.
 * </p>
 * <p>
 * Invoke all the workspace.eventSource.pollerDescription.poller at a specified
 * interval and update {@link EventSource#getLastPollDate()} field.
 * </p>
 * 
 * TODO inject the {@link WorkspaceRepository} instead of the
 * {@link EntityManager}. Wait for <a href=
 * "http://groups.google.com/group/xebia-france-dev-radar/browse_thread/thread/6569383f629b655d"
 * >xebia-france-dev-radar group : packages & dépendances</a> decision.
 * 
 * @author <a href="mailto:cyrille@cyrilleleclerc.com">Cyrille Le Clerc</a>
 */
@Service
public class PollersInvoker {

    private static final Log LOGGER = LogFactory.getLog(PollersInvoker.class);

    @PersistenceContext
    private EntityManager entityManager;

    private int maxHistoryInDays = 7;

    // @Autowired
    private WorkspaceRepository workspaceRepository;

    public int getMaxHistoryInDays() {
        return maxHistoryInDays;
    }

    @Transactional
    @Scheduled(fixedDelay = 5 * 60 * 1000)
    public void invokePollers() {

        Date endDate = new Date();
        Date defaultBeginDate = DateUtils.addDays(endDate, -1 * maxHistoryInDays);

        List<Workspace> workspaces = workspaceRepository.getAll();
        for (Workspace workspace : workspaces) {

            for (EventSource eventSource : workspace.getEventSources()) {
                PollerDescriptor pollerDescriptor = eventSource.getPollerDescriptor();

                Poller poller = pollerDescriptor.createPoller();

                try {
                    Date beginDate = eventSource.getLastPollDate();
                    if (beginDate == null) {
                        beginDate = defaultBeginDate;
                    }

                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info("Invoke " + poller + " for " + pollerDescriptor + " - " + eventSource + " - " + workspace + " from "
                                + beginDate + " until " + endDate);
                    }

                    // invoke poller
                    List<Event> events = poller.poll(eventSource, beginDate, endDate);

                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.info("Polled : " + events);
                    } else if (LOGGER.isInfoEnabled()) {
                        LOGGER.info("Polled " + events.size() + " events");
                    }

                    // update workspace
                    workspace.getEvents().addAll(events);
                    eventSource.setLastPollDate(endDate);
                } catch (PollException e) {
                    LOGGER.error("Exception invoking poller " + pollerDescriptor + " - " + eventSource + " - " + workspace + " until "
                            + endDate, e);
                }
            }
        }

        // update workspaces in a separate loop to prevent a
        // ConcurrentModificationException
        for (Workspace workspace : workspaces) {
            workspaceRepository.updateWorkspace(workspace);
        }
    }

    @PostConstruct
    public void postconstruct() {
        this.workspaceRepository = new WorkspaceRepository(entityManager);
    }

    public void setMaxHistoryInDays(int maxHistoryInDays) {
        this.maxHistoryInDays = maxHistoryInDays;
    }
}

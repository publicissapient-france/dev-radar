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
package com.xebia.devradar.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.ContextLoader;

import com.xebia.devradar.persistence.DatabaseInitializer;

/**
 * Initializes the database if necessary.
 * Must be triggered AFTER Spring context initialization.
 * @author Alexandre Dutra
 * @version $Revision: $ $Date: $
 *
 */
public class DatabaseInitializerListener implements ServletContextListener {

    public void contextInitialized(final ServletContextEvent sce) {

        final DatabaseInitializer databaseInitializer =
            (DatabaseInitializer)
            ContextLoader.getCurrentWebApplicationContext().getBean("databaseInitializer");
        databaseInitializer.initDatabase();

    }

    public void contextDestroyed(final ServletContextEvent sce) {
        //Nothing to do
    }

}

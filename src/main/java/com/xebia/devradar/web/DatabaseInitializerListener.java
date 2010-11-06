/**
 * Copyright (C) 2010 xebia-france <xebia-france@xebia.fr>
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

package com.xebia.devradar.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private static final Log LOGGER = LogFactory.getLog(DatabaseInitializerListener.class.getName());
 
    public void contextInitialized(ServletContextEvent sce) {

        LOGGER.info("-----------------------------------------------------------");
    	LOGGER.info("Dev Radar Initializing database");
    	
    	DatabaseInitializer databaseInitializer = 
        	(DatabaseInitializer) 
        		ContextLoader.getCurrentWebApplicationContext().getBean("databaseInitializer");
        databaseInitializer.initDatabase();

        LOGGER.info("Dev Radar Database successfully initialized");
        LOGGER.info("-----------------------------------------------------------");
    }

    public void contextDestroyed(ServletContextEvent sce) {
        //Nothing to do
    }

}

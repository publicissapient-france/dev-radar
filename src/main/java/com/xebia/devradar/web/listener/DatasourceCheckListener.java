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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Alexandre Dutra
 * @version $Revision: $ $Date: $
 */
public class DatasourceCheckListener implements ServletContextListener {

    private static final Log LOGGER = LogFactory.getLog(DatasourceCheckListener.class.getName());
 
    public void contextInitialized(ServletContextEvent sce) {

        LOGGER.info("-----------------------------------------------------------");
        LOGGER.info("Dev-Radar Server starting");
        LOGGER.info("Dev-Radar Checking datasource...");
        
        DataSource ds;
        try{
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            ds = (DataSource) envCtx.lookup("jdbc/devradar-ds");
        } catch (NamingException e){
            String msg = "Could not obtain JNDI datasource looking up java:comp/env/jdbc/devradar-ds, Dev Radar start failed!";
            LOGGER.error(msg, e);
            throw new IllegalStateException(msg, e);
        }
        
        try {
            Connection conn = null;
            try {
                conn = ds.getConnection();
                DatabaseMetaData meta = conn.getMetaData();
                LOGGER.info("Dev Radar RDBMS: " + meta.getDatabaseProductName() + ", version: " + meta.getDatabaseProductVersion());
                LOGGER.info("Dev Radar JDBC driver: " + meta.getDriverName() + ", version: " + meta.getDriverVersion());
            } finally {
                if(conn != null) conn.close();
            }
        } catch (SQLException e) {
            String msg = "Could not read datasource metadata, Dev Radar start failed!";
            LOGGER.error(msg, e);
            throw new IllegalStateException(msg, e);
        }
        
        LOGGER.info("Dev Radar Datasource OK");
        LOGGER.info("-----------------------------------------------------------");

    }

    public void contextDestroyed(ServletContextEvent sce) {
        // Nothing to do
    }

}

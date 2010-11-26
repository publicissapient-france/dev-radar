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
package com.xebia.devradar.persistence;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Alexandre Dutra
 */
public class DatabaseConnectionChecker {

    private static final Log LOGGER = LogFactory.getLog(DatabaseConnectionChecker.class.getName());

    public void checkDatabaseConnection() throws DatabaseConnectionException {


        LOGGER.info("-----------------------------------------------------------");
        LOGGER.info("Dev Radar Checking DataSource...");

        DataSource ds;
        try{
            final Context initCtx = new InitialContext();
            final Context envCtx = (Context) initCtx.lookup("java:comp/env");
            ds = (DataSource) envCtx.lookup("jdbc/devradar-ds");
        } catch (final NamingException e){
            final String msg = "Could not obtain JNDI DataSource looking up java:comp/env/jdbc/devradar-ds, Dev Radar start failed!";
            LOGGER.error(msg, e);
            throw new IllegalStateException(msg, e);
        }

        try {
            Connection conn = null;
            try {
                conn = ds.getConnection();
                final DatabaseMetaData meta = conn.getMetaData();
                LOGGER.info("Dev Radar RDBMS: " + meta.getDatabaseProductName() + ", version: " + meta.getDatabaseProductVersion());
                LOGGER.info("Dev Radar JDBC driver: " + meta.getDriverName() + ", version: " + meta.getDriverVersion());
            } finally {
                if(conn != null) {
                    conn.close();
                }
            }

        } catch (final SQLException e) {

            final String msg = "Could not read DataSource metadata";
            LOGGER.error(msg, e);
            throw new DatabaseConnectionException(msg, e);

        }

        LOGGER.info("Dev Radar DataSource OK");
        LOGGER.info("-----------------------------------------------------------");

    }

}

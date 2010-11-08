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

import java.lang.reflect.Method;

import javax.sql.DataSource;

/**
 * Utility designed to inject test datasets into a supplied <code>{@link DataSource}</code>,
 * before test execution, but inside the test transaction.
 * 
 * @author Alexandre Dutra
 *
 */
public interface DatabasePopulator {

    /**
     * Inject test datasets for the given test method.
     * @param testMethod
     * @throws Exception
     */
    void injectDatasets(Method testMethod) throws Exception;

}

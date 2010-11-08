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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.BeforeTransaction;

/**
 * Base class for all repository (DAO) tests.
 * @author Alexandre Dutra
 *
 */
@ContextConfiguration(
    locations={
        "classpath:com/xebia/devradar/applicationTestContext-*.xml",
        "classpath:com/xebia/devradar/applicationContext-tx.xml"
    }
)
@RunWith(RepositoryTestsJUnit4ClassRunner.class)
public abstract class AbstractRepositoryTests extends AbstractTransactionalJUnit4SpringContextTests {

    /**
     * The <code>{@link DatabasePopulator}</code> associated with the tests.
     */
    @Autowired(required=false)
    protected DatabasePopulator populator;

    /**
     * <code>{@link EntityManager}</code> available to subclasses.
     * Useful e.g. to force a flush of JPA / Hibernate session before
     * testing database state.
     */
    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    protected EntityManager entityManager;

    /**
     * This is executed before the transaction begins.
     * Feel free to override.
     */
    @BeforeTransaction
    public void setUpDatabase(){
    }

    /**
     * This is executed after the transaction begins. Performs
     * DbUnit dataset injection if needed.
     */
    @Before
    public void injectDatasets() throws Exception {
        final Method method = MethodHolder.getTestMethod();
        if(this.populator != null) {
            this.populator.injectDatasets(method);
        }
    }


}
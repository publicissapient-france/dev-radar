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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.BeforeTransaction;

/**
 * Base class for all repository tests.
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
     * The underlying <code>{@link DataSource}</code>.
     */
    @Autowired
    private DataSource dataSource;

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
    public void injectDatasets() throws IOException, DatabaseUnitException, SQLException{
        final Method method = MethodHolder.getTestMethod();
        final List<IDataSet> datasets = this.prepareDatasets(method);
        if(datasets != null && ! datasets.isEmpty()) {
            final DatabaseConnection connection = this.prepareConnection();
            final IDataSet[] array = datasets.toArray(new IDataSet[datasets.size()]);
            final CompositeDataSet dataset = new CompositeDataSet(array);
            DatabaseOperation.REFRESH.execute(connection, dataset);
        }
    }

    /**
     * Process annotations and collect DbUnit datasets for the given test method.
     * @param testMethod
     * @return
     * @throws DataSetException
     * @throws IOException
     */
    protected List<IDataSet> prepareDatasets(final Method testMethod) throws DataSetException, IOException {
        final List<DbUnitDataset> datasetAnnotations = this.collectDatasetAnnotations(testMethod);
        if(datasetAnnotations.isEmpty()) {
            return null;
        } else {
            final FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder().setDtdMetadata(false).setColumnSensing(true).setCaseSensitiveTableNames(true);
            final List<IDataSet> datasets = new ArrayList<IDataSet>();
            for (int i = 0; i < datasetAnnotations.size(); i++) {
                final DbUnitDataset datasetAnn = datasetAnnotations.get(i);
                for(final String resource: datasetAnn.value()){
                    final InputStream is = new ClassPathResource(resource).getInputStream();
                    if(is == null) {
                        throw new FileNotFoundException(resource);
                    }
                    this.logger.info("Processing DbUnit script: " + resource);
                    final InputStreamReader r = new InputStreamReader(is, "UTF-8");
                    final IDataSet dataset = builder.build(r);
                    datasets.add(dataset);
                }
            }
            return datasets;
        }
    }

    /**
     * Collect DbUnit datasets for the given test method.
     * @param testMethod
     * @return
     */
    protected List<DbUnitDataset> collectDatasetAnnotations(final Method testMethod) {
        final List<DbUnitDataset> datasetsAnnotations = new ArrayList<DbUnitDataset>();
        final DbUnitDataset methodDataset = AnnotationUtils.findAnnotation(testMethod, DbUnitDataset.class);
        if(methodDataset != null) {
            datasetsAnnotations.add(methodDataset);
        }
        final DbUnitDataset classDataset = AnnotationUtils.findAnnotation(testMethod.getDeclaringClass(), DbUnitDataset.class);
        if(classDataset != null) {
            datasetsAnnotations.add(classDataset);
        }
        return datasetsAnnotations;
    }

    /**
     * Prepare a <code>{@link DatabaseConnection}</code> object for use with DbUnit.
     * The connection is obtained via <code>{@link DataSourceUtils#getConnection(DataSource)}</code> so that
     * it is bound to the current transaction.
     * @return
     * @throws DatabaseUnitException
     */
    protected DatabaseConnection prepareConnection() throws DatabaseUnitException {
        DatabaseConnection connection = null;
        //we need to use this so that the connection is bound to the current transaction
        final Connection jdbcConnection = DataSourceUtils.getConnection(this.dataSource);
        connection = new DatabaseConnection(jdbcConnection);
        return connection;
    }

}
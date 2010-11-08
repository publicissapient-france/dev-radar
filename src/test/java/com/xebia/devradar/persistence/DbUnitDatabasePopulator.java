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
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * DbUnit implementation of <code>{@link DatabasePopulator}</code>.
 * Supports <code>{@link FlatXmlDataSet}</code>s as well as plain
 * <code>{@link XmlDataSet}</code>s. Other types of <code>{@link IDataSet}</code>s
 * are not yet supported.
 * 
 * @author Alexandre Dutra
 * 
 */
public class DbUnitDatabasePopulator implements DatabasePopulator {

    private static final Log LOGGER = LogFactory.getLog(DbUnitDatabasePopulator.class);

    /**
     * The underlying <code>{@link DataSource}</code>.
     */
    protected DataSource dataSource;

    /**
     * DbUnit config properties.
     * @see DatabaseConfig
     */
    protected Map<String,Object> configProperties;

    /**
     * Whether to use <code>{@link FlatXmlDataSet}</code>s or plain
     * <code>{@link XmlDataSet}</code>s. Other types of <code>{@link IDataSet}</code>s
     * are not yet supported.
     */
    protected boolean useFlatXmlDataset = true;

    /**
     * Whether or not DTD metadata is available
     * in the supplied datasets.
     * @see FlatXmlDataSetBuilder#setDtdMetadata(boolean)
     */
    protected boolean dtdMetadata = false;

    /**
     * Whether to dynamically guess all the
     * columns for each table.
     * @see FlatXmlDataSetBuilder#setColumnSensing(boolean)
     */
    protected boolean columnSensing = true;

    /**
     * Whether table names are case sensitive.
     * @see FlatXmlDataSetBuilder#setCaseSensitiveTableNames(boolean)
     */
    protected boolean caseSensitiveTableNames = false;

    /**
     * Whether to combine all supplied datasets
     * into one single <code>{@link CompositeDataSet}</code>.
     */
    protected boolean useCompositeDataset = true;

    /**
     * Dataset file encoding.
     */
    protected String datasetEncoding = "UTF-8";

    /**
     * The <code>{@link DatabaseOperation}</code> to execute.
     */
    protected DatabaseOperation operation = DatabaseOperation.REFRESH;

    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setConfigProperties(final Map<String, Object> configProperties) {
        this.configProperties = configProperties;
    }

    public void setDtdMetadata(final boolean dtdMetadata) {
        this.dtdMetadata = dtdMetadata;
    }

    public void setColumnSensing(final boolean columnSensing) {
        this.columnSensing = columnSensing;
    }

    public void setCaseSensitiveTableNames(final boolean caseSensitiveTableNames) {
        this.caseSensitiveTableNames = caseSensitiveTableNames;
    }

    public void setUseFlatXmlDataset(final boolean useFlatXmlDataset) {
        this.useFlatXmlDataset = useFlatXmlDataset;
    }

    public void setDatasetEncoding(final String datasetEncoding) {
        this.datasetEncoding = datasetEncoding;
    }

    public void setUseCompositeDataset(final boolean useCompositeDataset) {
        this.useCompositeDataset = useCompositeDataset;
    }

    public void setOperation(final DatabaseOperation operation) {
        this.operation = operation;
    }

    /**
     * @inheritdoc
     */
    public void injectDatasets(final Method testMethod) throws IOException, DatabaseUnitException, SQLException {

        final List<IDataSet> datasets = this.prepareDatasets(testMethod);

        if(datasets != null && ! datasets.isEmpty()) {

            final DatabaseConnection connection = this.prepareConnection();

            if(this.configProperties != null) {
                final DatabaseConfig config = connection.getConfig();
                this.applyConfigProperties(config);
            }

            if(this.useCompositeDataset) {
                final IDataSet[] array = datasets.toArray(new IDataSet[datasets.size()]);
                final CompositeDataSet dataset = new CompositeDataSet(array);
                this.operation.execute(connection, dataset);
            } else {
                for (final IDataSet dataset : datasets) {
                    this.operation.execute(connection, dataset);
                }
            }

        }

    }

    protected List<IDataSet> prepareDatasets(final Method testMethod) throws DataSetException, IOException {
        final List<DbUnitDataset> datasetAnnotations = this.collectDatasetAnnotations(testMethod);
        if(datasetAnnotations.isEmpty()) {
            return null;
        } else {
            FlatXmlDataSetBuilder builder = null;
            if(this.useFlatXmlDataset) {
                builder = new FlatXmlDataSetBuilder().
                setDtdMetadata(this.dtdMetadata).
                setColumnSensing(this.columnSensing).
                setCaseSensitiveTableNames(this.caseSensitiveTableNames);
            }
            final List<IDataSet> datasets = new ArrayList<IDataSet>();
            for (int i = 0; i < datasetAnnotations.size(); i++) {
                final DbUnitDataset datasetAnn = datasetAnnotations.get(i);
                for(final String resource: datasetAnn.value()){
                    final ClassPathResource classPathResource = new ClassPathResource(resource);
                    if(!classPathResource.exists()) {
                        throw new FileNotFoundException(resource);
                    }
                    final InputStream is = classPathResource.getInputStream();
                    DbUnitDatabasePopulator.LOGGER.info("Processing DbUnit script: " + resource);
                    final InputStreamReader r = new InputStreamReader(is, this.datasetEncoding);
                    final IDataSet dataset;
                    if(this.useFlatXmlDataset) {
                        dataset = builder.build(r);
                    } else {
                        dataset = new XmlDataSet(r);
                    }
                    datasets.add(dataset);
                }
            }
            return datasets;
        }
    }

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

    protected DatabaseConnection prepareConnection() throws DatabaseUnitException {
        //we need to use this so that the connection is bounded to the current transaction
        final Connection jdbcConnection = DataSourceUtils.getConnection(this.dataSource);
        final DatabaseConnection connection = new DatabaseConnection(jdbcConnection);
        return connection;
    }

    protected void applyConfigProperties(final DatabaseConfig config){
        for(final String key: this.configProperties.keySet()){
            final Object value = this.configProperties.get(key);
            config.setProperty(key, value);
        }
    }

}

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
package com.xebia.devradar.domain;

import javax.persistence.*;
import java.util.Set;

@Entity

public class BadgeType extends AbstractEntity {

    @Basic(optional = false)
    @Column(unique = true)
    private String name;

    private String dslQuery;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "BADGETYPE_ID")
    private Set<DslParameter> dslParameters;

    private Class ownerFinderClass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDslQuery() {
        return dslQuery;
    }

    public void setDslQuery(String dslQuery) {
        this.dslQuery = dslQuery;
    }

    public Set<DslParameter> getDslParameters() {
        return dslParameters;
    }

    public void setDslParameters(Set<DslParameter> dslParameters) {
        this.dslParameters = dslParameters;
    }

    public Class getOwnerFinderClass() {
        return ownerFinderClass;
    }

    public void setOwnerFinderClass(Class ownerFinderClass) {
        ownerFinderClass = ownerFinderClass;
    }
}

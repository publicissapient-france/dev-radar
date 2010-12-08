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
package com.xebia.devradar.badge.dsl;

import com.xebia.devradar.EventType;
import com.xebia.devradar.domain.DslParameter;

public enum DslParameterType {

    ID, EVENT_TYPE;

    public Object getObjectValue(DslParameter dslParameter) {
        if (dslParameter.getType().equals(ID)) {
           return Long.valueOf(dslParameter.getValue());
        } else if (dslParameter.getType().equals(EVENT_TYPE)) {
            return EventType.valueOf(dslParameter.getValue());
        }
        throw new IllegalArgumentException();
    }

}

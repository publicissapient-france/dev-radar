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
package com.xebia.devradar.pollers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Set;

import com.xebia.devradar.domain.PollerDescriptor;



/**
 * @author Alexandre Dutra
 *
 */
public class PollerServiceLocator {

    public static Set<PollerDescriptor> getSupportedPollers(){
        final Set<PollerDescriptor> pollerDescriptors = new HashSet<PollerDescriptor>();
        final ServiceLoader<PollerProvider> eventSourceLoader = ServiceLoader.load(PollerProvider.class);
        final Iterator<PollerProvider> iterator = eventSourceLoader.iterator();
        while(iterator.hasNext()) {
            final PollerProvider provider = iterator.next();
            pollerDescriptors.addAll(provider.getSupportedPollers());
        }
        return pollerDescriptors;
    }

    public static PollerDescriptor getPollerDescriptor(final Class<? extends Poller> pollerClass){
        final ServiceLoader<PollerProvider> eventSourceLoader = ServiceLoader.load(PollerProvider.class);
        final Iterator<PollerProvider> iterator = eventSourceLoader.iterator();
        while(iterator.hasNext()) {
            final PollerProvider provider = iterator.next();
            if(provider.isPollerSupported(pollerClass)) {
                return provider.getPollerDescriptor(pollerClass);
            }
        }
        return null;
    }
}

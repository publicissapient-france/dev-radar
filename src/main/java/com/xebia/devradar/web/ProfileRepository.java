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
package com.xebia.devradar.web;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.xebia.devradar.domain.Workspace;
import org.springframework.stereotype.Repository;

import com.xebia.devradar.domain.Profile;

/**
 * Repository for <code>{@link Profile}</code> instances.
 *
 */
@Repository
public class ProfileRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Profile createProfile(Profile profile) {
        entityManager.persist(profile);
        return profile;
    }

    public void deleteProfile(final Profile profile) {
        entityManager.remove(getProfileById(profile.getId()));
    }

    @SuppressWarnings("unchecked")
    public List<Profile> getAll() {
        return this.entityManager.createQuery("from Profile ").getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Profile> getAllProfiles() {
        Query query = entityManager.createNamedQuery(Profile.ORDER_BY_NAME);
        return query.getResultList();
    }

    public Profile getProfileById(Long id) {
        return entityManager.find(Profile.class, id);
    }

    public Profile updateProfile(Profile profile) {
        return entityManager.merge(profile);
    }

}

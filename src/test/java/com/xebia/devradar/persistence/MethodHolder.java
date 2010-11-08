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
import java.util.ConcurrentModificationException;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

/**
 * <code>{@link RunListener}</code> allowing to retrieve the test method currently being
 * executed.
 * Based on code by Joshua.Graham@thoughtworks.com.
 * @see "http://tech.groups.yahoo.com/group/junit/message/18728"
 * @author Alexandre Dutra
 *
 */
public class MethodHolder extends RunListener {

    private final static ThreadLocal<Class<?>> CLASS = new ThreadLocal<Class<?>>();

    private final static ThreadLocal<Method> METHOD = new ThreadLocal<Method>();

    @Override
    public void testStarted(final Description description) throws ClassNotFoundException, SecurityException, NoSuchMethodException {
        /* record start of tests, not suites */
        if(description.isTest()) {
            final String methodName = MethodHolder.extractTestMethodName(description.getDisplayName());
            final String className = MethodHolder.extractTestClassName(description.getDisplayName());
            final Class<?> testClass = Class.forName(className);
            MethodHolder.CLASS.set(testClass);
            final Method testMethod = testClass.getMethod(methodName);
            MethodHolder.METHOD.set(testMethod);
        } else {
            MethodHolder.CLASS.set(null);
            MethodHolder.METHOD.set(null);
        }
    }

    @Override
    public void testFinished(final Description description) throws ClassNotFoundException, SecurityException, NoSuchMethodException {
        final String testName = MethodHolder.getTestName();
        if (testName != null) {
            if (testName.equals(description.getDisplayName())) {
                MethodHolder.CLASS.set(null);
                MethodHolder.METHOD.set(null);
            } else {
                throw new ConcurrentModificationException(
                    "Test name mismatch. Was " +
                    description.getDisplayName() +
                    " expected "
                    + testName);
            }
        }
    }

    public static Method getTestMethod() {
        return MethodHolder.METHOD.get();
    }

    public static Class<?> getTestClass() {
        return MethodHolder.CLASS.get();
    }

    /**
     * Old-style JUnit 3 TestScenario.getName() format
     * @return package.class.method()
     */
    public static String getName() {
        return String.format("%s.%s()", MethodHolder.getTestClassName(), MethodHolder.getTestMethodName());
    }

    /**
     * JUnit 4 test name format
     * @return method(package.class)
     */
    public static String getTestName() {
        return String.format("%s(%s)", MethodHolder.getTestMethodName(), MethodHolder.getTestClassName());
    }

    public static String getTestMethodName() {
        return MethodHolder.getTestMethod().getName();
    }

    public static String getTestClassName() {
        return MethodHolder.getTestClass().getName();
    }

    private static String extractTestMethodName(final String name) {
        if (name != null) {
            final int last = name.lastIndexOf('(');
            return last < 0 ? name : name.substring(0, last);
        }
        return null;
    }

    private static String extractTestClassName(final String name) {
        if (name != null) {
            final int last = name.lastIndexOf('(');
            return last < 0 ? null : name.substring(last + 1, name.length() - 1);
        }
        return null;
    }
}
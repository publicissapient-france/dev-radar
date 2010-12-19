<%--
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
 --%>
<%@ include file="/WEB-INF/jsp/includes.jsp"%>
<%@ include file="/WEB-INF/jsp/header.jsp"%>
<c:set var="badgeType" value="${badgeTypeFormModel.badgeType}" />
<c:choose>
    <c:when test="${badgeType.new}">
        <c:set var="method" value="post" />
    </c:when>
    <c:otherwise>
        <c:set var="method" value="put" />
    </c:otherwise>
</c:choose>

<h2><c:if test="${badgeType.new}">New </c:if> <c:if
        test="${not badgeType.new}">Edit <c:out value="${badgeType.name}"></c:out>
</c:if> Badge Type</h2>

<form:form modelAttribute="badgeTypeFormModel" method="${method}">
    <table>
        <c:if test="${not badgeType.new}">
            <tr>
                <td>Id:</td>
                <td><c:out value="${badgeType.id}" /></td>
            </tr>
            <form:hidden path="badgeType.id" />
            <form:hidden path="badgeType.version" />
        </c:if>
        <tr>
            <td>Name*: <form:errors path="badgeType.name" cssClass="errors" /></td>
            <td><form:input path="badgeType.name" /></td>
        </tr>
        <tr>
            <td>Dsl Query*: <form:errors path="badgeType.dslQuery" cssClass="errors" /></td>
            <td><form:textarea path="badgeType.dslQuery" cols="50" rows="10" /></td>
        </tr>
        <tr>
            <td>Test mode</td>
            <td><form:checkbox path="testMode" /></td>
        </tr>
        <tr>
            <td>Workspace tested</td>
            <td><form:select path="workspaceId" itemLabel="name" itemValue="id" items="${badgeTypeFormModel.workspaces}" /></td>
        </tr>
        <tr>
            <td>Badge Owner : <form:errors path="gravatarUrl" cssClass="errors" /></td>
            <td><img src="${badgeTypeFormModel.gravatarUrl}"/></td>
        </tr>
        <tr>
            <td></td>
            <c:choose>
            <c:when test="${badgeType.new}">
                <td><p class="submit"><input type="submit" value="Add Badge Type" /></p></td>
            </c:when>
            <c:otherwise>
            <td><p class="submit"><input type="submit" value="Update Badge Type" /><td>
            </c:otherwise>
            </c:choose>
        </tr>
    </table>
</form:form>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>

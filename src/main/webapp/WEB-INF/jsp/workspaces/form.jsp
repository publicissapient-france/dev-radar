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
<c:choose>
    <c:when test="${workspace.new}">
        <c:set var="method" value="post" />
    </c:when>
    <c:otherwise>
        <c:set var="method" value="put" />
    </c:otherwise>
</c:choose>

<h2><c:if test="${workspace.new}">New </c:if> <c:if
        test="${not worksace.new}">Edit <c:out value="${workspace.name}"></c:out>
</c:if> Workspace</h2>

<form:form modelAttribute="workspace" method="${method}">
    <table>
        <c:if test="${not workspace.new}">
            <tr>
                <td>Id:</td>
                <td><c:out value="${workspace.id}" /></td>
            </tr>
        </c:if>
        <tr>
            <td>Pom URL*: <form:errors path="pomUrl" cssClass="errors" /></td>
            <td><form:input path="pomUrl" size="50" maxlength="256" /></td>
        </tr>
        <tr>
            <td>Name*: <form:errors path="name" cssClass="errors" /></td>
            <td><form:input path="name" size="50" maxlength="50" /></td>
        </tr>
        <tr>
            <td>Description: <form:errors path="description"
                                          cssClass="errors" /></td>
            <td><form:input path="description" size="50" maxlength="512" /></td>
        </tr>
        <tr>
            <td>Badges: </td>
            <td>
                <c:forEach items="${workspace.badges}" var="badge">
                    <c:out value="${badge.badgeType.name}" />
                </c:forEach>
            </td>
        </tr>
        <tr><td></td>
            <c:choose>
            <c:when test="${workspace.new}">
                <td><p class="submit"><input type="submit" value="Add Workspace" /></p></td>
            </c:when>
            <c:otherwise>
            <td><p class="submit"><input type="submit" value="Update Workspace" /><td>
                </c:otherwise>
                </c:choose>
        </tr>
    </table>
</form:form>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>

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

<h2>BadgeType <c:out value="${badgeType.name}" /></h2>
<table>
    <tr>
        <td width="100">Id:</td>
        <td><c:out value="${badgeType.id}" /></td>
    </tr>
    <tr>
        <td>Name :</td>
        <td><c:out value="${badgeType.name}" /></td>
    </tr>
    <tr>
        <td>Dsl Query :</td>
        <td><c:out value="${badgeType.dslQuery}" /></td>
    </tr>
    <tr>
        <td><a class="submit"
               href="<spring:url value="/badgeTypes/${badgeType.id}/edit.html" htmlEscape="true" />">Edit</a></td>
        <td><form:form action="./${badgeType.id}/delete.html" method="delete">
            <p class="submit"><input type="submit" value="Delete Badge Type" /></p>
        </form:form></td>
    </tr>
</table>


<p>

    <%@ include file="/WEB-INF/jsp/footer.jsp"%>

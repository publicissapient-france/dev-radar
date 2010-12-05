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
	<c:when test="${profile.new}">
		<c:set var="method" value="post" />
	</c:when>
	<c:otherwise>
		<c:set var="method" value="put" />
	</c:otherwise>
</c:choose>

<h2><c:if test="${profile.new}">New </c:if> <c:if
	test="${not profile.new}">Edit <c:out value="${profile.nickname}"></c:out>
</c:if> Profiles</h2>

<form:form modelAttribute="profile" method="${method}">

	<table>
		<c:if test="${not profile.new}">
			<tr>
				<td>Id:</td>
				<td><c:out value="${profile.id}" /></td>
                <td>&nbsp;</td>
			</tr>
		</c:if>
		<tr>
			<td>Nickname*: </td>
			<td><form:input path="nickname" size="50" maxlength="30" /></td>
            <td><form:errors path="nickname" cssClass="errors"/></td>
		</tr>
		<tr>
			<td>Email*: </td>
			<td><form:input path="email" size="50" maxlength="80" /></td>
            <td><form:errors path="email" cssClass="errors"/></td>
		</tr>
		<tr>
			<td>Alias SCM: </td>
			<td><form:input path="aliasSCM" size="50" maxlength="30" /></td>
            <td><form:errors path="aliasSCM" cssClass="errors"/></td>
        </tr>
		<tr>
            <td></td>
			<c:choose>
				<c:when test="${profile.new}">
					<td><p class="submit"><input type="submit" value="Add profile" /></p></td>
				</c:when>
				<c:otherwise>
					<td><p class="submit"><input type="submit" value="Update profile" /><td>
				</c:otherwise>
			</c:choose>
		</tr>
	</table>
</form:form>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>

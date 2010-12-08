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

<h2>Profile <c:out value="${profile.nickname}" /></h2>
<table>
	<tr>
		<td>Id:</td>
		<td><c:out value="${profile.id}" /></td>
        <td rowspan="4" valign="top"><img src="${profile.gravatarUrl}" alt="URL Gravatar" /></td>
	</tr>
	<tr>
		<td>Email:</td>
		<td><c:out value="${profile.email}" /></td>
	</tr>
	<tr>
		<td>Alias SCM :</td>
		<td><c:out value="${profile.aliasSCM}" /></td>
	</tr>
	<tr>
		<td><a class="submit"
			href='<spring:url value="/profiles/${profile.id}/edit.html" htmlEscape="true" />'>Edit</a></td>
		<td><form:form method="delete" action="./${profile.id}/delete.html">
			<p class="submit"><input type="submit" value="Delete Profile" /></p>
		</form:form></td>
	</tr>
</table>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>

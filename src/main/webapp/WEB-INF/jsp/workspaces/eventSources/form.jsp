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
	<c:when test="${eventSource.new}">
		<c:set var="method" value="post" />
	</c:when>
	<c:otherwise>
		<c:set var="method" value="put" />
	</c:otherwise>
</c:choose>

<h2><c:if test="${eventSource.new}">New </c:if> <c:if
	test="${not eventSource.new}">Edit <c:out value="${eventSource.description}"></c:out>
</c:if> Event Source</h2>

<form:form modelAttribute="eventSource" method="${method}">
	<table>
		<c:if test="${not eventSource.new}">
			<tr>
				<td>Id:</td>
				<td><c:out value="${eventSource.id}" /></td>
			</tr>
		</c:if>
		
		<spring:bind path="eventSource.pollerDescriptor">
        <tr>
            <td><form:label path="pollerDescriptor">Poller</form:label></td>
            <td>
               <table>
                   <c:forEach items="${pollerDescriptors}" var="pollerDescriptor">
                       <tr>
                           <td>
                             <form:radiobutton  path="pollerDescriptor" value="${pollerDescriptor.id}" label="${pollerDescriptor.name}"/>
                           </td>
                           <td>
                             ${pollerDescriptor.description}
                           </td>
                       </tr>
                   </c:forEach>
                   
               </table>
               <form:errors path="pollerDescriptor" cssClass="error" />
            </td>
        </tr>
        </spring:bind>
        <tr>
            <td><form:label path="description">Description</form:label></td>
            <td><form:input path="description" /><form:errors path="description" cssClass="error" /></td>
        </tr>
        <tr>
            <td><form:label path="url">URL</form:label></td>
            <td><form:input path="url" /><form:errors path="url" cssClass="error" /></td>
        </tr>
        <tr>
            <td><form:label path="proxy.host">Proxy Host</form:label></td>
            <td><form:input path="proxy.host" /><form:errors path="proxy.host" cssClass="error" /></td>
        </tr>
        <tr>
            <td><form:label path="proxy.port">Proxy Port</form:label></td>
            <td><form:input path="proxy.port" /><form:errors path="proxy.port" cssClass="error" /></td>
        </tr>
		<tr>
			<c:choose>
				<c:when test="${eventSource.new}">
					<td><p class="submit"><input type="submit" value="Add Event Source" /></p></td>
				</c:when>
				<c:otherwise>
					<td><p class="submit"><input type="submit" value="Update Event Source" /><td>
				</c:otherwise>
			</c:choose>
		</tr>
	</table>
</form:form>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>

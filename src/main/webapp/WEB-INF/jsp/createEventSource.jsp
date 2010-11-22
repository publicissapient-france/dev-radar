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
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head><title>New Event Source for Workspace: ${workspaceName}</title></head>
<body>
   <h1>New Event Source for Workspace: ${workspaceName}</h1>

<form:form method="post" commandName="eventSource" action="onCreate.html">
	
	<table>
	    <tr>
	        <td><form:label path="pollerDescriptor">Poller</form:label></td>
	        <td>
	           <table>
		           <c:forEach items="${pollerDescriptors}" var="pollerDescriptor">
			           <tr>
				           <td>
			                 <form:radiobutton path="pollerDescriptor" value="${pollerDescriptor.id}" label="${pollerDescriptor.name}"/>
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
        <tr>
            <td><form:label path="description">Description</form:label></td>
            <td><form:input path="description" /><form:errors path="description" cssClass="error" /></td>
        </tr>
        <tr>
            <td><form:label path="url">URL</form:label></td>
            <td><form:input path="url" /><form:errors path="url" cssClass="error" /></td>
        </tr>
        <tr>
            <td><form:label path="proxyUrl">Proxy URL</form:label></td>
            <td><form:input path="proxyUrl" /><form:errors path="proxyUrl" cssClass="error" /></td>
        </tr>
	</table>  
    <input type="submit" value="Create Event Source"/>

</form:form>
</body>
</html>
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

<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>

<html>
<head><title>Workspace : <c:out value="${workspace.name}" /></title></head>
<body>
   <h1>Workspace : <c:out value="${workspace.name}" /></h1>
   
   <a href="../index.html">Workspaces index page</a>
   <br />
   <a href="delete.html">Delete this workspace</a>
   
   <h2>Configured Event sources</h2>
   <ul>
   <c:forEach var="eventSource" items="${workspace.eventSources}">
      <li><c:out value="${eventSource.type}" /> : <a href="<c:out value='${eventSource.url}'/>"><c:out value="${eventSource.url}"/></a></li>
   </c:forEach>
   </ul>
   <a href="eventSources/create.html">Create new Event Source</a>
</body>
</html>
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

<h2>List of badge's types (${ fn:length(badgeTypes) })</h2>
   <ul>
   <c:forEach var="badgeType" items="${badgeTypes}">
       <c:url value="/badgeTypes/${badgeType.id}.html" var="viewBadgeTypeUrl"/>
       <li><a href="${viewBadgeTypeUrl}"><c:out value="${badgeType.name}" /></a></li>
   </c:forEach>
   </ul>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>

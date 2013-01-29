
<%@ page import="com.pereposter.web.entity.User" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'pereposterUser.label', default: 'User')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-pereposterUser" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-pereposterUser" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="username" title="${message(code: 'pereposterUser.username.label', default: 'Username')}" />
					
						<g:sortableColumn property="password" title="${message(code: 'pereposterUser.password.label', default: 'Password')}" />
					
						<g:sortableColumn property="active" title="${message(code: 'pereposterUser.active.label', default: 'Active')}" />
					
						<g:sortableColumn property="accountExpired" title="${message(code: 'pereposterUser.accountExpired.label', default: 'Account Expired')}" />
					
						<g:sortableColumn property="accountLocked" title="${message(code: 'pereposterUser.accountLocked.label', default: 'Account Locked')}" />
					
						<g:sortableColumn property="enabled" title="${message(code: 'pereposterUser.enabled.label', default: 'Enabled')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${pereposterUserInstanceList}" status="i" var="pereposterUserInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${pereposterUserInstance.id}">${fieldValue(bean: pereposterUserInstance, field: "username")}</g:link></td>
					
						<td>${fieldValue(bean: pereposterUserInstance, field: "password")}</td>
					
						<td><g:formatBoolean boolean="${pereposterUserInstance.active}" /></td>
					
						<td><g:formatBoolean boolean="${pereposterUserInstance.accountExpired}" /></td>
					
						<td><g:formatBoolean boolean="${pereposterUserInstance.accountLocked}" /></td>
					
						<td><g:formatBoolean boolean="${pereposterUserInstance.enabled}" /></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${pereposterUserInstanceTotal}" />
			</div>
		</div>
	</body>
</html>

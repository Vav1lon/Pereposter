
<%@ page import="com.pereposter.web.entity.UserSocialAccount" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'userSocialAccount.label', default: 'UserSocialAccount')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-userSocialAccount" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-userSocialAccount" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list userSocialAccount">
			
				<g:if test="${userSocialAccountInstance?.createDateLastPost}">
				<li class="fieldcontain">
					<span id="createDateLastPost-label" class="property-label"><g:message code="userSocialAccount.createDateLastPost.label" default="Create Date Last Post" /></span>
					
						<span class="property-value" aria-labelledby="createDateLastPost-label"><g:fieldValue bean="${userSocialAccountInstance}" field="createDateLastPost"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${userSocialAccountInstance?.enabled}">
				<li class="fieldcontain">
					<span id="enabled-label" class="property-label"><g:message code="userSocialAccount.enabled.label" default="Enabled" /></span>
					
						<span class="property-value" aria-labelledby="enabled-label"><g:formatBoolean boolean="${userSocialAccountInstance?.enabled}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${userSocialAccountInstance?.lastPostId}">
				<li class="fieldcontain">
					<span id="lastPostId-label" class="property-label"><g:message code="userSocialAccount.lastPostId.label" default="Last Post Id" /></span>
					
						<span class="property-value" aria-labelledby="lastPostId-label"><g:fieldValue bean="${userSocialAccountInstance}" field="lastPostId"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${userSocialAccountInstance?.password}">
				<li class="fieldcontain">
					<span id="password-label" class="property-label"><g:message code="userSocialAccount.password.label" default="Password" /></span>
					
						<span class="property-value" aria-labelledby="password-label"><g:fieldValue bean="${userSocialAccountInstance}" field="password"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${userSocialAccountInstance?.socialUserId}">
				<li class="fieldcontain">
					<span id="socialUserId-label" class="property-label"><g:message code="userSocialAccount.socialUserId.label" default="Socail User Id" /></span>
					
						<span class="property-value" aria-labelledby="socialUserId-label"><g:fieldValue bean="${userSocialAccountInstance}" field="socialUserId"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${userSocialAccountInstance?.socialNetwork}">
				<li class="fieldcontain">
					<span id="socialNetwork-label" class="property-label"><g:message code="userSocialAccount.socialNetwork.label" default="Social Network" /></span>
					
						<span class="property-value" aria-labelledby="socialNetwork-label"><g:fieldValue bean="${userSocialAccountInstance}" field="socialNetwork"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${userSocialAccountInstance?.user}">
				<li class="fieldcontain">
					<span id="user-label" class="property-label"><g:message code="userSocialAccount.user.label" default="User" /></span>
					
						<span class="property-value" aria-labelledby="user-label"><g:link controller="user" action="show" id="${userSocialAccountInstance?.user?.id}">${userSocialAccountInstance?.user?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${userSocialAccountInstance?.username}">
				<li class="fieldcontain">
					<span id="username-label" class="property-label"><g:message code="userSocialAccount.username.label" default="Username" /></span>
					
						<span class="property-value" aria-labelledby="username-label"><g:fieldValue bean="${userSocialAccountInstance}" field="username"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${userSocialAccountInstance?.id}" />
					<g:link class="edit" action="edit" id="${userSocialAccountInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>

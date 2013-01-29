<%@ page import="com.pereposter.web.entity.UserSocialAccount" %>



<div class="fieldcontain ${hasErrors(bean: userSocialAccountInstance, field: 'createDateLastPost', 'error')} required">
	<label for="createDateLastPost">
		<g:message code="userSocialAccount.createDateLastPost.label" default="Create Date Last Post" />
		<span class="required-indicator">*</span>
	</label>
	
</div>

<div class="fieldcontain ${hasErrors(bean: userSocialAccountInstance, field: 'enabled', 'error')} ">
	<label for="enabled">
		<g:message code="userSocialAccount.enabled.label" default="Enabled" />
		
	</label>
	<g:checkBox name="enabled" value="${userSocialAccountInstance?.enabled}" />
</div>

<div class="fieldcontain ${hasErrors(bean: userSocialAccountInstance, field: 'lastPostId', 'error')} ">
	<label for="lastPostId">
		<g:message code="userSocialAccount.lastPostId.label" default="Last Post Id" />
		
	</label>
	<g:textField name="lastPostId" value="${userSocialAccountInstance?.lastPostId}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: userSocialAccountInstance, field: 'password', 'error')} ">
	<label for="password">
		<g:message code="userSocialAccount.password.label" default="Password" />
		
	</label>
	<g:textField name="password" value="${userSocialAccountInstance?.password}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: userSocialAccountInstance, field: 'socialUserId', 'error')} ">
	<label for="socialUserId">
		<g:message code="userSocialAccount.socialUserId.label" default="Socail User Id" />
		
	</label>
	<g:textField name="socialUserId" value="${userSocialAccountInstance?.socialUserId}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: userSocialAccountInstance, field: 'socialNetwork', 'error')} required">
	<label for="socialNetwork">
		<g:message code="userSocialAccount.socialNetwork.label" default="Social Network" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="socialNetwork" from="${com.pereposter.web.entity.SocialNetworkEnum?.values()}" keys="${com.pereposter.web.entity.SocialNetworkEnum.values()*.name()}" required="" value="${userSocialAccountInstance?.socialNetwork?.name()}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: userSocialAccountInstance, field: 'user', 'error')} required">
	<label for="user">
		<g:message code="userSocialAccount.user.label" default="User" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="user" name="user.id" from="${com.pereposter.web.entity.User.list()}" optionKey="id" required="" value="${userSocialAccountInstance?.user?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: userSocialAccountInstance, field: 'username', 'error')} ">
	<label for="username">
		<g:message code="userSocialAccount.username.label" default="Username" />
		
	</label>
	<g:textField name="username" value="${userSocialAccountInstance?.username}"/>
</div>


<%@ page import="com.pereposter.web.entity.User" %>



<div class="fieldcontain ${hasErrors(bean: pereposterUserInstance, field: 'username', 'error')} required">
	<label for="username">
		<g:message code="pereposterUser.username.label" default="Username" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="username" required="" value="${pereposterUserInstance?.username}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pereposterUserInstance, field: 'password', 'error')} required">
	<label for="password">
		<g:message code="pereposterUser.password.label" default="Password" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="password" required="" value="${pereposterUserInstance?.password}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pereposterUserInstance, field: 'active', 'error')} ">
	<label for="active">
		<g:message code="pereposterUser.active.label" default="Active" />
		
	</label>
	<g:checkBox name="active" value="${pereposterUserInstance?.active}" />
</div>

<div class="fieldcontain ${hasErrors(bean: pereposterUserInstance, field: 'accountExpired', 'error')} ">
	<label for="accountExpired">
		<g:message code="pereposterUser.accountExpired.label" default="Account Expired" />
		
	</label>
	<g:checkBox name="accountExpired" value="${pereposterUserInstance?.accountExpired}" />
</div>

<div class="fieldcontain ${hasErrors(bean: pereposterUserInstance, field: 'accountLocked', 'error')} ">
	<label for="accountLocked">
		<g:message code="pereposterUser.accountLocked.label" default="Account Locked" />
		
	</label>
	<g:checkBox name="accountLocked" value="${pereposterUserInstance?.accountLocked}" />
</div>

<div class="fieldcontain ${hasErrors(bean: pereposterUserInstance, field: 'enabled', 'error')} ">
	<label for="enabled">
		<g:message code="pereposterUser.enabled.label" default="Enabled" />
		
	</label>
	<g:checkBox name="enabled" value="${pereposterUserInstance?.enabled}" />
</div>

<div class="fieldcontain ${hasErrors(bean: pereposterUserInstance, field: 'passwordExpired', 'error')} ">
	<label for="passwordExpired">
		<g:message code="pereposterUser.passwordExpired.label" default="Password Expired" />
		
	</label>
	<g:checkBox name="passwordExpired" value="${pereposterUserInstance?.passwordExpired}" />
</div>


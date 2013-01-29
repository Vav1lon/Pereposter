<%@ page import="com.pereposter.web.entity.UserSocialAccount" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'userSocialAccount.label', default: 'UserSocialAccount')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<a href="#list-userSocialAccount" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                                        default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                              args="[entityName]"/></g:link></li>
    </ul>
</div>

<div id="list-userSocialAccount" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>

            <g:sortableColumn property="createDateLastPost"
                              title="${message(code: 'userSocialAccount.createDateLastPost.label', default: 'Create Date Last Post')}"/>

            <g:sortableColumn property="enabled"
                              title="${message(code: 'userSocialAccount.enabled.label', default: 'Enabled')}"/>

            <g:sortableColumn property="lastPostId"
                              title="${message(code: 'userSocialAccount.lastPostId.label', default: 'Last Post Id')}"/>

            <g:sortableColumn property="password"
                              title="${message(code: 'userSocialAccount.password.label', default: 'Password')}"/>

            <g:sortableColumn property="socialUserId"
                              title="${message(code: 'userSocialAccount.socialUserId.label', default: 'Socail User Id')}"/>

            <g:sortableColumn property="socialNetwork"
                              title="${message(code: 'userSocialAccount.socialNetwork.label', default: 'Social Network')}"/>

        </tr>
        </thead>
        <tbody>
        <g:each in="${userSocialAccountInstanceList}" status="i" var="userSocialAccountInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td><g:link action="show"
                            id="${userSocialAccountInstance.id}">${fieldValue(bean: userSocialAccountInstance, field: "createDateLastPost")}</g:link></td>

                <td><g:formatBoolean boolean="${userSocialAccountInstance.enabled}"/></td>

                <td>${fieldValue(bean: userSocialAccountInstance, field: "lastPostId")}</td>

                <td>${fieldValue(bean: userSocialAccountInstance, field: "password")}</td>

                <td>${fieldValue(bean: userSocialAccountInstance, field: "socialUserId")}</td>

                <td>${fieldValue(bean: userSocialAccountInstance, field: "socialNetwork")}</td>

            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${userSocialAccountInstanceTotal}"/>
    </div>
</div>
</body>
</html>

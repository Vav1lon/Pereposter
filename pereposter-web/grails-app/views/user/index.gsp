<!doctype html>

<html>

<head>
    <meta name="layout" content="indexLayout">
</head>

<body>
<div style="margin:25px">
    <a class="btn btn-success" href="${g.createLink(controller: 'user', action: 'addSocialNetwork')}"><b>Добавить социальную сеть</b></a>
</div>

<g:each in="${accounts}" var="account">
    <div class="well">
        <div class="row">
            <div class="pull-left col-md-4">
                <span class="label label-info">${account.name}</span>
                <span class="label label-default">${account.socialNetwork.name()}</span>
            </div>

            <div class="col-md-8">
                <div class="checkbox pull-right">
                    <label>
                        <input type="checkbox" name="enable" value="true" <g:if test="${account.enabled}">checked</g:if>>Включена</label>
                </div>
            </div>
        </div>
    </div>
    <br/>
</g:each>
</body>

</html>
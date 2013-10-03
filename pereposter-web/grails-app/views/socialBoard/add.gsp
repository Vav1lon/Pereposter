<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
</head>

<body>

<div style="margin: 80px">
    <div class="well well-lg">
        <form action="<g:createLink mapping="addSocial"/>" class="form-group" method='POST'>
            <h4 class="text-muted">Добавление новой социальной сети</h4>

            <p><input name="name" type="text" class="form-control" placeholder="Название для сети"></p>

            <p><g:select class="selectpicker" name="socialNetworkList" from="${com.pereposter.web.entity.SocialNetworkEnum.values()}" optionValue="serviceName" optionKey="id"/></p>

            <p><input name="login" type="text" class="form-control" placeholder="Логин"></p>

            <p><input name="password" type="password" class="form-control" placeholder="Пароль"></p>

            <button type="submit" class="btn  btn-info">Добавить</button>
        </form>
    </div>
</div>

</body>
</html>

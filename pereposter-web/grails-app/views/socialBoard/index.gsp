<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
</head>

<body>

<div class="container" style="margin: 50px">
    <a class="btn btn-lg btn-success" href="<g:createLink mapping="addSocial"/>">Добавить социальную сеть</a>
</div>

<div id="content">

    <g:each in="${socialNetworks}" var="n">
        <div class="well">
            <div class="row">
                <div class="col-md-4">${n.name} (${n.socialNetworkEnum.serviceName})</div>

                <div class="col-md-8">
                    <p>${n.username}</p>

                    <p><a class="btn btn-success" href="#">Включить</a></p>

                    <p><a class="btn btn-default" href="#">Редактировать</a></p>
                </div>
            </div>
        </div>
    </g:each>

</div>

<!-- Site footer -->
<div class="footer">
    <p>&copy; Pereposter 2013</p>
</div>

</body>
</html>

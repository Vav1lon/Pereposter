<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="../../assets/ico/favicon.png">

    <title>Justified Nav Template for Bootstrap</title>

    <r:require modules="js, css"/>
    <g:layoutHead/>
    <r:layoutResources/>
</head>

<body>

<div class="container">

    <div class="masthead">
        <h1 class="text-muted">Pereposter</h1>
        <ul class="nav nav-justified">
            <li class="active"><a href="#">Мои социальные сети</a></li>
            <sec:ifAllGranted roles="ROLE_INVITE">
                <li><a href="#">Инвайт <span class="badge">3</span></a></li>
            </sec:ifAllGranted>
            <sec:ifAllGranted roles="ROLE_ADMIN">
                <li><a href="#">Пользователи <span class="badge">42</span></a></li>
            </sec:ifAllGranted>
        </ul>
    </div>

    <g:layoutBody/>
    <r:layoutResources/>

</div> <!-- /container -->

</body>
</html>

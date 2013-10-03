<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="../../assets/ico/favicon.png">

    <title>Signin Pereposter</title>

    <link href="<g:resource file="css/bootstrap.min.css"/>" rel="stylesheet">
    <link href="<g:resource file="css/signin.css"/>" rel="stylesheet">

</head>

<body>

<div class="container">

    <g:if test='${flash.message}'>
        <div class="alert alert-danger">${flash.message}</div>
    </g:if>

    <form action='${postUrl}' method='POST' id='loginForm' autocomplete='off' class="form-signin">
        <h2 class="form-signin-heading">Please sign in</h2>
        <input type="text" class="form-control" name='j_username' id='username' placeholder="Email address" autofocus>
        <input type="password" class="form-control" name='j_password' id='password' placeholder="Password">
        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
    </form>

</div>

</body>
</html>

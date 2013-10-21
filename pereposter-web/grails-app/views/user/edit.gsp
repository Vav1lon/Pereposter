<!doctype html>

<html>

<head>
    <title>Narrow Jumbotron</title>
    <meta name="viewport" content="width=device-width">
    <link rel="stylesheet" href="https://netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
    <script type="text/javascript" src="https://netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
</head>

<body>
<div class="container">
    <div style="margin-top: 50px"></div>

    <div class="navbar navbar-default">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span>
                </button>
                <a href="#" class="navbar-brand"><b>Pereposter</b></a>
            </div>

            <div class="collapse navbar-collapse">
                <ul class="nav navbar-nav">
                    <li class="active">
                        <a href="#">Мои социальные сети</a>
                    </li>
                    <li>
                        <a href="#">Профиль</a>
                    </li>
                    <li>
                        <a href="#">Contact</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <br/>
    <br/>

    <div class="well">
        <form>
            <div class="form-group">
                <label class="control-label">Имя пользователя</label>

                <div class="controls">
                    <input type="text" class="form-control">
                </div>
            </div>

            <div class="form-group">
                <label class="control-label">Почта</label>

                <div class="controls">
                    <input type="text" class="form-control">
                </div>
            </div>

            <div class="form-group">
                <label class="control-label">Пароль</label>

                <div class="controls">
                    <input type="text" class="form-control">
                </div>
            </div>
            <hr>
            <a class="btn btn-primary btn-lg">Сохранить</a>
            <a class="btn btn-danger btn-sm">Отмена</a>
        </form>
    </div>
</div>
</body>

</html>
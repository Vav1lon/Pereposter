<!doctype html>

<html>

<head>
    <meta name="layout" content="indexLayout">
</head>

<body>
<div style="margin:25px">
    <a class="btn btn-success disabled" href=""><b>Добавить социальную сеть</b></a>
</div>

<div class="well">
        <form>
            <div class="form-group">
                <label class="control-label">Название сети</label>

                <div class="controls">
                    <input type="text" class="form-control">
                </div>
            </div>

            <g:select class="form-control" name="socialNetwork" from="${com.pereposter.entity.SocialNetworkEnum.values()}" optionKey="id" optionValue="serviceName"/>

            <div class="form-group">
                <label class="control-label">Login</label>

                <div class="controls">
                    <input type="text" class="form-control">
                </div>
                <label class="control-label">Password</label>

                <div class="controls">
                    <input type="text" class="form-control">
                </div>
            </div>

            <div class="checkbox">
                <label>
                    <input type="checkbox" value="true"> Сеть включена
                </label>
            </div>
            <a class="btn btn-warning">Validate / Check data</a><hr>
            <a class="btn btn-primary btn-lg">Сохранить</a>
            <a class="btn btn-danger btn-sm">Отмена</a>
        </form>
    </div>
</div>
</body>

</html>
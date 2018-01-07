<!DOCTYPE html>
<html class="has-navbar-fixed-top">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>School CRM</title>
    <link rel="icon" type="image/svg+xml" href="/favicon.svg">
    <!--Import Google Icon Font-->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <!--Import materialize.css-->
    <link rel="stylesheet" href="/webjars/materializecss/0.100.2/css/materialize.min.css">

</head>
<body>
<nav>
    <div class="nav-wrapper"><a href="/" class="brand-logo"></a>
        <ul>
            <li><a href="/">На главную</a></li>
        </ul>
    </div>
</nav>
<div class="container">
    <div class="row">
        <form action="/auth" method="post" class="col s6 offset-s3">
            <p class="flow-text">Вход в систему</p>
            <div class="row">
                <div class="input-field ">
                    <input id="username" type="text" name="username" required
                           <#if params['username']??>value="${params['username']}" </#if>>
                    <label for="username">Логин</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field ">
                    <input id="password" type="password" <#if params['error']??>class="invalid" </#if> name="password"
                           required>
                    <label for="password"
                           <#if params['error']??>data-error="Имя&nbsp;пользователя&nbsp;или&nbsp;пароль&nbsp;введены&nbsp;неверно"</#if>>Пароль</label>
                </div>
            </div>
            <div class="row">
                <button class="btn waves-effect waves-light" type="submit" name="action">Войти
                    <i class="material-icons right">send</i>
                </button>
            </div>
            <input type="hidden" value="${csrf}" name="csrf">
        </form>
        <div class="col s4"></div>
    </div>
</div>
<script src="/webjars/jquery/3.2.1/jquery.min.js"></script>
<script src="/webjars/materializecss/0.100.2/js/materialize.min.js"></script>
</body>
</html>
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
        <ul class="right">
        <#if !loggedIn>
            <li><a href="/private">Войти</a></li>
        <#else >
            <li><a href="/logout">Выйти</a></li>
        </#if>
        </ul>
    </div>
</nav>
<div class="container">
    <h1>Добро пожаловать</h1>
    <p class="flow-text">Спасибо, что зашли на нашу страничку! Здесь вы можете добавить информацию о себе и ребёнке,
        учащемся в нашей школе в базу рассылок. Если у нас будет информация у вас вы сможете получать все нашин
        информационные рассылки. </p>
    <p class="flow-text">Обещаем никому не отдавать ваши данные!</p>
    <div class="row">
        <form action="/subscribe" class="col s12" method="post">
            <div class="row">
                <div class="input-field col s4">
                    <input id="firstname" type="text" class="validate" required>
                    <label for="firstname">Имя<span style="color: crimson">*</span></label>
                </div>
                <div class="input-field col s4">
                    <input id="lastname" type="text" class="validate" required>
                    <label for="lastname">Фамилия<span style="color: crimson">*</span></label>
                </div>
                <div class="input-field col s4">
                    <input id="patronymic" type="text" class="validate">
                    <label for="patronymic">Отчество</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s4">
                    <i class="material-icons prefix">contact_mail</i>
                    <input type="email" class="validate" name="email" id="email" required>
                    <label for="email" data-error="Некорректный адрес">Email<span
                            style="color: crimson">*</span></label>
                </div>
                <div class="input-field col s4">
                    <input type="number" class="validate" required min="1" max="11" id="classno" name="classno">
                    <label for="classno">Номер класса<span style="color: crimson">*</span></label>
                </div>
                <div class="input-field col s4">
                    <input type="text" class="validate" required pattern="^[А-ЯЁа-яё]{1}$">
                    <label for="">Буква класса<span style="color: crimson">*</span></label>
                </div>
            </div>
            <div class="row">
                <button class="btn waves-effect waves-light" type="submit" name="action">Зарегистрировать
                    <i class="material-icons right">send</i>
                </button>
            </div>
        </form>
    </div>
</div>
<script src="/webjars/jquery/3.2.1/jquery.min.js"></script>
<script src="/webjars/materializecss/0.100.2/js/materialize.min.js"></script>
</body>
</html>
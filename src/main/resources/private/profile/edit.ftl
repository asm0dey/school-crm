<#-- @ftlvariable name="editorEnabled" type="boolean" -->
<#-- @ftlvariable name="profile" type="org.pac4j.core.profile.CommonProfile" -->
<#-- @ftlvariable name="data" type="org.ort.school.app.repo.UserDTO" -->
<!DOCTYPE html>
<html class="has-navbar-fixed-top">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>School CRM</title>
    <link rel="icon" type="image/svg+xml" href="/favicon.svg">
    <!--Import Google Icon Font-->
    <link href="//fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <!--Import materialize.css-->
    <link rel="stylesheet" href="/webjars/materializecss/1.0.0-rc.2/css/materialize.min.css">

</head>
<body>
<ul id="dropdown1" class="dropdown-content">
    <li class="disabled"><a href="/private/user/${profile.id}/edit">Профиль</a></li>
    <li><a href="/logout">Выйти</a></li>
</ul>
<nav class="nav-extended">
    <div class="nav-wrapper">
        <a href="/" class="brand-logo"></a>
        <ul>
            <li><a href="/">На главную</a></li>
        </ul>

        <ul class="right">
            <#if profile.roles?seq_contains('admin')>
            <li><a href="/private/admin/users">Пользователи</a></li>
            <li><a href="/private/admin/degrees">Классы</a></li>
            <li>
                <a class="dropdown-button" href="/private/user/${profile.id}" data-activates="dropdown1">
                    <#if profile.displayName?has_content>${profile.displayName}<#else>${profile.id}</#if>
                    <i class="material-icons right">arrow_drop_down</i>
                </a>
            </li>
            </#if>
        </ul>
    </div>

</nav>
<div class="container">
    <h1>Редактирование пользователя</h1>
    <div class="row">
        <form action="/private/user/${data.username}/edit" class="col s12" method="post">
            <div class="row">
                <div class="input-field col s4">
                    <input id="lastname" type="text" class="validate" name="lastname"
                           <#if data.lastname??>value="${data.lastname}"</#if>>
                    <label for="lastname">Фамилия</label>
                </div>
                <div class="input-field col s4">
                    <input id="firstname" type="text" class="validate" name="firstname"
                           <#if data.firstName??>value="${data.firstName}"</#if>>
                    <label for="firstname">Имя</label>
                </div>
                <div class="input-field col s4">
                    <input id="patronymic" type="text" class="validate" name="patronymic"
                           <#if data.patronymic??>value="${data.patronymic}"</#if>>
                    <label for="patronymic">Отчество</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s6">
                    <input id="password" type="password" class="validate" name="password">
                    <label for="password">Пароль</label>
                </div>
                <div class="input-field col s6">
                    <input id="passwordConfirm" type="password" class="validate" name="passwordConfirm">
                    <label for="passwordConfirm">Пароль</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s6">
                    <input id="email" type="email" class="validate" name="email"
                    <#if data.email??>value="${data.email}"</#if>>
                    <label for="email">EMail</label>
                </div>
                <div class="input-field col s6">
                    <select id="role" name="role">
                        <option value="no" disabled>Выберите роль</option>
                        <option value="admin"
                                <#if profile.roles?seq_contains('author')>disabled</#if>
                                <#if data.role == 'admin'>selected</#if>>
                            Администратор
                        </option>
                        <option value="author"
                            <#if !editorEnabled></#if>disabled<#if data.role == 'author'>selected</#if>>
                            Редактор
                        </option>
                    </select>
                    <label for="role">Роль</label>
                </div>
            </div>
            <input type="hidden" value="${csrf}" name="csrf">
            <div class="row">
                <button class="btn waves-effect waves-light" type="submit">Обновить
                    <i class="material-icons right">send</i>
                </button>
            </div>
        </form>
    </div>
</div>
<script src="/webjars/jquery/3.3.1-1/jquery.min.js"></script>
<script src="/webjars/materializecss/1.0.0-rc.2/js/materialize.min.js"></script>
<script>
    $(document).ready(function () {
        $(".dropdown-button").dropdown();
        $('select').material_select();
    })
</script>
</body>
</html>
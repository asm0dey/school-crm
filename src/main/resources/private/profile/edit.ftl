<#-- @ftlvariable name="editorEnabled" type="boolean" -->
<#-- @ftlvariable name="profile" type="org.pac4j.core.profile.CommonProfile" -->
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
    <link rel="stylesheet" href="/webjars/materializecss/0.100.2/css/materialize.min.css">

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
            <li><a href="/private/admin/users">Пользователи</a></li>
            <li><a href="/private/admin/degrees">Классы</a></li>
            <li>
                <a class="dropdown-button" href="/private/user/${profile.id}" data-activates="dropdown1">
                    <#if profile.displayName?has_content>${profile.displayName}<#else>${profile.id}</#if>
                    <i class="material-icons right">arrow_drop_down</i>
                </a>
            </li>
        </ul>
    </div>

</nav>
<div class="container">
    <h1>Редактирование пользователя</h1>
    <div class="row">
        <form action="/private/user/${profile.id}/update" class="col s12" method="post">
            <div class="row">
                <div class="input-field col s4">
                    <input id="lastname" type="text" class="validate" name="lastname"
                           <#if profile.firstName??>value="${profile.firstName}"</#if>>
                    <label for="lastname">Фамилия<span style="color: crimson">*</span></label>
                </div>
                <div class="input-field col s4">
                    <input id="firstname" type="text" class="validate" name="firstname"
                           <#if profile.familyName??>value="${profile.familyName}"</#if>>
                    <label for="firstname">Имя<span style="color: crimson">*</span></label>
                </div>
                <div class="input-field col s4">
                    <input id="patronymic" type="text" class="validate" name="patronymic"
                       <#if profile.attributes['patronymic']??>value="${profile.attributes['patronymic']}"</#if>>
                    <label for="patronymic">Отчество</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s4">
                    <input id="password" type="password" class="validate" name="password">
                    <label for="password">Пароль</label>
                </div>
                <div class="input-field col s4">
                    <input id="passwordConfirm" type="password" class="validate" name="passwordConfirm">
                    <label for="passwordConfirm">Пароль</label>
                </div>
                <div class="input-field col s4">
                    <select id="role" name="role">
                        <option value="no" disabled>Выберите роль</option>
                        <option value="admin" <#if profile.roles?seq_contains('author')>disabled</#if>
                                <#if profile.roles?seq_contains('admin')>selected</#if>>Администратор
                        </option>
                        <option value="author" <#if !editorEnabled></#if>disabled<#if profile.roles?seq_contains('author')>selected</#if>>Редактор
                        </option>
                    </select>
                    <label for="role">Роль</label>
                </div>

            </div>
            <div class="row">
                <button class="btn waves-effect waves-light" type="submit" name="action">Обновить
                    <i class="material-icons right">send</i>
                </button>
            </div>
        </form>
    </div>
</div>
<script src="/webjars/jquery/3.2.1/jquery.min.js"></script>
<script src="/webjars/materializecss/0.100.2/js/materialize.min.js"></script>
<script>
    $(document).ready(function () {
        $(".dropdown-button").dropdown();
        $('select').material_select();
    })
</script>
</body>
</html>
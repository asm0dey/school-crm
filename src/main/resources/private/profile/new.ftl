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
    <h1>Создание пользователя</h1>
    <div class="row">
        <form action="/private/user/new" class="col s12" method="post" enctype="application/x-www-form-urlencoded">
            <div class="row">
                <div class="input-field col s4">
                    <input id="lastname" type="text" class="validate <#if errors?? && errors['lastname']??>invalid</#if>" name="lastname" required>
                    <label for="lastname" <#if errors?? && errors['lastname']??>data-error="${errors['lastname']}"</#if>>
                        Фамилия
                    </label>
                </div>
                <div class="input-field col s4">
                    <input id="firstname" type="text" class="validate <#if errors?? && errors['firstname']??>invalid</#if>" name="firstname" required>
                    <label for="firstname" <#if errors?? && errors['firstname']??>data-error="${errors['firstname']}" </#if>>
                        Имя
                    </label>
                </div>
                <div class="input-field col s4">
                    <input id="patronymic" type="text" class="validate <#if errors?? && errors['patronymic']??>invalid</#if>" name="patronymic">
                    <label for="patronymic"<#if errors?? && errors['patronymic']??> data-error="${errors['patronymic']}"</#if>>
                        Отчество
                    </label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s4">
                    <input id="username" type="text" class="validate <#if errors?? && errors['username']??>invalid</#if>" name="username" minlength="6" required>
                    <label for="username" <#if errors?? && errors['username']??>data-error="${errors['username']}"</#if>>
                        Имя пользователя
                    </label>
                </div>
                <div class="input-field col s4">
                    <input id="email" type="email" class="validate <#if errors?? && errors['email']??>invalid</#if>" name="email" required>
                    <label for="email" <#if errors?? && errors['email']??>data-error="${errors['email']}"</#if>>
                        Email
                    </label>
                </div>
                <div class="input-field col s4">
                    <select id="role" name="role" required class="validate <#if errors?? && errors['role']??>invalid</#if>">
                        <option value="no" disabled selected>Выберите роль</option>
                        <option value="admin"
                                <#if profile.roles?seq_contains('author')>disabled</#if>>
                            Администратор
                        </option>
                        <option value="author" <#if !editorEnabled>disabled</#if>>
                            Редактор
                        </option>
                    </select>
                    <label for="role" <#if errors?? && errors['role']??>data-error="${errors['role']}"</#if>>
                        Роль
                    </label>
                </div>

            </div>
            <div class="row">
                <div class="input-field col s6">
                    <input id="password" type="password" class="validate <#if errors?? && errors['password']??>invalid</#if>" name="password" minlength="6" required>
                    <label for="password" <#if errors?? && errors['password']??>data-error="${errors['password']}"</#if>>
                        Пароль
                    </label>
                </div>
                <div class="input-field col s6">
                    <input id="passwordConfirm" type="password" class="validate <#if errors?? && errors['validPassword']??>invalid</#if>" name="passwordConfirm" minlength="6" required>
                    <label for="passwordConfirm" <#if errors?? && errors['validPassword']??>data-error="${errors['validPassword']}"</#if>>
                        Повторите пароль
                    </label>
                </div>
            </div>
            <input type="hidden" value="${csrf}" name="csrf">
            <div class="row">
                <button class="btn waves-effect waves-light" type="submit">Создать
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
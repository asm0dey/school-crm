<#-- @ftlvariable name="degrees" type="java.util.List<kotlin.Pair<java.lang.Integer, java.lang.String>>" -->
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
    <link rel="stylesheet" href="/webjars/materializecss/1.0.0/css/materialize.min.css">

</head>
<body>
<#if profile??>
<ul id="dropdown1" class="dropdown-content">
    <li class="disabled"><a href="/private/user/${profile.id}/edit">Профиль</a></li>
    <li><a href="/logout">Выйти</a></li>
</ul>
</#if>
<nav>
    <div class="nav-wrapper"><a href="/" class="brand-logo"></a>
        <ul>
            <li><a href="/">На главную</a></li>
        </ul>
        <ul class="right">
        <#if !loggedIn>
            <li><a href="/private">Войти</a></li>
        <#else >

            <#if profile.roles?seq_contains('admin')>
            <li><a href="/private/admin/users">Пользователи</a></li>
            <li><a href="/private/admin/degrees">Классы</a></li>
            <li>
                <a class="dropdown-trigger" href="/private/user/${profile.id}" data-target="dropdown1">
                    <#if profile.displayName?has_content>${profile.displayName}<#else>${profile.id}</#if>
                    <i class="material-icons right">arrow_drop_down</i>
                </a>
            </li>
            <#elseif profile.roles?seq_contains('author')>
            <li><a href="/private/author">Создать рассылку</a></li>
            <li>
                <a class="dropdown-trigger" href="/private/user/${profile.id}" data-target="dropdown1">
                    <#if profile.displayName?has_content>${profile.displayName}<#else>${profile.id}</#if>
                    <i class="material-icons right">arrow_drop_down</i>
                </a>
            </li>
            </#if>
        </#if>
        </ul>
    </div>
</nav>
<h1>Ошибка</h1>
<h3>${status}: ${reason}</h3>
<script src="/webjars/jquery/3.3.1-1/jquery.min.js"></script>
<script src="/webjars/materializecss/1.0.0/js/materialize.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        var selects = document.querySelectorAll('select');
        var instances = M.FormSelect.init(selects);
        var dropdowns = document.querySelectorAll('.dropdown-trigger');
        var instances2 = M.Dropdown.init(dropdowns);

    });
</script>
</body>
</html>
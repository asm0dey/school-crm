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
    <li><a href="/private/user/${profile.id}/edit">Профиль</a></li>
    <li><a href="/logout">Выйти</a></li>
</ul>
<nav class="nav-extended">
    <div class="nav-wrapper">
        <a href="/" class="brand-logo"></a>
        <ul>
            <li><a href="/">На главную</a></li>
        </ul>
        <ul class="right">
            <li <#if part == 'users'>class="active" </#if>><a href="/private/admin/users">Пользователи</a></li>
            <li <#if part == 'degrees'>class="active" </#if>><a href="/private/admin/degrees">Классы</a></li>

            <li>
                <a class="dropdown-button" href="#!" data-activates="dropdown1">
                    <#if profile.displayName?has_content>${profile.displayName}<#else>${profile.id}</#if>
                    <i class="material-icons right">arrow_drop_down</i>
                </a>
            </li>
        </ul>
    </div>

</nav>
<div>
    <div class="row">
        <div class="col s10 offset-s1">
            <#include "admin/"+part+".ftl">
        </div>
    </div>
</div>
<script src="/webjars/jquery/3.3.1-1/jquery.min.js"></script>
<script src="/webjars/materializecss/1.0.0-rc.2/js/materialize.min.js"></script>
<script>
    $(document).ready(function () {
        $(".dropdown-button").dropdown();
        $('select').material_select();
        $('.modal').modal();
    })
</script>
</body>
</html>
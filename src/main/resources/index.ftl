<#-- @ftlvariable name="errors" type="java.util.Map<java.lang.String, java.lang.String>" -->
<#-- @ftlvariable name="session" type="org.jooby.Session" -->
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
    <link rel="stylesheet" href="/webjars/materializecss/1.0.0-rc.2/css/materialize.min.css">

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
            <#elseif profile.roles?seq_contains('author')>
            <li><a href="/private/author">Создать рассылку</a></li>
            </#if>
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
    <h1>Добро пожаловать</h1>
    <p class="flow-text">Спасибо, что зашли на нашу страничку! Здесь вы можете добавить информацию о себе и ребёнке,
        учащемся в нашей школе в базу рассылок. Если у нас будет информация у вас вы сможете получать все нашин
        информационные рассылки. </p>
    <p class="flow-text">Обещаем никому не отдавать ваши данные!</p>
    <div class="row">
        <form action="/" class="col s12" method="post">
            <div class="row">
                <div class="input-field col s4">
                    <input id="parent-lastname" type="text"
                           class="validate <#if errors?? && errors['parent.lastname']??>invalid</#if>" required
                           name="parent.lastname">
                    <label for="parent-lastname"
                           <#if  errors?? && errors['parent.lastname']??>data-error="${errors['parent.lastname']}" </#if>>Ваша
                        фамилия<span style="color: crimson">*</span></label>
                </div>
                <div class="input-field col s4">
                    <input id="parent-firstname" type="text"
                           class="validate <#if errors?? && errors['parent.firstname']??>invalid</#if>"
                           required name="parent.firstname">
                    <label for="parent-firstname"
                           <#if  errors?? && errors['parent.firstname']??>data-error="${errors['parent.firstname']}" </#if>>Ваше
                        имя<span style="color: crimson">*</span></label>
                </div>
                <div class="input-field col s4">
                    <input id="parent-patronymic" type="text"
                           name="parent.patronymic"
                           class="<#if errors?? && errors['parent.patronymic']??>invalid</#if>">
                    <label for="parent-patronymic"
                           <#if  errors?? && errors['parent.patronymic']??>data-error="${errors['parent.patronymic']}" </#if>>Ваше
                        отчество</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s4">
                    <input id="student-lastname" type="text"
                           class="validate <#if errors?? && errors['student.lastname']??>invalid</#if>" required
                           name="student.lastname">
                    <label for="student-lastname"
                           <#if  errors?? && errors['student.lastname']??>data-error="${errors['student.lastname']}" </#if>>Фамилия
                        ученика<span style="color: crimson">*</span></label>
                </div>
                <div class="input-field col s4">
                    <input id="student-firstname" type="text"
                           class="validate <#if errors?? && errors['student.firstname']??>invalid</#if>" required
                           name="student.firstname">
                    <label for="student-firstname"
                           <#if  errors?? && errors['student.firstname']??>data-error="${errors['student.firstname']}" </#if>>Имя
                        ученика<span style="color: crimson">*</span></label>
                </div>
                <div class="input-field col s4">
                    <input id="student-patronymic" type="text"
                           class="validate <#if errors?? && errors['student.patronymic']??>invalid</#if>"
                           name="student.patronymic">
                    <label for="student-patronymic"
                           <#if  errors?? && errors['student.patronymic']??>data-error="${errors['student.patronymic']}" </#if>>Отчество
                        ученика</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s6">
                    <input type="email" class="validate <#if errors?? && errors['parent.email']??>invalid</#if>"
                           name="parent.email" id="email" required>
                    <label for="email"
                           <#if  errors?? && errors['parent.email']??>data-error="${errors['parent.email']}" </#if>> Ваш
                        Email<span
                                style="color: crimson">*</span></label>
                </div>
                <div class="input-field col s6">
                    <select name="degreeNo" id="classno"
                            class="validate <#if errors?? && errors['degreeNo']??>invalid</#if>" required>
                        <option value="no" disabled>Выберите номер класса</option>
                        <#list degrees as degree>
                        <option value="${degree.first}">${degree.second}</option>
                        </#list>
                    </select>
                    <label for="classno"
                           <#if  errors?? && errors['degreeNo']??>data-error="${errors['degreeNo']}" </#if>>Класс<span
                            style="color: crimson">*</span></label>
                </div>
            </div>
            <input type="hidden" value="${csrf}" name="csrf">
            <div class="row">
                <button class="btn waves-effect waves-light" type="submit">Зарегистрировать
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
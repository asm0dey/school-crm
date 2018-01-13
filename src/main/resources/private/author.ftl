<#-- @ftlvariable name="path" type="java.lang.String" -->
<#-- @ftlvariable name="degrees" type="java.util.List<kotlin.Pair<java.lang.Integer, java.lang.String>>" -->
<!DOCTYPE html>
<html class="has-navbar-fixed-top">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>School CRM</title>
    <link rel="icon" type="image/svg+xml" href="/favicon.svg">
    <link href="//fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="/webjars/materializecss/0.100.2/css/materialize.min.css">
    <link href="/webjars/summernote/0.8.9/dist/summernote-lite.css" rel="stylesheet">

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
                <a class="dropdown-button" href="/private/user/${profile.id}" data-activates="dropdown1">
                    <#if profile.displayName?has_content>${profile.displayName}<#else>${profile.id}</#if>
                    <i class="material-icons right">arrow_drop_down</i>
                </a>
            </li>
            <#elseif profile.roles?seq_contains('author')>
            <li <#if path?contains('/author')>class="active" </#if>><a href="/private/author">Создать рассылку</a></li>
            <li>
                <a class="dropdown-button" href="/private/user/${profile.id}" data-activates="dropdown1">
                    <#if profile.displayName?has_content>${profile.displayName}<#else>${profile.id}</#if>
                    <i class="material-icons right">arrow_drop_down</i>
                </a>
            </li>
            </#if>
        </#if>
        </ul>
    </div>
</nav>
<div class="container">
    <div class="row">
        <h2>Создание рассылки</h2>
        <p>Вы можете создавать рассылку как обычное письмо. Единственное, что нужно иметь ввиду — это что вместо ФИО
            родителя надо нажимать кнопку «ФИО», а вместо номера класса надо нажимать на кнопку «№». Удачных рассылок
            вам!</p>
        <form action="/private/author" class="col s12" method="post">
            <div class="row">
                <#list degrees as degree>
                    <div class="col s2">
                        <input type="checkbox" name="d-${degree.first}" id="degree-${degree?index}">
                        <label for="degree-${degree?index}">${degree.second}</label>
                    </div>
                </#list>
            </div>
            <div class="row input-field">
                <div class="col s12">
                    <input type="text" name="subject" id="subject" class="validate" required>
                    <label for="subject">Тема сообщения</label>
                </div>
            </div>
            <div class="row input-field">
                <textarea required name="content" id="summernote" class="col s12 validate"></textarea>
            </div>
            <input type="hidden" value="${csrf}" name="csrf">
            <div class="row">
                <button class="btn waves-effect waves-light" type="submit">Отправить
                    <i class="material-icons right">send</i>
                </button>
            </div>
        </form>
    </div>
</div>

<script src="/webjars/jquery/3.2.1/jquery.min.js"></script>
<script src="/webjars/materializecss/0.100.2/js/materialize.min.js"></script>
<script src="/webjars/summernote/0.8.9/dist/summernote-lite.js"></script>
<script>
    $(document).ready(function () {
        $(".dropdown-button").dropdown();
        $('select').material_select();
        <#if flash?? && flash.success?? && flash.success == 'OK'>Materialize.toast("Рассылка успешно выполнена")</#if>
    });
    const FioButton = function (context) {
        const ui = $.summernote.ui;

        const button = ui.button({
            contents: 'ФИО',
            tooltip: 'ФИО родителя',
            click: function () {
                context.invoke('editor.insertText', '%recipient.fullname%');
            }
        });

        return button.render();
    };
    const DegreeButton = function (context) {
        const ui = $.summernote.ui;

        const button = ui.button({
            contents: '№',
            tooltip: 'Класс',
            click: function () {
                context.invoke('editor.insertText', '%recipient.degree%');
            }
        });

        return button.render();
    };
    $('#summernote').summernote({
        buttons: {
            fio: FioButton,
            degree: DegreeButton
        },
        toolbar: [
            ['style', ['bold', 'italic', 'underline', 'clear']],
            ['font', ['strikethrough', 'superscript', 'subscript']],
            ['fontsize', ['fontsize', 'style']],
            ['color', ['color']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['height', ['height']],
            ['inset', ['table', 'picture', 'hr']],
            ['cutom', ['fio', 'degree']]
        ],
        tabsize: 2,
        height: 400
    });
</script>
</body>
</html>
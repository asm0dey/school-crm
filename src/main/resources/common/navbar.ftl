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
        <#else>
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
            <li><a href="/private/author">Создать рассылку</a></li>
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
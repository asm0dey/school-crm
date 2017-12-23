<table class="highlight responsive-table">
    <thead>
    <tr>
        <td width="20%">Имя пользователя</td>
        <td width="40%">Полное имя</td>
        <td width="20%">Роль</td>
        <td></td>
    </tr>
    </thead>
    <tbody>
    <#list users as user>
    <tr>
        <td>${user.username}</td>
        <td>${user.displayName}</td>
        <td><#if user.role=='admin'>Администратор<#else>Автор</#if></td>
        <td><a class="waves-effect waves-light btn" href="/private/user/${user.username}/edit"><i
                class="material-icons left">edit</i>Изменить</a></td>
    </tr>

    </#list>
    </tbody>
</table>
<a href="/private/user/new" class="waves-effect waves-light btn-large"><i class="material-icons left">add</i>Создать нового</a>
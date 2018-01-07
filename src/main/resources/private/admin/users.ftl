<#-- @ftlvariable name="users" type="java.util.Collection<org.ort.school.app.repo.UserDTO>" -->
<table class="highlight responsive-table">
    <thead>
    <tr>
        <td width="20%">Имя пользователя</td>
        <td width="58%">Полное имя</td>
        <td width="20%">Роль</td>
        <td width="1%"></td>
        <td width="1%"></td>
    </tr>
    </thead>
    <tbody>
    <#list users as user>
    <tr>
        <td>${user.username}</td>
        <td>${user.displayName}</td>
        <td><#if user.role=='admin'>Администратор<#else>Автор</#if></td>
        <td><a class="waves-effect waves-light btn-floating" href="/private/user/${user.username}/edit"
               title="Изменить"><i
                class="material-icons">edit</i></a></td>
        <td><a class="waves-effect waves-light btn-floating red" onclick="deleteUser('${user.username}')"
               title="Удалить"><i
                class="material-icons">delete</i></a></td>
    </tr>

    </#list>
    </tbody>
</table>
<a href="/private/user/new" class="waves-effect waves-light btn-large"><i class="material-icons left">add</i>Создать нового</a>
<script type="application/javascript">
    const deleteUser = function (username) {
        if (confirm("Вы уверены что пользователя " + username + " нужно удалить?")) {
            $.ajax({
                url: "/private/user/" + username + "/delete",
                type: 'DELETE',
                success: function (result) {
                    window.location.reload(true)
                }
            })
        }
    };
</script>
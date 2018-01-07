<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Настройка приложения</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bulma/0.6.1/css/bulma.min.css">
</head>
<body>
<section class="section">
    <div class="container">
        <div class="card">
            <header class="card-header">
                <p class="card-header-title">Первый запуск</p>
            </header>
            <div class="card-content">
                <p>Похоже, что это первый раз, когда вы запускаете CRM.</p>
                <p>Чтобы система заработала в ней надо создать учётную запись адинистратора</p>
                <p></p>
                <form action="/init" method="post">
                    <div class="field">
                        <label for="username" class="label">Имя пользователя</label>
                        <div class="control"><input type="text" class="input" id="username" name="username"></div>
                        <#if errors?? && errors['username']??>
                            <p class="help is-danger">${errors['username']}</p>
                        </#if>
                    </div>

                    <div class="field">
                        <label for="email" class="label">Email</label>
                        <div class="control"><input type="email" class="input" id="email" name="email"></div>
                        <#if errors?? && errors['email']??>
                            <p class="help is-danger">${errors['email']}</p>
                        </#if>

                    </div>

                    <div class="field">
                        <label for="password" class="label">Пароль</label>
                        <div class="control"><input type="password" class="input" id="password" name="password"></div>
                        <#if errors?? && errors['password']??>
                            <p class="help is-danger">${errors['password']}</p>
                        </#if>
                    </div>

                    <div class="field">
                        <label for="passwordConfirm" class="label">Повторите пароль</label>
                        <div class="control"><input id="passwordConfirm" type="password" class="input" name="passwordConfirm"></div>
                        <#if errors?? && errors['valid']??>
                            <p class="help is-danger">${errors['valid']}</p>
                        </#if>
                    </div>

                    <div class="control">
                        <button class="button isLink" type="submit">Отправить</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</section>
</body>
</html>
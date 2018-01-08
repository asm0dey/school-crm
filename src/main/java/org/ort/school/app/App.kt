package org.ort.school.app

import com.google.inject.TypeLiteral
import org.jooby.*
import org.jooby.flyway.Flywaydb
import org.jooby.ftl.Ftl
import org.jooby.handlers.CsrfHandler
import org.jooby.hbv.Hbv
import org.jooby.jdbc.Jdbc
import org.jooby.jooq.jOOQ
import org.jooby.pac4j.Auth
import org.jooby.pac4j.AuthSessionStore
import org.ort.school.app.repo.DegreeRepo
import org.ort.school.app.repo.UserRepo
import org.ort.school.app.routes.*
import org.ort.school.app.service.*
import org.pac4j.core.profile.CommonProfile


/**
 * Kotlin stater project.
 */
class App : Kooby({
    modules()
    repositoriies()
    services()
    filters()
    unsecureControllers()
    err { _, rsp, err ->
        val require = require(CommonProfile::class.java)
        rsp.send(
                Results
                        .html("/public/error")
                        .put("status", err.statusCode())
                        .put("profile", require)
                        .put("reason", when (err.statusCode()) {
                            403 -> "Недостаточно прав"
                            404 -> "Страница не найдена"
                            else -> "Неизвестная ошибка"
                        })
                        .status(err.statusCode())
        )
    }
    use(Auth().form("/private/**", DBAuth::class.java))
    secureControllers()
})

private fun Kooby.secureControllers() {
    use(User::class)
    use(Degree::class)
    use(Author::class)
    use(Private::class)
}

fun Kooby.services() {
    use(PasswordService::class)
    use(UserService::class)
    use(DegreeService::class)
    use(SubscribeService::class)
}


private fun Kooby.unsecureControllers() {
    use(Main::class)
    use(Login::class)
}

private fun Kooby.filters() {
    use("*", RequestLogger().latency().extended().queryString())
    assets("/webjars/**", "/META-INF/resources/webjars/{0}")
    assets("favicon*", "/")
    use("*", CsrfHandler())
    use("*") { req, resp, chain ->
        val loggedIn = req.session().get(Auth.ID).toOptional().isPresent
        req.set("loggedIn", loggedIn)
        if (loggedIn) {
            val sessionStore = require(object : TypeLiteral<AuthSessionStore<CommonProfile>>() {})
            val profile = sessionStore.get(req.session().get(Auth.ID).value()).get()
            req.set("profile", profile)
        } else {
            req.unset<CommonProfile>("profile")
        }
        chain.next(req, resp)
    }
}

private fun Kooby.repositoriies() {
    use(UserRepo::class)
    use(DegreeRepo::class)
}

private fun Kooby.modules() {
    use(Jdbc())
    use(Flywaydb())
    use(jOOQ())
    use(Ftl("/", ".ftl"))
    use(Hbv())
    use(FlashScope())
}


/**
 * Run application:
 */
fun main(args: Array<String>) {
    run(::App, *args)
}

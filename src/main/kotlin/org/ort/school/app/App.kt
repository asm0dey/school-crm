package org.ort.school.app

import com.google.inject.TypeLiteral
import org.jooby.Kooby
import org.jooby.caffeine.CaffeineCache
import org.jooby.flyway.Flywaydb
import org.jooby.ftl.Ftl
import org.jooby.hbv.Hbv
import org.jooby.jdbc.Jdbc
import org.jooby.jooq.jOOQ
import org.jooby.pac4j.Auth
import org.jooby.pac4j.AuthSessionStore
import org.jooby.run
import org.ort.school.app.repo.UserRepo
import org.ort.school.app.routes.Login
import org.ort.school.app.routes.Main
import org.ort.school.app.routes.Private
import org.ort.school.app.routes.User
import org.ort.school.app.service.DBAuth
import org.pac4j.core.profile.CommonProfile
import org.jooby.csl.XSS




/**
 * Kotlin stater project.
 */
class App : Kooby({
    modules()
    repositoriies()
    unsecureControllers()

    use(Auth().form("*", DBAuth::class.java).logout("/logout", "/"))
    use(User::class)
    use(Private::class)
})


private fun Kooby.unsecureControllers() {
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
    use(Main::class)
    use(Login::class)
}

private fun Kooby.repositoriies() {
    use(UserRepo::class)
}

private fun Kooby.modules() {
    use(Jdbc())
    use(Flywaydb())
    use(jOOQ())
    use(CaffeineCache.newCache())
    use(Ftl("/", ".ftl"))
    use(Hbv())
    assets("/webjars/**", "/META-INF/resources/webjars/{0}")
    use(XSS())
}


/**
 * Run application:
 */
fun main(args: Array<String>) {
    run(::App, *args)
}

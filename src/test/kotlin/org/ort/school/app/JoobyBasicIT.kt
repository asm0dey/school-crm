package org.ort.school.app

import com.github.kittinunf.fuel.httpGet
import com.winterbe.expekt.should
import io.kotlintest.Spec
import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import org.testcontainers.containers.PostgreSQLContainer
import java.net.ServerSocket

class KPostgreSQLContainer : PostgreSQLContainer<KPostgreSQLContainer>()


abstract class JoobyIntegrationSpec(body: JoobyIntegrationSpec.(port: Int) -> Unit = {}) : BehaviorSpec() {
    private val postgres = KPostgreSQLContainer().apply { start() }
    val httpPort = ServerSocket(0).use { it.localPort }
    private val httpsPort = ServerSocket(0).use { it.localPort }
    private val app = SchoolCRM()

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        app.start(
                "server.join=false",
                "application.port=$httpPort",
                "application.securePort=$httpsPort",
                "db.url=${postgres.jdbcUrl.replace("\\?.*".toRegex(), "")}",
                "db.user=${postgres.username}",
                "db.password=${postgres.password}"
        )
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
        app.stop()
    }

    init {
        body(httpPort)
    }
}

class JoobyFirstIT : JoobyIntegrationSpec({ port ->
    given("application started") {
        `when`("I request /") {
            val response = "http://localhost:$port/"
                    .httpGet()
                    .response()
            then("status should be 200") {
                response.second.statusCode shouldBe 201
            }
        }
    }

})

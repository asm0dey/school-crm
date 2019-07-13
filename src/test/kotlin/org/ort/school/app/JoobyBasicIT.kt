package org.ort.school.app

import io.kotlintest.Spec
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.apache.http.client.fluent.Request
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers
import java.net.ServerSocket

class KPostgreSQLContainer : PostgreSQLContainer<KPostgreSQLContainer>()

@Testcontainers
abstract class BasicInt : StringSpec() {

    val httpPort = ServerSocket(0).use { it.localPort }
    val httpsPort = ServerSocket(0).use { it.localPort }
    private val app = SchoolCRM()

    companion object {
        @JvmStatic
        private var PSQL_CONTAINER: KPostgreSQLContainer = KPostgreSQLContainer()

        init {
            PSQL_CONTAINER.start()
        }

    }

    override fun beforeSpec(spec: Spec) {
        app.start(
                "server.join=false",
                "application.port=$httpPort",
                "application.securePort=$httpsPort",
                "db.url=${PSQL_CONTAINER.jdbcUrl}",
                "db.user=${PSQL_CONTAINER.username}",
                "db.password=${PSQL_CONTAINER.password}"
        )
    }

    override fun afterSpec(spec: Spec) = app.stop()

    init {
        this.body(httpPort)
    }

    abstract fun body(t: Int)
}

class JoobyBasicIT : BasicInt() {
    override fun body(t: Int) {
        "on get / status should be 200" {
            val response = Request.Get("http://localhost:$t/")
                    .execute()
                    .returnResponse()
            response.statusLine.statusCode shouldBe 200
        }
    }

}


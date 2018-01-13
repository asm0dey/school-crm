package org.ort.school.app

import org.jetbrains.spek.api.dsl.SpecBody
import org.jooby.Jooby
import java.net.ServerSocket

fun SpecBody.jooby(app: Jooby, body: SpecBody.(port: Int) -> Unit) {
    val port = ServerSocket(0).use { it.localPort }
    beforeGroup {
        app.start("server.join=false", "application.port=$port")
    }

    body(port)

    afterGroup {
        app.stop()
    }
}

fun SpecBody.joobyProd(app: Jooby, body: SpecBody.(port: Int) -> Unit) {
    val port = ServerSocket(0).use { it.localPort }
    beforeGroup {
        app.start("prod", "server.join=false", "application.port=$port")
    }

    body(port)

    afterGroup {
        app.stop()
    }
}


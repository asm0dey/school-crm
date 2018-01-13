package org.ort.school.app

import org.jetbrains.spek.api.dsl.SpecBody
import org.jooby.Jooby
import java.net.ServerSocket

fun SpecBody.jooby(app: Jooby, body: SpecBody.(port: Int) -> Unit) {
    val httpSocket = ServerSocket(0)
    val httpsSocket = ServerSocket(0)
    val port = httpSocket.use { it.localPort }
    val securePort = httpsSocket.use { it.localPort }
    beforeGroup {
        app.start("server.join=false", "application.port=$port", "application.securePort=$securePort")
    }

    body(port)

    afterGroup {
        app.stop()
    }
}


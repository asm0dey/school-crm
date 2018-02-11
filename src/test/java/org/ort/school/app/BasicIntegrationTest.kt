package org.ort.school.app

import com.winterbe.expekt.should
import org.apache.http.client.fluent.Request
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jooby.jooby
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class BasicIntegrationTest : Spek({
    val app = SchoolCRM()
    jooby(app) { port ->
        describe("Server") {
            on("get /") {
                val response = Request.Get("http://localhost:$port/")
                        .execute()
                        .returnResponse()
                it("status should be 200") {
                    response
                            .statusLine
                            .statusCode.should.be.equal(200)
                }
            }
        }
    }
})
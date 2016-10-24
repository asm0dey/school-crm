package org.ort.school.app.routes

import com.google.inject.Inject
import org.jooby.Request
import org.jooby.Result
import org.jooby.Results
import org.jooby.mvc.GET
import org.jooby.mvc.Path
import org.jooq.DSLContext
import javax.validation.Validator

@Path("/login")
class Login @Inject constructor(private val ctx: DSLContext, private val validator: Validator) {
    @GET
    fun login(req: Request): Result {
        return Results.html("login").put("params", req.params().toMap().mapValues { it.value.value() })
    }
}
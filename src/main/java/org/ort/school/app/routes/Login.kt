package org.ort.school.app.routes

import com.google.inject.Singleton
import org.jooby.Request
import org.jooby.Result
import org.jooby.Results
import org.jooby.mvc.GET
import org.jooby.mvc.Path

@Singleton
@Path("/login")
class Login {
    @GET
    fun login(req: Request): Result {
        return Results.html("login").put("params", req.params().toMap().mapValues { it.value.value() })
    }
}
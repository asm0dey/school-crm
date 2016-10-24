package org.ort.school.app.routes

import org.jooby.Request
import org.jooby.Result
import org.jooby.Results
import org.jooby.mvc.GET
import org.jooby.mvc.Path
import org.pac4j.core.profile.CommonProfile

@Path("/private")
class Private {
    @GET
    fun findCorrectRedirect(request: Request): Result {
        val commonProfile: CommonProfile = request["profile"]
        return if (commonProfile.roles.isEmpty()) Results.tempRedirect("/logout")
        else Results.tempRedirect("/private/${commonProfile.roles.first()}")
    }

    @GET
    @Path("/:value")
    fun render(request: Request): Result {
        println(request.param("value"))
        return Results.ok(request.param("value").value())
    }
}
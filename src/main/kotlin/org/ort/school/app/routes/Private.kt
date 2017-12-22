package org.ort.school.app.routes

import com.google.inject.Inject
import org.jooby.Request
import org.jooby.Result
import org.jooby.Results
import org.jooby.mvc.GET
import org.jooby.mvc.Path
import org.ort.school.app.repo.UserRepo
import org.pac4j.core.profile.CommonProfile

@Path("/private")
class Private @Inject constructor(val userRepo: UserRepo) {
    @GET
    fun findCorrectRedirect(request: Request): Result {
        val commonProfile: CommonProfile = request["profile"]
        return if (commonProfile.roles.isEmpty()) Results.tempRedirect("/logout")
        else Results.tempRedirect("/private/${commonProfile.roles.first()}")
    }

    @GET
    @Path("/admin/:part")
    fun adminBase(request: Request): Result {
        val value = request.param("part").value()
        val result = Results.html("private/admin").put("part", value)
        if (value == "users") result.put("users", userRepo.listUsers())
        return result
    }

    @GET
    @Path("/:role")
    fun render(request: Request): Result {
        if (request.param("role").value() == "admin") return Results.redirect("/private/admin/users")
        return Results.ok(request.param("value").value())
    }
}
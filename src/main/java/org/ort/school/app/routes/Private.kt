package org.ort.school.app.routes

import com.google.inject.Inject
import com.google.inject.Singleton
import org.jooby.Request
import org.jooby.Result
import org.jooby.Results
import org.jooby.mvc.GET
import org.jooby.mvc.Local
import org.jooby.mvc.Path
import org.ort.school.app.service.DegreeService
import org.ort.school.app.service.UserService
import org.pac4j.core.profile.CommonProfile

@Singleton
@Path("/private")
class Private @Inject constructor(private val userService: UserService, private val degreeService: DegreeService) {
    @GET
    fun findCorrectRedirect(@Local profile: CommonProfile): Result {
        return if (profile.roles.isEmpty()) Results.tempRedirect("/logout")
        else Results.tempRedirect("/private/${profile.roles.first()}")
    }

    @GET
    @Path("/admin/:part")
    fun adminBase(request: Request): Result {
        val value = request.param("part").value()
        val result = Results.html("private/admin").put("part", value)
        if (value == "users") result.put("users", userService.listUsers())
        else if (value == "degrees") return Results.redirect("/private/degree")
        return result
    }

    @GET
    @Path("/:role")
    fun render(request: Request): Result {
        val role = request.param("role").value()
        if (role == "admin") return Results.redirect("/private/admin/users")
        if (role == "author") return Results.redirect("/private/author")
        return Results.ok(role)
    }
}
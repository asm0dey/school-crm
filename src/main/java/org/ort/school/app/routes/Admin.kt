package org.ort.school.app.routes

import com.google.inject.Inject
import com.google.inject.Singleton
import com.typesafe.config.Config
import org.jooby.Err
import org.jooby.Result
import org.jooby.mvc.GET
import org.jooby.mvc.Path
import org.jooby.Results
import org.jooby.View
import org.jooby.mvc.Local
import org.jooby.mvc.POST
import org.ort.school.app.service.DegreeService
import org.ort.school.app.service.UserService
import org.pac4j.core.profile.CommonProfile

@Singleton
@Path("/private/admin")
class Admin @Inject constructor(
        private val userService: UserService,
        private val config: Config,
        private val degreeService: DegreeService
) {
    @GET
    fun get(): Result {
        return Results.moved("/private/admin/users")
    }

    @GET
    @Path("/users")
    fun getUsers(@Local profile: CommonProfile): View {
        if (!profile.roles.contains("admin")) throw Err(403)
        return Results.html("private/admin")
                .put("part", "users")
                .put("users", userService.listUsers())
    }

    @GET
    @Path("/degrees")
    fun degrees(@Local profile: CommonProfile): View {
        if (!profile.roles.contains("admin")) throw Err(403)
        val allowedDegrees = config.getIntList("crm.allowedDegrees")
        val allowedLetters = config.getStringList("crm.allowedLetter")
        val degrees = degreeService.listDegrees()
        return Results
                .html("private/admin")
                .put("part", "degrees")
                .put("degrees", degrees)
                .put("allowedDegrees", allowedDegrees)
                .put("allowedLetters", allowedLetters)
    }

    @Path("/degrees")
    @POST
    fun createDegree(degree: DegreeDTO, @Local profile: CommonProfile): Result {
        if (!profile.roles.contains("admin")) throw Err(403)
        degreeService.createDegree(degree)
        return degrees(profile)
    }
}

data class DegreeDTO(
        val degreeLetter: String,
        val degreeNo: Int
)

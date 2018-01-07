package org.ort.school.app.routes

import com.google.inject.Inject
import com.google.inject.Singleton
import com.typesafe.config.Config
import org.jooby.Err
import org.jooby.Result
import org.jooby.Results
import org.jooby.Results.redirect
import org.jooby.View
import org.jooby.mvc.GET
import org.jooby.mvc.Local
import org.jooby.mvc.POST
import org.jooby.mvc.Path
import org.ort.school.app.service.DegreeService
import org.pac4j.core.profile.CommonProfile

@Singleton
@Path("/private/degree")
class Degree @Inject constructor(private val degreeService: DegreeService, private val config: Config) {
    @GET
    fun list(@Local profile: CommonProfile): View {
        if (!profile.roles.contains("admin")) throw Err(403)
        val allowedDegrees = config.getIntList("crm.allowedDegrees")
        val allowedLetters = config.getStringList("crm.allowedLetter")
        val degrees = degreeService.listDegrees()
        return Results
                .html("/private/admin")
                .put("part", "degrees")
                .put("degrees", degrees)
                .put("allowedDegrees", allowedDegrees)
                .put("allowedLetters", allowedLetters)
    }

    @Path("/create")
    @POST
    fun createDegree(degree: DegreeDTO, @Local profile: CommonProfile): Result {
        if (!profile.roles.contains("admin")) throw Err(403)
        degreeService.createDegree(degree)
        return redirect("/private/degree")

    }
}

data class DegreeDTO(
        val degreeLetter: String,
        val degreeNo: Int
)

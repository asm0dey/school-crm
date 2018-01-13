package org.ort.school.app.routes

import com.google.inject.Inject
import com.google.inject.Singleton
import org.jooby.Err
import org.jooby.Request
import org.jooby.Results
import org.jooby.View
import org.jooby.mvc.GET
import org.jooby.mvc.Local
import org.jooby.mvc.POST
import org.jooby.mvc.Path
import org.ort.school.app.service.AuthorService
import org.ort.school.app.service.DegreeService
import org.pac4j.core.profile.CommonProfile

@Singleton
@Path("/private/author")
class Author @Inject constructor(private val degreeService: DegreeService, private val authorService: AuthorService) {
    @GET
    fun index(@Local profile: CommonProfile): View {
        if (!profile.roles.contains("author")) throw Err(403)
        return Results.html("private/author").put("degrees", degreeService.listDegreeNames())
    }

    @POST
    fun sendLetter(request: Request): View {
        val degreeIds = request
                .params()
                .toMap()
                .filterKeys { it.startsWith("d-") }
                .keys
                .map { it.replace("d-", "").toInt() }
        val letterContent = request.param("content", "html").value()
        val subject = request.param("subject", "text").value()
        authorService.sendLetter(degreeIds, letterContent, subject)
        request.flash("success", "OK")
        return Results.html("private/author").put("degrees", degreeService.listDegreeNames())
    }
}
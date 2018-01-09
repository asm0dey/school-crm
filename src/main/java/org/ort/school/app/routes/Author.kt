package org.ort.school.app.routes

import com.google.inject.Singleton
import org.jooby.Err
import org.jooby.Results
import org.jooby.View
import org.jooby.mvc.GET
import org.jooby.mvc.Local
import org.jooby.mvc.Path
import org.pac4j.core.profile.CommonProfile

@Singleton
@Path("/private/author")
class Author {
    @GET
    fun index(@Local profile: CommonProfile): View {
        if (!profile.roles.contains("author")) throw Err(403)
        return Results.html("private/author")
    }
}
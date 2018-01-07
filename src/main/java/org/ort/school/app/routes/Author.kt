package org.ort.school.app.routes

import org.jooby.Results
import org.jooby.View
import org.jooby.mvc.GET
import org.jooby.mvc.Path

@Path("/private/author")
class Author {
    @GET
    fun index(): View {
        return Results.html("/private/author")
    }
}
package org.ort.school.app.routes

import com.google.inject.Singleton
import org.jooby.Results
import org.jooby.View
import org.jooby.mvc.GET
import org.jooby.mvc.Path

@Singleton
@Path("/private/author")
class Author {
    @GET
    fun index(): View {
        return Results.html("/private/author")
    }
}
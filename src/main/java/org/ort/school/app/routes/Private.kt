package org.ort.school.app.routes

import com.google.inject.Singleton
import org.jooby.Result
import org.jooby.Results
import org.jooby.mvc.GET
import org.jooby.mvc.Local
import org.jooby.mvc.Path
import org.pac4j.core.profile.CommonProfile

@Singleton
@Path("/private")
class Private {
    @GET
    fun findCorrectRedirect(@Local profile: CommonProfile): Result {
        return if (profile.roles.isEmpty()) Results.tempRedirect("/logout")
        else Results.tempRedirect("/private/${profile.roles.first()}")
    }
}
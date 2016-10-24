package org.ort.school.app.routes

import com.google.inject.Inject
import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.NotBlank
import org.jooby.Request
import org.jooby.Result
import org.jooby.Results
import org.jooby.mvc.GET
import org.jooby.mvc.POST
import org.jooby.mvc.Path
import org.jooby.pac4j.Auth
import org.jooby.pac4j.AuthSessionStore
import org.jooby.pac4j.AuthStore
import org.ort.school.app.repo.UserRepo
import org.pac4j.core.profile.CommonProfile
import javax.validation.Valid
import javax.validation.Validator
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.Size


@Path("/")
class Main @Inject constructor(private val validator: Validator, private val userRepo: UserRepo) {

    @GET
    fun home(): Result {
        if (!isInitialized()) return Results.tempRedirect("init")

        return Results.html("index")//.put("profile", commonProfile)
    }

    @GET
    @Path("/init")
    fun init(): Result =
            if (!isInitialized()) Results.html("init")
            else Results.redirect("/")

    @POST
    @Path("/init")
    fun createFirstUser(userInfo: FirstUserInfo): Result {
        val validate = validator.validate(userInfo)
        if (validate.size > 0) {
            val map = validate.associate { it -> it.propertyPath.toString() to it.message }
            return Results.html("init").put("errors", map)
        }
        userRepo.createUser(userInfo)
        return Results.redirect("/")
    }


    private fun isInitialized() = userRepo.hasUsers()

}

data class FirstUserInfo @Valid constructor(
        @get:NotBlank val username: String,
        @get:Email @get:NotBlank val email: String,
        @get:Size(min = 6) @get:NotBlank val password: String,
        val repeat: String) {
    @get:AssertTrue(message = "Should be equal to password")
    val valid
        get() = repeat == password
}

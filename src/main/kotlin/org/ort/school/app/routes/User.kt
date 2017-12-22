package org.ort.school.app.routes

import com.google.inject.Inject
import org.jooby.Request
import org.jooby.Result
import org.jooby.Results
import org.jooby.Status
import org.jooby.mvc.GET
import org.jooby.mvc.POST
import org.jooby.mvc.Path
import org.ort.school.app.repo.UserRepo
import org.pac4j.core.profile.CommonProfile
import javax.validation.Validator
import javax.validation.constraints.AssertTrue

@Path("/private/user")
class User @Inject constructor(val userRepo: UserRepo, val validator: Validator) {
    @GET
    @Path(":username/edit")
    fun updateUserProfile(request: Request): Result {
        val profile = request.get<CommonProfile>("profile")
        if (profile.roles.contains("admin") || profile.id == request.param("username").value()) {
            val admins = userRepo.countAdmins()
            return Results.html("private/profile/edit").put("editorEnabled", profile.roles.contains("admin") && admins > 1).put("errors", emptyMap<String, Any?>())
        }
        return Results.with(Status.FORBIDDEN)
    }

    @POST
    @Path(":username/update")
    fun updateUser(userInfoUpdateDTO: UserInfoUpdateDTO, request: Request): Result {
        val profile = request.get<CommonProfile>("profile")
        if (profile.roles.contains("admin") || profile.id == request.param("username").value()) {
            val validate = validator.validate(userInfoUpdateDTO)
            val admins = userRepo.countAdmins()
            if (validate.size > 0) {
                val map = validate.associate { it -> it.propertyPath.toString() to it.message }
                return Results.html("private/profile/edit").put("errors", map).put("editorEnabled", profile.roles.contains("admin") && admins > 1)
            }
            userRepo.updateUser(request.param("username").value(), userInfoUpdateDTO)
        }
        return Results.redirect("/private/users")
    }
}

data class UserInfoUpdateDTO(
        val lastname: String?,
        val firstname: String?,
        val patronymic: String?,
        val password: String?,
        val passwordConfirm: String?,
        val role: String
) {
    @get:AssertTrue(message = "Should be equal to password")
    val validPassword
        get() = password.isNullOrBlank() || (passwordConfirm == password && password!!.length > 5)
}

package org.ort.school.app.routes

import com.google.inject.Inject
import org.hibernate.validator.constraints.NotBlank
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
        val username = request.param("username").value()
        if (profile.roles.contains("admin") || profile.id == username) {
            val admins = userRepo.countAdmins()
            val user = userRepo.userBy(username)
            return Results.html("private/profile/edit")
                    .put("editorEnabled", profile.roles.contains("admin") && admins > 1)
                    .put("errors", emptyMap<String, Any?>())
                    .put("data", user)
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
        return Results.redirect("/private/admin/users")
    }

    @POST
    @Path("new")
    fun newUser(userInfoUpdateDTO: UserInfoUpdateDTO, request: Request): Result {
        val profile = request.get<CommonProfile>("profile")
        if (profile.roles.contains("admin") || profile.id == request.param("username").value()) {
            val validate = validator.validate(userInfoUpdateDTO, CreateUser::class.java)
            val admins = userRepo.countAdmins()
            if (validate.size > 0) {
                val map = validate.associate { it -> it.propertyPath.toString() to it.message }
                return Results.html("private/profile/edit").put("errors", map).put("editorEnabled", profile.roles.contains("admin") && admins > 1)
            }
            userRepo.updateUser(request.param("username").value(), userInfoUpdateDTO)
        }
        return Results.redirect("/private/admin/users")
    }

    @GET
    @Path("new")
    fun newUserPage(request: Request): Result {
        val profile = request.get<CommonProfile>("profile")
        if (profile.roles.contains("admin")) {
            val admins = userRepo.countAdmins()
            return Results.html("private/profile/new")
                    .put("editorEnabled", profile.roles.contains("admin") && admins > 1)
                    .put("errors", emptyMap<String, Any?>())
        }
        return Results.with(Status.FORBIDDEN)
    }
}

data class UserInfoUpdateDTO(
        @get:NotBlank(groups = [(CreateUser::class)]) val lastname: String?,
        @get:NotBlank(groups = [(CreateUser::class)]) val firstname: String?,
        val patronymic: String?,
        @get:NotBlank(groups = [(CreateUser::class)]) val password: String?,
        @get:NotBlank(groups = [(CreateUser::class)]) val passwordConfirm: String?,
        @get:NotBlank(groups = [(CreateUser::class)]) val role: String
) {
    @get:AssertTrue(message = "Should be equal to password")
    val validPassword
        get() = password.isNullOrBlank() || (passwordConfirm == password && password!!.length > 5)
}

object CreateUser

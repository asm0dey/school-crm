package org.ort.school.app.routes

import com.google.inject.Inject
import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.NotBlank
import org.jooby.Request
import org.jooby.Result
import org.jooby.Results
import org.jooby.Status
import org.jooby.mvc.GET
import org.jooby.mvc.Local
import org.jooby.mvc.POST
import org.jooby.mvc.Path
import org.ort.school.app.repo.UserRepo
import org.ort.school.app.validate.UniqueUsername
import org.pac4j.core.profile.CommonProfile
import javax.validation.Validator
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.Size


@Path("/private/user")
class User @Inject constructor(val userRepo: UserRepo, val validator: Validator) {
    @GET
    @Path(":username/edit")
    fun updateUserProfile(request: Request, @Local profile: CommonProfile): Result {
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
    fun updateUser(userInfoDTO: UserInfoDTO, request: Request, @Local profile: CommonProfile): Result {
        if (profile.roles.contains("admin") || profile.id == request.param("username").value()) {
            val validate = validator.validate(userInfoDTO)
            val admins = userRepo.countAdmins()
            if (validate.size > 0) {
                val map = validate.associate { it -> it.propertyPath.toString() to it.message }
                return Results.html("private/profile/edit").put("errors", map).put("editorEnabled", profile.roles.contains("admin") && admins > 1)
            }
            userRepo.updateUser(request.param("username").value(), userInfoDTO)
        }
        return Results.redirect("/private/admin/users")
    }

    @GET
    @Path("new")
    fun newUserPage(request: Request, @Local profile: CommonProfile): Result {
        if (profile.roles.contains("admin")) {
            val admins = userRepo.countAdmins()
            return Results.html("private/profile/new")
                    .put("editorEnabled", profile.roles.contains("admin"))
                    .put("errors", emptyMap<String, Any?>())
        }
        return Results.with(Status.FORBIDDEN)
    }

    @POST
    @Path("new")
    fun newUser(request: Request, userInfoDTO: UserInfoDTO, @Local profile: CommonProfile): Result {
        if (profile.roles.contains("admin")) {
            val validate = validator.validate(userInfoDTO, CreateUser::class.java)
            var map = validate.associate { it -> it.propertyPath.toString() to it.message }
            if (!userInfoDTO.username.isNullOrBlank() && userRepo.countUsersBy(userInfoDTO.username!!) > 0) {
                map += ("username" to "username should be unique")
            }

            if (validate.size > 0) {
                return Results.html("private/profile/new")
                        .put("errors", map)
                        .put("editorEnabled", profile.roles.contains("admin"))
            }
            userRepo.createUser(userInfoDTO)
        }
        return Results.redirect("/private")
    }
}

data class UserInfoDTO(
        @get:NotBlank(groups = [(CreateUser::class)])
        val lastname: String?,

        @get:NotBlank(groups = [(CreateUser::class)])
        val firstname: String?,

        val patronymic: String?,

        @get:NotBlank(groups = [(CreateUser::class)])
        @get:Size(min = 6, groups = [CreateUser::class])
        val password: String?,

        @get:NotBlank(groups = [(CreateUser::class)])
        private val passwordConfirm: String?,

        @get:NotBlank(groups = [(CreateUser::class)])
        val role: String,

        @get:NotBlank(groups = [(CreateUser::class)])
        @get:Size(min = 6, groups = [CreateUser::class])
        @get:UniqueUsername(groups = [CreateUser::class])
        val username: String?,

        @get:Email(groups = [(CreateUser::class)])
        val email: String?
) {
    @get:AssertTrue(message = "Should be equal to password")
    val validPassword
        get() = password.isNullOrBlank() || passwordConfirm.isNullOrBlank() || passwordConfirm == password
}

interface CreateUser


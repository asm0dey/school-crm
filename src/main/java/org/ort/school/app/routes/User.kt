package org.ort.school.app.routes

import com.google.inject.Inject
import com.google.inject.Singleton
import org.jooby.Request
import org.jooby.Result
import org.jooby.Results
import org.jooby.Status
import org.jooby.mvc.*
import org.ort.school.app.model.UserInfoDTO
import org.ort.school.app.service.UserService
import org.ort.school.app.validate.CreateUser
import org.pac4j.core.profile.CommonProfile
import javax.validation.Validator

@Singleton
@Path("/private/user")
class User @Inject constructor(val validator: Validator, val userService: UserService) {
    @GET
    @Path(":username/edit")
    fun updateUserProfile(request: Request, @Local profile: CommonProfile): Result {
        val username = request.param("username").value()
        if (profile.roles.contains("admin") || profile.id == username) {
            val adminsCount = userService.countAdmins()
            val user = userService.userBy(username)
            return Results.html("private/profile/edit")
                    .put("editorEnabled", profile.roles.contains("admin") && adminsCount > 1)
                    .put("errors", emptyMap<String, Any?>())
                    .put("data", user)
        }
        return Results.with(Status.FORBIDDEN)
    }

    @POST
    @Path(":username/edit")
    fun updateUser(userInfoDTO: UserInfoDTO, request: Request, @Local profile: CommonProfile): Result {
        if (profile.roles.contains("admin") || profile.id == request.param("username").value()) {
            val validate = validator.validate(userInfoDTO)
            val admins = userService.countAdmins()
            if (validate.size > 0) {
                val map = validate.associate { it -> it.propertyPath.toString() to it.message }
                return Results.html("private/profile/edit").put("errors", map).put("editorEnabled", profile.roles.contains("admin") && admins > 1)
            }
            userService.updateUser(request.param("username").value(), userInfoDTO)
        } else {
            return Results.with(Status.FORBIDDEN)
        }
        return Results.redirect("/private/admin/users")
    }

    @DELETE
    @Path(":username/delete")
    fun deleteUser(request: Request, @Local profile: CommonProfile): Result {
        val userToDelete = request.param("username").value()
        return if (profile.roles.contains("admin") && profile.username != userToDelete && userService.mayDeleteUser(userToDelete)) {
            userService.deleteUser(userToDelete)
            Results.ok()
        } else {
            Results.with(Status.FORBIDDEN)
        }
    }

    @GET
    @Path("new")
    fun newUserPage(@Local profile: CommonProfile): Result {
        if (profile.roles.contains("admin")) {
            val admins = userService.countAdmins()
            return Results.html("private/profile/new")
                    .put("editorEnabled", profile.roles.contains("admin"))
                    .put("errors", emptyMap<String, Any?>())
        }
        return Results.with(Status.FORBIDDEN)
    }

    @POST
    @Path("new")
    fun newUser(userInfoDTO: UserInfoDTO, @Local profile: CommonProfile): Result {
        if (profile.roles.contains("admin")) {
            val validate = validator.validate(userInfoDTO, CreateUser::class.java)
            val map = validate.associate { it -> it.propertyPath.toString() to it.message }

            if (validate.size > 0) {
                return Results.html("private/profile/new")
                        .put("errors", map)
                        .put("editorEnabled", profile.roles.contains("admin"))
            }
            userService.createUser(userInfoDTO)
        }
        return Results.redirect("/private")
    }
}



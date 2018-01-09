package org.ort.school.app.routes

import com.google.inject.Inject
import com.google.inject.Singleton
import org.jooby.*
import org.jooby.Results.redirect
import org.jooby.mvc.*
import org.ort.school.app.model.UserInfoDTO
import org.ort.school.app.service.UserService
import org.ort.school.app.validate.CreateUser
import org.pac4j.core.profile.CommonProfile
import javax.validation.Validator

@Singleton
@Path("/private/user")
class User @Inject constructor(private val validator: Validator, private val userService: UserService) {
    @GET
    fun defaultUserArea(@Local profile: CommonProfile): Result {
        return redirect("/private/user/${profile.username}/edit")
    }

    @GET
    @Path(":username")
    fun userAreaByName(request: Request): Result {
        return redirect("/private/user/${request.param("username").value()}/edit")
    }

    @GET
    @Path(":username/edit")
    fun updateUserProfilePage(request: Request, @Local profile: CommonProfile): Result {
        val username = request.param("username").value()
        if (profile.roles.contains("admin") || profile.id == username) {
            val adminsCount = userService.countAdmins()
            val user = userService.userBy(username)
            return Results.html("private/profile/edit")
                    .put("editorEnabled", profile.roles.contains("admin") && adminsCount > 1)
                    .put("errors", emptyMap<String, Any?>())
                    .put("data", user)
        }
        throw Err(403)
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
            throw Err(403)
        }
        return redirect("/private/admin/users")
    }

    @DELETE
    @Path(":username/delete")
    fun deleteUser(request: Request, @Local profile: CommonProfile): Result {
        val userToDelete = request.param("username").value()
        return if (profile.roles.contains("admin") && profile.username != userToDelete && userService.mayDeleteUser(userToDelete)) {
            userService.deleteUser(userToDelete)
            Results.ok()
        } else {
            throw Err(403)
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
        throw Err(403)
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
        return redirect("/private")
    }
}



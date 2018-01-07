package org.ort.school.app.model

import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.NotBlank
import org.ort.school.app.validate.CreateUser
import org.ort.school.app.validate.FirstUser
import org.ort.school.app.validate.UniqueUsername
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.Size


data class UserInfoDTO @JvmOverloads constructor(
        @get:NotBlank(groups = [(CreateUser::class)])
        val lastname: String? = null,

        @get:NotBlank(groups = [(CreateUser::class)])
        val firstname: String? = null,

        val patronymic: String? = null,

        @get:NotBlank(groups = [CreateUser::class, FirstUser::class])
        @get:Size(min = 6, groups = [CreateUser::class, FirstUser::class])
        val password: String? = null,

        @get:NotBlank(groups = [(CreateUser::class)])
        val passwordConfirm: String? = null,

        @get:NotBlank(groups = [(CreateUser::class)])
        val role: String = "",

        @get:NotBlank(groups = [CreateUser::class, FirstUser::class])
        @get:Size(min = 6, groups = [CreateUser::class])
        @get:UniqueUsername(groups = [CreateUser::class])
        val username: String? = null,

        @get:Email(groups = [CreateUser::class, FirstUser::class])
        @get:NotBlank(groups = [CreateUser::class, FirstUser::class])
        val email: String? = null
) {
    @get:AssertTrue(message = "Should be equal to password")
    val validPassword
        get() = password.isNullOrBlank() || passwordConfirm.isNullOrBlank() || passwordConfirm == password
}
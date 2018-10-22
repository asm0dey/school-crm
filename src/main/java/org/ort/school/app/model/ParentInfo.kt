package org.ort.school.app.model

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class ParentInfo(
        @get:NotBlank
        val lastname: String? = null,
        @get:NotBlank
        val firstname: String? = null,
        val patronymic: String? = null,
        @get:NotBlank @get:Email
        val email: String? = null
) {
    val displayName
        get() = listOfNotNull(lastname, firstname, patronymic).joinToString(" ")
}
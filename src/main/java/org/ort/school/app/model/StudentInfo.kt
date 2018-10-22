package org.ort.school.app.model

import javax.validation.constraints.NotBlank

data class StudentInfo(
        @get:NotBlank
        val lastname: String? = null,
        @get:NotBlank
        val firstname: String? = null,
        val patronymic: String? = null
)
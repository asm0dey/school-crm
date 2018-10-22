package org.ort.school.app.model

import javax.validation.Valid
import javax.validation.constraints.NotNull

data class SubscribeDTO(
        @get:NotNull @get:Valid
        val parent: ParentInfo? = null,
        @get:NotNull @get:Valid
        val student: StudentInfo? = null,
        @get:NotNull
        val degreeNo: Int? = null
)
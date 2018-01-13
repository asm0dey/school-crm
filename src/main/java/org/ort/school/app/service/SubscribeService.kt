package org.ort.school.app.service

import com.google.inject.Inject
import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.NotBlank
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.ort.school.app.repo.DegreeRepo
import javax.validation.Valid
import javax.validation.constraints.NotNull

class SubscribeService @Inject constructor(private val ctx: DSLContext, private val degreeRepo: DegreeRepo) {
    fun subscribe(info: SubscribeDTO) {
        ctx.transactionResult { conf ->
            val tx = DSL.using(conf)
            val studentId = degreeRepo.saveStudent(info.student!!, tx, info.degreeNo!!)
            degreeRepo.createParent(info.parent!!, studentId, info.degreeNo, tx)
        }
    }
}

data class SubscribeDTO(
        @get:NotNull @get:Valid
        val parent: ParentInfo? = null,
        @get:NotNull @get:Valid
        val student: StudentInfo? = null,
        @get:NotNull
        val degreeNo: Int? = null
)

data class StudentInfo(
        @get:NotBlank
        val lastname: String? = null,
        @get:NotBlank
        val firstname: String? = null,
        val patronymic: String? = null
)

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
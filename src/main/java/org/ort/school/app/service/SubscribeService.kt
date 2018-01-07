package org.ort.school.app.service

import com.google.inject.Inject
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.ort.school.app.repo.DegreeRepo

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
        val parent: ParentInfo? = null,
        val student: StudentInfo? = null,
        val degreeNo: Int? = null
)

data class StudentInfo(
        val lastname: String? = null,
        val firstname: String? = null,
        val patronymic: String? = null
)

data class ParentInfo(
        val lastname: String? = null,
        val firstname: String? = null,
        val patronymic: String? = null,
        val email: String? = null
)
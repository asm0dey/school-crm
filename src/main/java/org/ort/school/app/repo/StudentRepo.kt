package org.ort.school.app.repo

import com.google.inject.Inject
import org.jooq.DSLContext
import org.jooq.impl.DSL.coalesce
import org.jooq.impl.DSL.concat
import org.ort.school.app.model.StudentInfo
import org.ort.school.crm.jooq.model.Tables.STUDENT
import org.jooq.impl.DSL.`val` as value

class StudentRepo @Inject constructor(private val ctx: DSLContext) {
    fun studentByName(filter: String): List<StudentInfo> {
        val space = value(" ")
        val patronymic = coalesce(STUDENT.PATRONYMIC, "")
        val firstname = STUDENT.FIRSTNAME
        val lastname = STUDENT.LASTNAME
        val fullName = concat(lastname, space, firstname, space, patronymic).trim()
        return ctx
                .selectFrom(STUDENT)
                .where(fullName.containsIgnoreCase(filter))
                .map {
                    StudentInfo(
                            id = it.id,
                            lastname = it.lastname,
                            firstname = it.firstname,
                            patronymic = it.patronymic,
                            degreeNo = it.gradeId,
                            birthDate = it.birthDate.toLocalDate()
                    )
                }
    }

}

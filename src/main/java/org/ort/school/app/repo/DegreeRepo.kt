package org.ort.school.app.repo

import com.google.inject.Inject
import org.jooq.DSLContext
import org.jooq.Record10
import org.jooq.impl.DSL
import org.ort.school.app.routes.DegreeDTO
import org.ort.school.app.service.ParentInfo
import org.ort.school.app.service.StudentInfo
import org.ort.school.crm.jooq.model.Tables.*

class DegreeRepo @Inject constructor(private val ctx: DSLContext) {
    fun listDegrees(): List<GradeWithParents> {
        val fullGradeName = GRADE.GRADE_NO.concat(GRADE.GRADE_LETTER).`as`("fullGradeName")
        return ctx
                .select(
                        GRADE.ID,
                        fullGradeName,
                        STUDENT.ID,
                        STUDENT.LASTNAME,
                        STUDENT.FIRSTNAME,
                        STUDENT.PATRONYMIC,
                        PARENT.ID,
                        PARENT.LASTNAME,
                        PARENT.FIRSTNAME,
                        PARENT.PATRONYMIC
                )
                .from(GRADE)
                .leftJoin(STUDENT).on(STUDENT.GRADE_ID.eq(GRADE.ID))
                .leftJoin(PARENT_GRADE).on(PARENT_GRADE.GRADE_ID.eq(GRADE.ID))
                .leftJoin(PARENT).on(PARENT.ID.eq(PARENT_GRADE.PARENT_ID))
                .leftJoin(PARENT_STUDENT).on(PARENT_STUDENT.PARENT_ID.eq(PARENT.ID), PARENT_STUDENT.STUDENT_ID.eq(STUDENT.ID))
                .orderBy(GRADE.GRADE_NO.asc(), GRADE.GRADE_LETTER.asc())
                .fetch()
                .asSequence()
                .groupBy { GradeInfoDTO(it[GRADE.ID], it[fullGradeName]) }
                .mapValues {
                    it
                            .value
                            .asSequence()
                            .groupBy { parentInfo(it) }
                            .mapValues {
                                it
                                        .value
                                        .map { studentInfo(it) }
                            }
                            .asSequence()
                            .map { ParentWithChildren(it.key, it.value) }
                            .toList()

                }
                .toList()
    }

    private fun parentInfo(record: Record10<Int, String, Long, String, String, String, Long, String, String, String>): HumanInfoDTO? {
        return if (record[STUDENT.ID] != null)
            HumanInfoDTO(
                    record[PARENT.ID],
                    record[PARENT.LASTNAME],
                    record[PARENT.FIRSTNAME],
                    record[PARENT.PATRONYMIC])
        else null
    }

    private fun studentInfo(record: Record10<Int, String, Long, String, String, String, Long, String, String, String>): HumanInfoDTO? {
        return if (record[STUDENT.ID] != null)
            HumanInfoDTO(
                    record[STUDENT.ID],
                    record[STUDENT.LASTNAME],
                    record[STUDENT.FIRSTNAME],
                    record[STUDENT.PATRONYMIC])
        else null
    }

    fun createDegree(degree: DegreeDTO): Int? {
        return ctx
                .select(GRADE.ID)
                .from(GRADE)
                .where(GRADE.GRADE_LETTER.eq(degree.degreeLetter),
                        GRADE.GRADE_NO.eq(degree.degreeNo))
                .fetchOptional(GRADE.ID)
                .orElseGet {
                    ctx
                            .insertInto(GRADE, GRADE.GRADE_NO, GRADE.GRADE_LETTER)
                            .values(degree.degreeNo, degree.degreeLetter)
                            .returning(GRADE.ID)
                            .fetchOne()
                            .id

                }
    }

    fun listDegreeNames(): List<Pair<Int, String>> {
        val concat = DSL.concat(GRADE.GRADE_NO, GRADE.GRADE_LETTER)
        return ctx
                .select(GRADE.ID, concat).from(GRADE)
                .orderBy(GRADE.GRADE_NO.asc(), GRADE.GRADE_LETTER.asc())
                .map { it[GRADE.ID] to it[concat] }
    }

    fun saveStudent(student: StudentInfo, tx: DSLContext = ctx, degreeNo: Int): Long {

        return ctx.selectFrom(STUDENT)
                .where(
                        STUDENT.LASTNAME.eq(student.lastname),
                        STUDENT.FIRSTNAME.eq(student.firstname),
                        STUDENT.PATRONYMIC.eq(student.patronymic),
                        STUDENT.GRADE_ID.eq(degreeNo)
                )
                .fetchOptional(STUDENT.ID)
                .orElseGet {
                    val rcrd = tx
                            .newRecord(STUDENT)
                            .apply {
                                lastname = student.lastname
                                firstname = student.firstname
                                patronymic = student.patronymic
                                gradeId = degreeNo
                            }
                    rcrd.insert()
                    rcrd.id

                }
    }

    fun studentBy(student: StudentInfo, tx: DSLContext = ctx, degreeNo: Int): Long {
        return tx.selectFrom(STUDENT).where(
                STUDENT.LASTNAME.eq(student.lastname),
                STUDENT.PATRONYMIC.eq(student.patronymic),
                STUDENT.FIRSTNAME.eq(student.firstname),
                STUDENT.GRADE_ID.eq(degreeNo)
        ).fetchOne(STUDENT.ID)
    }

    fun createParent(parent: ParentInfo, studentId: Long, degreeNo: Int, tx: DSLContext = ctx) {
        val parentId = tx
                .selectFrom(PARENT)
                .where(PARENT.EMAIL.eq(parent.email), PARENT.LASTNAME.eq(parent.lastname), PARENT.PATRONYMIC.eq(parent.patronymic))
                .fetchOptional(PARENT.ID)
                .orElseGet {
                    tx.insertInto(PARENT, PARENT.EMAIL, PARENT.LASTNAME, PARENT.FIRSTNAME, PARENT.PATRONYMIC)
                            .values(parent.email, parent.lastname, parent.firstname, parent.patronymic)
                            .onConflictDoNothing()
                            .returning(PARENT.ID)
                            .fetchOne()
                            .id
                }

        tx
                .insertInto(PARENT_GRADE, PARENT_GRADE.PARENT_ID, PARENT_GRADE.GRADE_ID)
                .values(parentId, degreeNo)
                .onDuplicateKeyIgnore()
                .execute()
        tx
                .insertInto(PARENT_STUDENT, PARENT_STUDENT.PARENT_ID, PARENT_STUDENT.STUDENT_ID)
                .values(parentId, studentId)
                .onDuplicateKeyIgnore()
                .execute()
    }

    fun degreesAndParentsBy(degreeIds: List<Int>): Map<String, MutableList<ParentInfo>> {
        val concat = DSL.concat(GRADE.GRADE_NO, GRADE.GRADE_LETTER)

        return ctx.select(concat, PARENT.LASTNAME, PARENT.FIRSTNAME, PARENT.PATRONYMIC, PARENT.EMAIL)
                .from(GRADE, PARENT_GRADE, PARENT)
                .where(GRADE.ID.eq(PARENT_GRADE.GRADE_ID),
                        PARENT.ID.eq(PARENT_GRADE.PARENT_ID),
                        GRADE.ID.`in`(degreeIds))
                .fetchGroups { it -> it[concat] }
                .mapValues { (_, value) ->
                    value.map {
                        ParentInfo(
                                it[PARENT.LASTNAME],
                                it[PARENT.FIRSTNAME],
                                it[PARENT.PATRONYMIC],
                                it[PARENT.EMAIL]
                        )
                    }
                }
    }


}

data class GradeInfoDTO(
        val id: Int,
        val fullGradeName: String
)

data class HumanInfoDTO(
        val id: Long,
        val lastname: String,
        val firstname: String,
        val patronymic: String?
) {
    val displayName
        get() = listOfNotNull(lastname, firstname, patronymic).joinToString(" ")
}

data class ParentWithChildren(
        val parent: HumanInfoDTO?,
        val children: List<HumanInfoDTO?>
)


typealias GradeWithParents = Pair<GradeInfoDTO, List<ParentWithChildren>>
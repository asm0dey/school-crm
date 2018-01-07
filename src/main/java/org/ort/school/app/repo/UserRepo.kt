package org.ort.school.app.repo

import com.google.inject.Inject
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.mindrot.jbcrypt.BCrypt
import org.ort.school.app.model.UserInfoDTO
import org.ort.school.crm.jooq.model.Tables
import org.ort.school.crm.jooq.model.Tables.*

class UserRepo @Inject constructor(private val ctx: DSLContext) {

    fun hasUsers() = ctx
            .selectCount()
            .from(USER_ROLE)
            .innerJoin(USER).on(USER_ROLE.USER_ID.eq(USER.ID))
            .innerJoin(ROLE).on(ROLE.NAME.eq(USER_ROLE.ROLE))
            .where(ROLE.NAME.eq("admin"))
            .fetchOne(0, Int::class.java) > 0

    fun listUsers() = ctx
            .select(USER.USERNAME, USER.FIRSTNAME, USER.LASTNAME, USER.PATRONYMIC, USER_ROLE.ROLE, USER.EMAIL)
            .from(USER, USER_ROLE)
            .where(
                    USER.ID.eq(USER_ROLE.USER_ID)
            )
            .fetchInto(UserDTO::class.java)

    fun countAdmins(): Int {
        return ctx
                .selectCount()
                .from(
                        USER,
                        USER_ROLE,
                        ROLE
                )
                .where(
                        USER.ID.eq(USER_ROLE.USER_ID),
                        USER_ROLE.ROLE.eq(ROLE.NAME),
                        ROLE.NAME.eq("admin")
                )
                .fetchOne(0, Int::class.java)
    }

    fun updateUser(username: String, userInfoDTO: UserInfoDTO) {
        ctx.transactionResult { conf ->
            val tx = DSL.using(conf)
            val qStart = tx
                    .update(USER)
                    .set(USER.USERNAME, username)
            if (!userInfoDTO.password.isNullOrBlank()) {
                qStart.set(USER.PASSWORD, BCrypt.hashpw(userInfoDTO.password, BCrypt.gensalt()))
            }
            if (!userInfoDTO.firstname.isNullOrBlank()) {
                qStart.set(USER.FIRSTNAME, userInfoDTO.firstname)
            }
            if (!userInfoDTO.lastname.isNullOrBlank()) {
                qStart.set(USER.LASTNAME, userInfoDTO.lastname)
            }
            if (!userInfoDTO.patronymic.isNullOrBlank()) {
                qStart.set(USER.PATRONYMIC, userInfoDTO.patronymic)
            }
            qStart
                    .where(USER.USERNAME.eq(username))
                    .execute()
            tx
                    .update(USER_ROLE)
                    .set(USER_ROLE.ROLE, userInfoDTO.role)
                    .where(
                            USER_ROLE.USER_ID.eq(
                                    tx
                                            .select(USER.ID)
                                            .from(USER)
                                            .where(USER.USERNAME.eq(username))
                                            .fetchOne(USER.ID)
                            )
                    )
        }

    }

    fun userBy(username: String): UserDTO =
            ctx
                    .select(USER.USERNAME, USER.FIRSTNAME, USER.LASTNAME, USER.PATRONYMIC, USER_ROLE.ROLE, USER.EMAIL)
                    .from(USER, USER_ROLE)
                    .where(
                            USER.ID.eq(USER_ROLE.USER_ID),
                            USER.USERNAME.eq(username)
                    )
                    .fetchOneInto(UserDTO::class.java)
                    .copy(password = null)

    fun rolesBy(username: String) = ctx
            .select(Tables.ROLE.NAME)
            .from(Tables.USER, Tables.USER_ROLE, Tables.ROLE)
            .where(
                    Tables.USER.ID.eq(Tables.USER_ROLE.USER_ID),
                    Tables.ROLE.NAME.eq(Tables.USER_ROLE.ROLE),
                    Tables.USER.USERNAME.eq(username)
            )
            .fetch(Tables.ROLE.NAME)
            .toSet()

    fun userRecordBy(username: String) = ctx
            .selectFrom(Tables.USER)
            .where(Tables.USER.USERNAME.eq(username))
            .fetchOptional().orElse(null)


    fun countUsersBy(username: String): Long = ctx
            .selectCount()
            .from(USER)
            .where(USER.USERNAME.eq(username))
            .fetchOneInto(Long::class.java)

    fun createUser(userInfo: UserInfoDTO) {
        ctx.transactionResult { configuration ->
            val tx = DSL.using(configuration)
            val record = tx.newRecord(USER)
            record.from(userInfo)
            val userRecord = tx
                    .insertInto(USER)
                    .set(record)
                    .returning()
                    .fetchOne()
            tx
                    .insertInto(USER_ROLE, USER_ROLE.USER_ID, USER_ROLE.ROLE)
                    .values(userRecord.id, userInfo.role)
                    .execute()
        }
    }

    fun deleteUser(username: String) =
            ctx.transactionResult { configuration ->
                val tx = DSL.using(configuration)
                val userId = tx
                        .select(USER.ID)
                        .from(USER)
                        .where(USER.USERNAME.eq(username))
                        .fetchOne(USER.ID)
                tx
                        .deleteFrom(USER_ROLE)
                        .where(USER_ROLE.USER_ID.eq(userId))
                        .execute()
                tx
                        .deleteFrom(USER)
                        .where(USER.ID.eq(userId))
                        .execute()
            }

}

data class UserDTO @JvmOverloads constructor(
        val username: String,
        val firstName: String?,
        val lastname: String?,
        val patronymic: String?,
        val role: String,
        val email: String?,
        val password: String? = null
) {
    val displayName
        get() = sequenceOf(lastname, firstName, patronymic).filterNotNull().joinToString(" ")
}

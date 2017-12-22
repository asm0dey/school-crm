package org.ort.school.app.repo

import com.google.inject.Inject
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.mindrot.jbcrypt.BCrypt
import org.ort.school.app.routes.FirstUserInfo
import org.ort.school.app.routes.UserInfoUpdateDTO
import org.ort.school.crm.jooq.model.Tables.*
import org.pac4j.core.credentials.password.JBCryptPasswordEncoder

class UserRepo @Inject constructor(private val ctx: DSLContext) {

    fun hasUsers() = ctx
            .selectCount()
            .from(USER_ROLE)
            .innerJoin(USER).on(USER_ROLE.USER_ID.eq(USER.ID))
            .innerJoin(ROLE).on(ROLE.NAME.eq(USER_ROLE.ROLE))
            .where(ROLE.NAME.eq("admin"))
            .fetchOne(0, Int::class.java) > 0

    fun createUser(userInfo: FirstUserInfo) {
        ctx.transactionResult { conf ->
            val tx = DSL.using(conf)
            val userId = tx
                    .insertInto(USER, USER.USERNAME, USER.PASSWORD, USER.EMAIL)
                    .values(userInfo.username, JBCryptPasswordEncoder().encode(userInfo.password), userInfo.email)
                    .returning(USER.ID)
                    .fetchOne()[USER.ID]
            tx.insertInto(USER_ROLE, USER_ROLE.USER_ID, USER_ROLE.ROLE)
                    .values(userId, "admin")
                    .execute()

        }
    }

    fun listUsers() = ctx
            .select(USER.USERNAME, USER.FIRSTNAME, USER.LASTNAME, USER.PATRONYMIC, USER_ROLE.ROLE)
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

    fun updateUser(username: String, userInfoUpdateDTO: UserInfoUpdateDTO) {
        ctx.transactionResult { conf ->
            val tx = DSL.using(conf)
            val qStart = tx
                    .update(USER)
                    .set(USER.USERNAME, username)
            if (!userInfoUpdateDTO.password.isNullOrBlank()){
                qStart.set(USER.PASSWORD, BCrypt.hashpw(userInfoUpdateDTO.password, BCrypt.gensalt()))
            }
            if (!userInfoUpdateDTO.firstname.isNullOrBlank()){
                qStart.set(USER.PASSWORD, userInfoUpdateDTO.firstname)
            }
            if (!userInfoUpdateDTO.lastname.isNullOrBlank()){
                qStart.set(USER.PASSWORD, userInfoUpdateDTO.lastname)
            }
            if (!userInfoUpdateDTO.patronymic.isNullOrBlank()){
                qStart.set(USER.PASSWORD, userInfoUpdateDTO.patronymic)
            }
            qStart
                    .where(USER.USERNAME.eq(username))
                    .execute()
            tx
                    .update(USER_ROLE)
                    .set(USER_ROLE.ROLE, userInfoUpdateDTO.role)
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
}

data class UserDTO @JvmOverloads constructor(
        val username: String,
        val firstName: String?,
        val lastname: String?,
        val patronymic: String?,
        val role: String,
        val password: String? = null
) {
    val displayName
        get() = sequenceOf(lastname, firstName, patronymic).filterNotNull().joinToString(" ")
}
package org.ort.school.app.repo

import com.google.inject.Inject
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.ort.school.app.routes.FirstUserInfo
import org.ort.school.crm.jooq.model.Tables
import org.pac4j.core.credentials.password.JBCryptPasswordEncoder

class UserRepo @Inject constructor(private val ctx: DSLContext) {

    fun hasUsers() = ctx
            .selectCount()
            .from(Tables.USER_ROLE)
            .innerJoin(Tables.USER).on(Tables.USER_ROLE.USER_ID.eq(Tables.USER.ID))
            .innerJoin(Tables.ROLE).on(Tables.ROLE.NAME.eq(Tables.USER_ROLE.ROLE))
            .where(Tables.ROLE.NAME.eq("admin"))
            .fetchOne(0, Int::class.java) > 0

    fun createUser(userInfo: FirstUserInfo) {
        ctx.transactionResult { conf ->
            val tx = DSL.using(conf)
            val userId = tx
                    .insertInto(Tables.USER, Tables.USER.USERNAME, Tables.USER.PASSWORD, Tables.USER.EMAIL)
                    .values(userInfo.username, JBCryptPasswordEncoder().encode(userInfo.password), userInfo.email)
                    .returning(Tables.USER.ID)
                    .fetchOne()[Tables.USER.ID]
            tx.insertInto(Tables.USER_ROLE, Tables.USER_ROLE.USER_ID, Tables.USER_ROLE.ROLE)
                    .values(userId, "admin")
                    .execute()

        }
    }


}
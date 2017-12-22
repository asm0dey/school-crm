package org.ort.school.app.service

import com.google.inject.Inject
import org.jooq.DSLContext
import org.mindrot.jbcrypt.BCrypt
import org.ort.school.crm.jooq.model.Tables
import org.pac4j.core.context.Pac4jConstants
import org.pac4j.core.context.WebContext
import org.pac4j.core.credentials.UsernamePasswordCredentials
import org.pac4j.core.credentials.authenticator.Authenticator
import org.pac4j.core.exception.CredentialsException
import org.pac4j.core.profile.CommonProfile

class DBAuth @Inject constructor(val ctx: DSLContext) : Authenticator<UsernamePasswordCredentials> {
    override fun validate(credentials: UsernamePasswordCredentials, webCtx: WebContext) {
        if (credentials.username.isNullOrBlank() || credentials.password.isNullOrBlank())
            throw CredentialsException("Username and password cannot be blank")
        val userInfo = ctx
                .selectFrom(Tables.USER)
                .where(Tables.USER.USERNAME.eq(credentials.username))
                .fetchOptional().orElse(null)
                ?: throw CredentialsException("Username or password invalid")


        if (!BCrypt.checkpw(credentials.password, userInfo.password))
            throw CredentialsException("Username or password invalid")

        val roles = ctx
                .select(Tables.ROLE.NAME)
                .from(Tables.USER, Tables.USER_ROLE, Tables.ROLE)
                .where(
                        Tables.USER.ID.eq(Tables.USER_ROLE.USER_ID),
                        Tables.ROLE.NAME.eq(Tables.USER_ROLE.ROLE),
                        Tables.USER.USERNAME.eq(credentials.username)
                )
                .fetch(Tables.ROLE.NAME)
                .toSet()
        val profile = CommonProfile()
        profile.setId(credentials.username)
        profile.addAttribute(Pac4jConstants.USERNAME, credentials.username)
        profile.addAttribute("email", userInfo.email)
        profile.addAttribute("first_name", userInfo.firstname)
        profile.addAttribute("family_name", userInfo.lastname)
        profile.addAttribute("display_name", listOf(userInfo.lastname, userInfo.firstname, userInfo.patronymic).filterNotNull().joinToString(" "))
        profile.addAttribute("patronymic", userInfo.patronymic)
        profile.addRoles(roles)
        credentials.userProfile = profile

    }

}

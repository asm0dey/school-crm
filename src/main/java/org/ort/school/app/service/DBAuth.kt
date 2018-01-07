package org.ort.school.app.service

import com.google.inject.Inject
import org.mindrot.jbcrypt.BCrypt
import org.ort.school.app.repo.UserRepo
import org.pac4j.core.context.Pac4jConstants
import org.pac4j.core.context.WebContext
import org.pac4j.core.credentials.UsernamePasswordCredentials
import org.pac4j.core.credentials.authenticator.Authenticator
import org.pac4j.core.exception.CredentialsException
import org.pac4j.core.profile.CommonProfile

class DBAuth @Inject constructor(private val userRepo: UserRepo, private val passwordService: PasswordService) : Authenticator<UsernamePasswordCredentials> {
    override fun validate(credentials: UsernamePasswordCredentials, webCtx: WebContext) {
        val username = credentials.username
        if (username.isNullOrBlank() || credentials.password.isNullOrBlank())
            throw CredentialsException("Username and password cannot be blank")
        val userInfo = userRepo.userRecordBy(username) ?: throw CredentialsException("Username or password invalid")


        if (!passwordService.checkPw(credentials.password, userInfo.password))
            throw CredentialsException("Username or password invalid")

        val roles =userRepo.rolesBy(username)
        val profile = CommonProfile()
        profile.setId(username)
        profile.addAttribute(Pac4jConstants.USERNAME, username)
        profile.addAttribute("email", userInfo.email)
        profile.addAttribute("first_name", userInfo.firstname)
        profile.addAttribute("family_name", userInfo.lastname)
        profile.addAttribute("display_name", listOfNotNull(userInfo.lastname, userInfo.firstname, userInfo.patronymic).joinToString(" "))
        profile.addAttribute("patronymic", userInfo.patronymic)
        profile.addRoles(roles)
        credentials.userProfile = profile
    }

}

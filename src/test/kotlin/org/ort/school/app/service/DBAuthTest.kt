package org.ort.school.app.service

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.winterbe.expekt.should
import io.kotest.core.spec.style.AnnotationSpec
import org.junit.jupiter.api.Assertions.assertThrows
import org.ort.school.app.repo.UserRepo
import org.ort.school.crm.jooq.model.tables.records.UserRecord
import org.pac4j.core.context.Pac4jConstants
import org.pac4j.core.credentials.UsernamePasswordCredentials
import org.pac4j.core.exception.CredentialsException

class DBAuthTest : AnnotationSpec() {

    private val foundUser = UserRecord(0L, "admin", "admin", "admin", "admin", "admin", "admin")

    @Test
    fun validate() {
        val userRepo = mock<UserRepo>()
        val passwordService = mock<PasswordService>()
        val dbAuth = DBAuth(userRepo, passwordService)
        val credentials = UsernamePasswordCredentials("admin", "admin", "def")
        val roles = setOf("some", "another")
        whenever(userRepo.userRecordBy("admin")).thenReturn(foundUser)
        whenever(userRepo.rolesBy("admin")).thenReturn(roles)
        whenever(passwordService.checkPw(any(), any())).thenReturn(true)
        dbAuth.validate(credentials, mock())
        credentials.userProfile.id.should.equal("admin")
        credentials.userProfile.attributes.should
                .contain(Pac4jConstants.USERNAME to "admin")
                .contain("email" to "admin")
                .contain("first_name" to "admin")
                .contain("family_name" to "admin")
                .contain("display_name" to "admin admin admin")
                .contain("patronymic" to "admin")
        credentials.userProfile.roles.should.equal(roles)
    }

    @Test()
    fun `null username should throw exception`() {
        val userRepo = mock<UserRepo>()
        val passwordService = mock<PasswordService>()
        val dbAuth = DBAuth(userRepo, passwordService)
        val credentials = UsernamePasswordCredentials(null, "admin", "def")
        assertThrows(CredentialsException::class.java) { dbAuth.validate(credentials, mock()) }
    }

    @Test()
    fun `empty username should throw exception`() {
        val userRepo = mock<UserRepo>()
        val passwordService = mock<PasswordService>()
        val dbAuth = DBAuth(userRepo, passwordService)
        val credentials = UsernamePasswordCredentials("", "admin", "def")
        assertThrows(CredentialsException::class.java) { dbAuth.validate(credentials, mock()) }
    }

    @Test()
    fun `invalid password shoud throw exception`() {
        val userRepo = mock<UserRepo>()
        val passwordService = PasswordService()
        val dbAuth = DBAuth(userRepo, passwordService)
        val credentials = UsernamePasswordCredentials("admin", "admin", "def")
        assertThrows(CredentialsException::class.java) { dbAuth.validate(credentials, mock()) }
    }

    @Test()
    fun `empty password should throw exception`() {
        val userRepo = mock<UserRepo>()
        val passwordService = mock<PasswordService>()
        val dbAuth = DBAuth(userRepo, passwordService)
        val credentials = UsernamePasswordCredentials("admin", "", "def")
        assertThrows(CredentialsException::class.java) { dbAuth.validate(credentials, mock()) }
    }

    @Test()
    fun `null password should throw exception`() {
        val userRepo = mock<UserRepo>()
        val passwordService = mock<PasswordService>()
        val dbAuth = DBAuth(userRepo, passwordService)
        val credentials = UsernamePasswordCredentials("admin", null, "def")
        assertThrows(CredentialsException::class.java) { dbAuth.validate(credentials, mock()) }
    }

}
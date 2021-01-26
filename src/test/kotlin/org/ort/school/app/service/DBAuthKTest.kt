package org.ort.school.app.service

import com.winterbe.expekt.should
import io.kotlintest.shouldThrow
import io.kotlintest.specs.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import org.ort.school.app.repo.UserRepo
import org.ort.school.crm.jooq.model.tables.records.UserRecord
import org.pac4j.core.context.Pac4jConstants
import org.pac4j.core.credentials.UsernamePasswordCredentials
import org.pac4j.core.exception.CredentialsException

class DBAuthKTest : ShouldSpec({
    "with null/empty username or password"{
        should("throw exception on null password") {
            shouldThrow<CredentialsException> {
                DBAuth(mockk(), mockk()).validate(UsernamePasswordCredentials(null, null, "def"), mockk())
            }
        }
    }
    "with valid credentials"{
        should("modify profile") {
            val foundUser = UserRecord(10L, "admin", "admin", "admin", "admin", "admin", "admin")
            val roles = setOf("some", "another")
            val userRepo = mockk<UserRepo> {
                every { userRecordBy("admin") } returns foundUser
                every { rolesBy("admin") } returns roles
            }
            val passwordService = mockk<PasswordService> {
                every { checkPw("admin", "admin") } returns true
            }
            val dbAuth = DBAuth(userRepo, passwordService)
            val credentials = UsernamePasswordCredentials("admin", "admin", "def")
            dbAuth.validate(credentials, mockk())
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
    }
})


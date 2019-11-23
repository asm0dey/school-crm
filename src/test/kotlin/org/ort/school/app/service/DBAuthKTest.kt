package org.ort.school.app.service

import com.winterbe.expekt.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import org.ort.school.app.repo.UserRepo
import org.pac4j.core.credentials.UsernamePasswordCredentials
import org.pac4j.core.exception.CredentialsException

class DBAuthKTest : ShouldSpec(
        {
            val userRepo = mockk<UserRepo>()
            val passwordService = mockk<PasswordService>()
            val auth = DBAuth(userRepo, passwordService)

            "auth username"{
                should("not be null") {
                    every { userRepo.userBy("admin") } returns mockk{
                        every { username } returns "admin"
                        every { password } returns ""
                    }
                    shouldThrow<CredentialsException> { auth.validate(UsernamePasswordCredentials(null, null, null), mockk()) }
                            .message shouldBe  "Username and password cannot be blank 1"
                }

            }

        }
)


package org.ort.school.app.service

import io.kotest.core.datatest.forAll
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldStartWith

class PasswordServiceKTest : ShouldSpec({
    context("password hash") {
        should("not be equal to password itself") {
            val pass = "somePass"
            PasswordService().encryptPassword(pass) shouldNotBe pass
        }
        should("start with \$2a\$10 by default") {
            PasswordService().encryptPassword("somePass") shouldStartWith "\$2a\$10"
        }
        should("start with \$2a\$12 when we use 12 rounds") {
            PasswordService().encryptPassword("somePass", 12) shouldStartWith "\$2a\$12"
        }
    }
    context("check password return true when password matches hash") {
        forAll(
            "Yahveh",
            "testPass",
            "comlexpass",
            "you won't ever crack it you bastard"
        ) { pw ->
            val service = PasswordService()
            val hashed = service.encryptPassword(pw)
            service.checkPw(pw, hashed) shouldBe true
        }
        should("return false when password is wrong") {
            val service = PasswordService()
            val pass = "potentially wrong"
            val hashed = service.encryptPassword("wrong!!!")
            service.checkPw(pass, hashed) shouldBe false
        }

    }
})
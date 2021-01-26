package org.ort.school.app.service

import io.kotlintest.data.forall
import io.kotlintest.matchers.string.shouldStartWith
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.ShouldSpec
import io.kotlintest.tables.row

class PasswordServiceKTest: ShouldSpec({
    "password hash"{
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
    "check password"{
        should("return true when password matches hash") {
            forall(
                    row("Yahveh"),
                    row("testPass"),
                    row("comlexpass"),
                    row("you won't ever crack it you bastard")
            ) { pw ->
                val service = PasswordService()
                val hashed = service.encryptPassword(pw)
                service.checkPw(pw, hashed) shouldBe true
            }
        }
        should("return false when password is wrong") {
            val service = PasswordService()
            val pass = "potentially wrong"
            val hashed = service.encryptPassword("wrong!!!")
            service.checkPw(pass, hashed) shouldBe false
        }

    }
})
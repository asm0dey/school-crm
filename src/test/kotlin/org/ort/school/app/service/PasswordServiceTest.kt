package org.ort.school.app.service

import com.winterbe.expekt.should
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mindrot.jbcrypt.BCrypt

class PasswordServiceTest {

    @Test
    fun encryptPassword() {
        val passwordService = PasswordService()
        passwordService.encryptPassword("aaa").should.startWith("$2a$10")
    }

    @Test
    fun encryptPassword2() {
        val passwordService = PasswordService()
        passwordService.encryptPassword("aaa", 12).should.startWith("$2a$12")
    }

    @Test
    fun checkPw() {
        val pass = "ss"
        val hashpw = BCrypt.hashpw(pass, BCrypt.gensalt())
        PasswordService().checkPw(pass, hashpw).should.be.`true`
    }

    @Test()
    fun `non-bcrypt-encrypted pass should cause exception`() {
        val pass = "ss"
        val hashpw = "lala"
        Assertions.assertThrows(IllegalArgumentException::class.java) { PasswordService().checkPw(pass, hashpw) }
    }

    @Test
    fun `wrong pass should fail`() {
        val pass = "ss"
        val hashpw = "\$2a\$10\$2W59wXhMcQylsq9qwbzZoOaUC9MyM.yYnmA2uCHzW2ylQOKwtKnMW"
        PasswordService().checkPw(pass, hashpw).should.be.`false`
    }
}
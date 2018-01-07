package org.ort.school.app.service

import org.mindrot.jbcrypt.BCrypt

class PasswordService {
    fun encryptPassword(plainTextPw: String, rounds: Int = 10) = BCrypt.hashpw(plainTextPw, BCrypt.gensalt(rounds))
    fun checkPw(plainTextPw: String, hashedPw: String) = BCrypt.checkpw(plainTextPw, hashedPw)
}
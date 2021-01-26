package org.ort.school.app.validate

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.winterbe.expekt.should
import io.kotlintest.specs.AnnotationSpec
import org.ort.school.app.repo.UserRepo

class UniqueUsernameValidatorTest :AnnotationSpec(){

    @Test
    fun `username not null and not exists`() {
        val userRepo = mock<UserRepo> {
            on { countUsersBy("som") } doReturn 0
        }
        val uniqueUsernameValidator = UniqueUsernameValidator(userRepo)
        uniqueUsernameValidator.isValid("som", null).should.be.`true`
    }

    @Test
    fun `username null`() {
        val userRepo = mock<UserRepo>()
        val uniqueUsernameValidator = UniqueUsernameValidator(userRepo)
        uniqueUsernameValidator.isValid(null, null).should.be.`true`
    }

    @Test
    fun `username not null and user exists`() {
        val userRepo = mock<UserRepo> {
            on { countUsersBy("som") } doReturn 1
        }
        val uniqueUsernameValidator = UniqueUsernameValidator(userRepo)
        uniqueUsernameValidator.isValid("som", null).should.be.`false`
    }

}
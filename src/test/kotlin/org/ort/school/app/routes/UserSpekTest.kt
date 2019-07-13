package org.ort.school.app.routes

import com.nhaarman.mockito_kotlin.*
import com.winterbe.expekt.should
import io.kotlintest.shouldBe
import org.hibernate.validator.internal.engine.path.PathImpl
import org.jooby.Err
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.ort.school.app.model.UserInfoDTO
import org.ort.school.app.service.UserService
import org.ort.school.app.validate.CreateUser
import org.pac4j.core.profile.CommonProfile
import views.priv.profile.newuser
import javax.validation.ConstraintViolation
import javax.validation.Validator

class UserSpecTest {
    @JvmField
    val validator = mock<Validator>()
    @JvmField
    val userService = mock<UserService>()
    @JvmField
    val user = User(validator, userService)

    @Test
    fun `non-adin on new user page should throw 403`() {
        val profile = mock<CommonProfile>()
        assertThrows(Err::class.java) { user.newUserPage(profile) }
                .statusCode() shouldBe 403
    }

    @Test
    fun `new user page should render correctly when admin enters`() {
        val profile = mock<CommonProfile> {
            on { roles } doReturn setOf("admin")
        }
        val newUserPage = user.newUserPage(profile)
        newUserPage.editorEnabled().should.be.`true`
        newUserPage.errors().should.be.empty
    }

    @Test
    fun `non-admin trying to create new user shout be redirected to "priv"`() {
        val profile = mock<CommonProfile>()
        val result = user.newUser(mock(), profile)
        result.status().get().value().should.be.equal(302)
        result.headers().should.contain("location" to "/priv")
    }

    @Test
    fun `admin, sending valid user should create user and be redirected to "priv"`() {
        whenever(validator.validate<UserInfoDTO>(any())).thenReturn(emptySet())
        val profile = mock<CommonProfile> {
            on { roles } doReturn setOf("admin")
        }
        val userInfoDTO = mock<UserInfoDTO>()
        val result = user.newUser(userInfoDTO, profile)
        verify(userService, times(1)).createUser(userInfoDTO)
        result.status().get().value().should.be.equal(302)
        result.headers().should.contain("location" to "/priv")
    }

    @Test
    fun `when I'm creating user with invalid date we should be returned to page with errors filled`() {
        val vio = mock<ConstraintViolation<UserInfoDTO>> {
            on { propertyPath } doReturn PathImpl.createPathFromString("path1")
            on { message } doReturn "mess1"
        }
        val vio2 = mock<ConstraintViolation<UserInfoDTO>> {
            on { propertyPath } doReturn PathImpl.createPathFromString("path2")
            on { message } doReturn "mess2"
        }
        val errors = setOf(vio, vio2)
        whenever(validator.validate<UserInfoDTO>(any(), eq(CreateUser::class.java))).thenReturn(errors)
        val profile = mock<CommonProfile> {
            on { roles } doReturn setOf("admin")
        }
        val userInfoDTO = mock<UserInfoDTO>()
        val result = user.newUser(userInfoDTO, profile)
        val get = result.get<newuser>()!!
        get.errors().should.equal(mapOf("path1" to "mess1", "path2" to "mess2"))
        get.editorEnabled().should.be.`true`
    }
}

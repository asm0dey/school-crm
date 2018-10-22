package org.ort.school.app.routes

import com.nhaarman.mockito_kotlin.*
import com.winterbe.expekt.should
import org.hibernate.validator.internal.engine.path.PathImpl
import org.jooby.Status
import org.junit.Test
import org.ort.school.app.model.UserInfoDTO
import org.ort.school.app.service.DegreeService
import org.ort.school.app.model.SubscribeDTO
import org.ort.school.app.service.SubscribeService
import org.ort.school.app.service.UserService
import views.index
import views.init
import javax.validation.ConstraintViolation
import javax.validation.Validator


class IndexTest {
    @Test
    fun `when there are no users index should redirect to init`() {
        val userService = mock<UserService>()
        whenever(userService.hasUsers()).thenReturn(false)
        val main = Main(mock(), userService, mock(), mock())
        val home = main.home()
        home.status().get().should.equal(Status.TEMPORARY_REDIRECT)
        home.headers().should.contain("location" to "init")
    }

    @Test
    fun `when there are users init should redirect to index`() {
        val userService = mock<UserService>()
        whenever(userService.hasUsers()).thenReturn(true)
        val main = Main(mock(), userService, mock(), mock())
        val home = main.init()
        home.status().get().should.equal(Status.FOUND)
        home.headers().should.contain("location" to "/")
    }

    @Test
    fun `when there are no users init should render initial user creation page`() {
        val userService = mock<UserService>()
        whenever(userService.hasUsers()).thenReturn(false)
        val main = Main(mock(), userService, mock(), mock())
        val view = main.init().get<init>()!!
    }

    @Test
    fun `when there are  users index should render main page`() {
        val userService = mock<UserService>()
        whenever(userService.hasUsers()).thenReturn(true)
        val degreeService = mock<DegreeService>()
        val degrees = listOf(1 to "1А", 2 to "1Б", 3 to "1В")
        whenever(degreeService.listDegreeNames()).thenReturn(degrees)
        val main = Main(mock(), userService, degreeService, mock())
        val view = main.home().get<index>()!!
        view.degrees().should.equal(degrees)
        view.errors().should.equal(emptyMap())
    }

    @Test
    fun `should return init if error in first user creation`() {
        val validator = mock<Validator>()
        val constraintViolation = mock<ConstraintViolation<UserInfoDTO>>()
        whenever(constraintViolation.propertyPath).thenReturn(PathImpl.createPathFromString("ss"))
        whenever(constraintViolation.message).thenReturn("mess")
        whenever(validator.validate(any<UserInfoDTO>(), any())).thenReturn(setOf(constraintViolation))
        val main = Main(validator, mock(), mock(), mock())
        val view = main.createFirstUser(UserInfoDTO()).get<init>()!!
        view.errors().should.equal(mapOf("ss" to "mess"))
    }

    @Test
    fun `should create first user with admin role and redirect to index if first user is correct`() {
        val userService = mock<UserService>()
        val main = Main(mock(), userService, mock(), mock())
        val userInfo = UserInfoDTO()
        val view = main.createFirstUser(userInfo)
        verify(userService, times(1)).createUser(userInfo.copy(role = "admin"))
        view.status().get().should.equal(Status.FOUND)
        view.headers().should.contain("location" to "/")
        main.home().get<index>()!!.errors().should.be.empty
    }

    @Test
    fun `subscribe should render index with errors in model if there are errors in DTO`() {
        val validator = mock<Validator>()
        val constraintViolation = mock<ConstraintViolation<SubscribeDTO>>()
        whenever(constraintViolation.propertyPath).thenReturn(PathImpl.createPathFromString("ss"))
        whenever(constraintViolation.message).thenReturn("mess")
        whenever(validator.validate(any<SubscribeDTO>(), any())).thenReturn(setOf(constraintViolation))
        val index = Main(validator, mock(), mock(), mock()).subscribe(SubscribeDTO())
        index.errors().should.equal(mapOf("ss" to "mess"))
    }

    @Test
    fun `subscribe should save info about parent and child when subscribeDTO is fine`() {
        val subscribeService = mock<SubscribeService>()
        val subscribeDTO = SubscribeDTO()
        val view = Main(mock(), mock(), mock(), subscribeService).subscribe(subscribeDTO)
        view.degrees().should.equal(emptyList())
        verify(subscribeService, times(1)).subscribe(subscribeDTO)
    }
}
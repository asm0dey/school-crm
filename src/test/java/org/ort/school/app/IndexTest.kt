package org.ort.school.app

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.winterbe.expekt.should
import org.jooby.Status
import org.jooby.View
import org.junit.Test
import org.ort.school.app.routes.Main
import org.ort.school.app.service.DegreeService
import org.ort.school.app.service.UserService


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
        val view = main.init().get<View>()!!
        view.name().should.equal("init")
    }

    @Test
    fun `when there are  users index should render main page`() {
        val userService = mock<UserService>()
        whenever(userService.hasUsers()).thenReturn(true)
        val degreeService = mock<DegreeService>()
        val degrees = listOf(1 to "1А", 2 to "1Б", 3 to "1В")
        whenever(degreeService.listDegreeNames()).thenReturn(degrees)
        val main = Main(mock(), userService, degreeService, mock())
        val view = main.home().get<View>()!!
        view.name().should.equal("index")
        view.model().should.contain("degrees" to degrees)
    }
}
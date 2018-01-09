package org.ort.school.app.routes

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.winterbe.expekt.should
import org.jooby.Mutant
import org.jooby.Request
import org.jooby.View
import org.jooby.internal.MutantImpl
import org.junit.Test

import org.junit.Assert.*

class LoginTest {

    @Test
    fun login() {
        val req = mock<Request>()
        val mock = mock<Mutant>()
        whenever(req.params()).thenReturn(mock)
        whenever(mock.value()).thenReturn("another")
        whenever(mock.toMap()).thenReturn(mapOf("some" to mock))
        val view = Login().login(req).get<View>()!!
        view.name().should.be.equal("login")
        view.model().should.contain("params" to mapOf("some" to "another"))
    }
}
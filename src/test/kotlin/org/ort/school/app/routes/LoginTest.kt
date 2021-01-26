package org.ort.school.app.routes

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.winterbe.expekt.should
import io.kotest.core.spec.style.AnnotationSpec
import org.jooby.Mutant
import org.jooby.Request

class LoginTest : AnnotationSpec(){

    @Test
    fun login() {
        val req = mock<Request>()
        val mock = mock<Mutant>()
        whenever(req.params()).thenReturn(mock)
        whenever(mock.value()).thenReturn("another")
        whenever(mock.toMap()).thenReturn(mapOf("some" to mock))
        val login = Login().login(req)
        login.params().should.equal(mapOf("some" to "another"))
    }
}
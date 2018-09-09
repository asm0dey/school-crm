package org.ort.school.app.service

import com.nhaarman.mockito_kotlin.*
import org.jooq.DSLContext
import org.jooq.TransactionalCallable
import org.jooq.impl.DefaultConfiguration
import org.jooq.tools.jdbc.Mock
import org.jooq.tools.jdbc.MockConfiguration
import org.junit.Test
import org.ort.school.app.repo.DegreeRepo

class SubscribeServiceTest {

    @Test
    fun subscribe() {
        val mockConfiguration = MockConfiguration(DefaultConfiguration(), Mock.of(1))
        val ctx = mock<DSLContext> {
            on { transactionResult(any<TransactionalCallable<*>>()) } doAnswer { invocation -> invocation.getArgument<TransactionalCallable<*>>(0).run(mockConfiguration) }
        }
        val student = StudentInfo("ss", "ss", "ss")
        val parent = ParentInfo("ss", "ss", "ss", "ss")
        val degreeNo = 2
        val subscribeDTO = SubscribeDTO(parent, student, degreeNo)
        val degreeRepo = mock<DegreeRepo> {
            on { saveStudent(eq(student), any(), eq(degreeNo)) } doReturn 777
        }
        val subscribeService = SubscribeService(ctx, degreeRepo)
        subscribeService.subscribe(subscribeDTO)

        verify(degreeRepo, times(1)).createParent(eq(parent), eq(777), eq(degreeNo), any())
    }
}
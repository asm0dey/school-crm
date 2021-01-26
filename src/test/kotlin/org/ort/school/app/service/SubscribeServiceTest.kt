package org.ort.school.app.service

import com.nhaarman.mockito_kotlin.*
import io.kotest.core.spec.style.AnnotationSpec
import org.jooq.DSLContext
import org.jooq.TransactionalCallable
import org.jooq.impl.DefaultConfiguration
import org.jooq.tools.jdbc.Mock
import org.jooq.tools.jdbc.MockConfiguration
import org.ort.school.app.model.ParentInfo
import org.ort.school.app.model.StudentInfo
import org.ort.school.app.model.SubscribeDTO
import org.ort.school.app.repo.DegreeRepo

class SubscribeServiceTest : AnnotationSpec(){

    @Test
    fun subscribe() {
        val mockConfiguration = MockConfiguration(DefaultConfiguration(), Mock.of(1))
        val ctx = mock<DSLContext> {
            on { transactionResult(any<TransactionalCallable<*>>()) } doAnswer { invocation -> invocation.getArgument<TransactionalCallable<*>>(0).run(mockConfiguration) }
        }
        val student = StudentInfo(
                null,
                "ss",
                "ss",
                "ss",
                2
        )
        val parent = ParentInfo("ss", "ss", "ss", "ss")
        val degreeNo = 2
        val subscribeDTO = SubscribeDTO(parent, listOf(student))
        val degreeRepo = mock<DegreeRepo> {
            on { saveStudent(eq(student), any()) } doReturn 777
        }
        val subscribeService = SubscribeService(ctx, degreeRepo)
        subscribeService.subscribe(subscribeDTO)

        verify(degreeRepo, times(1)).createParent(eq(parent), eq(777), any())
    }
}
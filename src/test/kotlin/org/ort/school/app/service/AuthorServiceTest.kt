package org.ort.school.app.service

import com.nhaarman.mockito_kotlin.*
import io.kotest.core.spec.style.AnnotationSpec
import org.ort.school.app.model.ParentInfo

class AuthorServiceTest : AnnotationSpec() {
    @Test
    fun sendLetterTest() {
        val degreeService = mock<DegreeService>()
        val emptyMap = emptyMap<String, MutableList<ParentInfo>>()
        whenever(degreeService.degreesAndParentsBy(any())).thenReturn(emptyMap)
        val emailService = mock<EmailService>()
        val authorService = AuthorService(emailService, degreeService)
        authorService.sendLetter(emptyList(), "", "")
        verify(emailService, times(1)).sendMails(emptyMap, "", "")
    }
}
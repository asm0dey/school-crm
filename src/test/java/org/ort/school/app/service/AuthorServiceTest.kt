package org.ort.school.app.service

import com.nhaarman.mockito_kotlin.*
import org.junit.Test

class AuthorServiceTest {
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
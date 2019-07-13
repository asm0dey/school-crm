package org.ort.school.app.routes

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import io.kotlintest.shouldBe
import org.jooby.Err
import org.jooby.Mutant
import org.jooby.Request
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.ort.school.app.service.AuthorService
import org.ort.school.app.service.DegreeService
import org.pac4j.core.profile.CommonProfile

class AuthorTest {

    @Test()
    fun `should throw 403 if user is not author`() {
        val profile = mock<CommonProfile>()
        assertThrows(Err::class.java) { Author(mock(), mock()).index(profile) }
                .statusCode() shouldBe 403

    }

    @Test
    fun `should open author view on happy pass`() {
        val profile = mock<CommonProfile> {
            on { roles } doReturn hashSetOf("author")
        }

        val degrees = listOf(1 to "a")
        val degreeService = mock<DegreeService> {
            on { listDegreeNames() } doReturn degrees
        }

        Author(degreeService, mock()).index(profile).degrees() shouldBe degrees
    }

    @Test
    fun `should send letter`() {
        val mutant = mock<Mutant> {
            on { toMap() } doReturn mapOf<String, Mutant?>("d-1" to null, "d-2" to mock<Mutant>())
        }
        val mutantContent = mock<Mutant> {
            on { value() } doReturn "sss"
        }
        val mutantSubject = mock<Mutant> {
            on { value() } doReturn "subj"
        }
        val request = mock<Request> {
            on { params() } doReturn mutant
            on { param("content", "html") } doReturn mutantContent
            on { param("subject", "text") } doReturn mutantSubject
        }
        val authorService = mock<AuthorService>()
        val degrees = listOf(1 to "з", 2 to "я")
        val degreeService = mock<DegreeService> {
            on { listDegreeNames() } doReturn degrees
        }
        val profile = mock<CommonProfile> {
            on { roles } doReturn setOf("author")
        }
        val sendLetter = Author(degreeService, authorService).sendLetter(profile, request)
        verify(authorService, times(1)).sendLetter(listOf(1, 2), "sss", "subj")
        verify(request, times(1)).flash("success", "OK")
        sendLetter.degrees() shouldBe degrees
    }

    @Test()
    fun `can't send letter without author role`() {
        assertThrows(Err::class.java) { Author(mock(), mock()).sendLetter(mock(), mock()) }
                .statusCode() shouldBe 403

    }


}
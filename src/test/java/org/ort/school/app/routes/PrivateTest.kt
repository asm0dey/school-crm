package org.ort.school.app.routes

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.winterbe.expekt.should
import org.jooby.Status
import org.junit.Test
import org.pac4j.core.profile.CommonProfile
import java.util.*

class PrivateTest {

    @Test
    fun `when user with no roles enters private zone he's redirected to logout`() {
        val profile = mock<CommonProfile>()
        whenever(profile.roles).thenReturn(emptySet())
        val result = Private().findCorrectRedirect(profile)
        result.status().should.be.equal(Optional.of(Status.TEMPORARY_REDIRECT))
        result.headers().should.contain("location" to "/logout")
    }
    @Test
    fun `when admin enters private zone he's redirected to admin zone`() {
        val profile = mock<CommonProfile>()
        whenever(profile.roles).thenReturn(setOf("admin"))
        val result = Private().findCorrectRedirect(profile)
        result.status().should.be.equal(Optional.of(Status.TEMPORARY_REDIRECT))
        result.headers().should.contain("location" to "/priv/admin")
    }
    @Test
    fun `when author enters private zone he's redirected to author zone`() {
        val profile = mock<CommonProfile>()
        whenever(profile.roles).thenReturn(setOf("author"))
        val result = Private().findCorrectRedirect(profile)
        result.status().should.be.equal(Optional.of(Status.TEMPORARY_REDIRECT))
        result.headers().should.contain("location" to "/priv/author")
    }
}
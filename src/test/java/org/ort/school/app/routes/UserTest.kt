package org.ort.school.app.routes

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.winterbe.expekt.should
import org.jooby.Mutant
import org.jooby.Request
import org.jooby.Status
import org.junit.Test
import org.pac4j.core.profile.CommonProfile

class UserTest {

    @Test
    fun defaultUserArea() {
        val profile = mock<CommonProfile>()
        whenever(profile.username).thenReturn("asm")
        val result = User(mock(), mock()).defaultUserArea(profile)
        result.status().get().should.be.equal(Status.FOUND)
        result.headers()["location"].should.be.equal("/priv/user/asm/edit")
    }

    @Test
    fun userAreaByName() {
        val request = mock<Request>()
        val mutant = mock<Mutant>()
        whenever(mutant.value()).thenReturn("asm")
        whenever(request.param("username")).thenReturn(mutant)
        val result = User(mock(), mock()).userAreaByName(request)
        result.status().get().should.be.equal(Status.FOUND)
        result.headers()["location"].should.be.equal("/priv/user/asm/edit")
    }

    @Test
    fun updateUserProfile() {
    }

    @Test
    fun updateUser() {
    }

    @Test
    fun deleteUser() {
    }

    @Test
    fun newUserPage() {
    }

    @Test
    fun newUser() {
    }
}
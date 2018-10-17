package org.ort.school.app.routes

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.winterbe.expekt.should
import org.jooby.Err
import org.junit.Assert
import org.junit.Test

import org.pac4j.core.profile.CommonProfile

class AuthorTest {

    @Test
    fun index() {
        val profile = mock<CommonProfile>()
        try {
            Author(mock(), mock()).index(profile)
        } catch (e: Exception) {
            e.should.be.instanceof(Err::class.java)
            (e as Err).statusCode().should.be.equal(403)
            return
        }
        Assert.fail("Err has not been thrown")
    }


}
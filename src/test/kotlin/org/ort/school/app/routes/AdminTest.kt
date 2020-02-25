package org.ort.school.app.routes

import com.nhaarman.mockito_kotlin.*
import com.typesafe.config.Config
import com.winterbe.expekt.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import net.andreinc.mockneat.MockNeat
import org.jooby.Err
import org.jooby.Status
import org.junit.jupiter.api.Assertions.assertThrows
import org.ort.school.app.model.DegreeDTO
import org.ort.school.app.repo.*
import org.ort.school.app.service.DegreeService
import org.ort.school.app.service.UserService
import org.pac4j.core.profile.CommonProfile

private val m: MockNeat = MockNeat.threadLocal()

class AdminTest : AnnotationSpec(){
    @Test
    fun `when user hits slash-admin he's redirected to users area`() {
        val admin = Admin(mock(), mock(), mock(), mock()).get()
        admin.status().get().should.be.equal(Status.MOVED_PERMANENTLY)
        admin.headers().should.contain("location" to "/priv/admin/users")
    }

    @Test
    fun `when user hits users part with no admin rights he gots 403 error`() {
        val profile = mock<CommonProfile>()
        whenever(profile.roles).thenReturn(emptySet())
        assertThrows(Err::class.java) { Admin(mock(), mock(), mock(), mock()).getUsers(profile) }
                .statusCode() shouldBe 403
    }

    @Test
    fun `when user hits degrees part with no admin rights he gots 403 error`() {
        val profile = mock<CommonProfile>()
        whenever(profile.roles).thenReturn(emptySet())
        assertThrows(Err::class.java) { Admin(mock(), mock(), mock(), mock()).getUsers(profile) }
                .statusCode() shouldBe 403
    }

    @Test
    fun `when admin enters users zone he gots corresponding view`() {
        val users = users(20)
        val profile = mock<CommonProfile>()
        whenever(profile.roles).thenReturn(setOf("admin"))
        val userService = mock<UserService>()
        whenever(userService.listUsers()).thenReturn(users)
        val view = Admin(userService, mock(), mock(), mock()).getUsers(profile)
        view.users().should.equal(users)
    }

    @Test
    fun `when admin enters degrees zone he gots corresponding view`() {
        val profile = mock<CommonProfile>()
        val config = mock<Config>()
        val degreeService = mock<DegreeService>()

        val gradeList = gradeWithParentsConstructor.list(20).`val`() as List<GradeWithParents>

        whenever(profile.roles).thenReturn(setOf("admin"))
        whenever(config.getIntList(any())).thenReturn(allowedDegrees)
        whenever(config.getStringList(any())).thenReturn(allowedLetters)
        whenever(degreeService.listDegrees()).thenReturn(gradeList)
        val view = Admin(mock(), config, degreeService, mock()).degrees(profile)
        view.degrees().should.equal(gradeList)
        view.allowedDegrees().should.equal(allowedDegrees)
        view.allowedLetters().should.equal(allowedLetters)
    }

    @Test
    fun `when user without admin rights tries to create degree he gets 403`() {
        val degreeService = mock<DegreeService>()
        val commonProfile = mock<CommonProfile>()
        whenever(commonProfile.roles).thenReturn(setOf("author"))
        assertThrows(Err::class.java) {
            Admin(
                    mock(),
                    mock(),
                    degreeService,
                    mock()
            ).createDegree(
                    m.constructor(DegreeDTO::class.java).params(m.strings(), m.intSeq()).`val`(),
                    commonProfile
            )
        }.statusCode() shouldBe 403
    }

    @Test
    fun `when admin saves degree it's being tranferred to service and page renders again`() {
        val degreeService = mock<DegreeService>()
        val degreeDTO = m.constructor(DegreeDTO::class.java).params(m.strings(), m.intSeq()).`val`()
        val profile = mock<CommonProfile>()
        whenever(profile.roles).thenReturn(setOf("admin"))
        val view = Admin(mock(), mock(), degreeService, mock()).createDegree(degreeDTO, profile)
        verify(degreeService, times(1)).createDegree(degreeDTO)
    }

}

private fun users(count: Int): List<UserDTO>? {
    return m.constructor(UserDTO::class.java)
            .params(
                    m.words(),
                    m.names().first(),
                    m.names().last(),
                    m.regex("[А-Я]{1}[а-я]{6,}"),
                    m.fromStrings(listOf("admin", "author")),
                    m.emails(),
                    m.passwords()
            )
            .list(count)
            .`val`()
}

val allowedDegrees = m.intSeq().min(1).list(10).`val`()

val allowedLetters = m.regex("[А-Я]{1}").list(20).`val`().distinct()

val longSeq = m.longSeq()

val humanConstructor = m
        .constructor(HumanInfoDTO::class.java)
        .params(
                longSeq,
                m.names().last(),
                m.names().first(),
                m.probabilites(String::class.java)
                        .add(0.3, null as String?)
                        .add(0.7, m.regex("[А-Я][а-я]{4,12}"))
        )
val gradeConstructor = m
        .constructor(GradeInfoDTO::class.java)
        .params(
                m.intSeq(),
                m.fromStrings(allowedDegrees.map { degree ->
                    allowedLetters.map { letter ->
                        "$degree$letter"
                    }
                }.flatten())
        )

val parentWithChildrenConstructor = m
        .constructor(ParentWithChildren::class.java)
        .params(humanConstructor, humanConstructor.list(20))

val gradeWithParentsConstructor = m
        .constructor(Pair::class.java)
        .params(gradeConstructor, parentWithChildrenConstructor.list(20))
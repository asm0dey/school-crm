package org.ort.school.app.service

import ch.tutteli.atrium.api.infix.en_GB.*
import ch.tutteli.atrium.api.verbs.expect
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.winterbe.expekt.should
import io.kotest.core.spec.style.AnnotationSpec
import io.mockk.*
import org.ort.school.app.model.UserInfoDTO
import org.ort.school.app.repo.UserDTO
import org.ort.school.app.repo.UserRepo

class UserServiceTest : AnnotationSpec() {

    private lateinit var userRepo: UserRepo

    private lateinit var userService: UserService

    @BeforeEach
    fun setup() {
        userRepo = mockk()
        userService = UserService(userRepo, PasswordService())
    }


    @Test
    fun createUserNew() {
        val userInfo = UserInfoDTO("ad", "ad", "ad", "ad", "ad", "ad", "ad", "ad")
        every { userRepo.createUser(any()) } just Runs
        userService.createUser(userInfo)
        verify(exactly = 1) {
            userRepo.createUser(withArg {
                expect(it) {
                    feature { f(it::lastname) } toBe "ad"
                    feature { f(it::firstname) } toBe "ad"
                    feature { f(it::patronymic) } toBe "ad"
                    feature { f(it::password) } notToBeNull o startsWith "$2a$10"
                    feature { f(it::passwordConfirm) } toBe null
                    feature { f(it::role) } toBe "ad"
                    feature { f(it::username) } toBe "ad"
                    feature { f(it::email) } toBe "ad"
                }
            })
        }
        confirmVerified(userRepo)
    }

    @Test
    fun hasUsers() {
        every { userRepo.hasUsers() } returns true
        expect(userService.hasUsers()) toBe true
    }

    @Test
    fun hasUsers2() {
        whenever(userRepo.hasUsers()).thenReturn(false)
        userService.hasUsers().should.be.`false`
    }

    @Test
    fun userBy() {
        val mock = mockk<UserDTO>()
        every { userRepo.userBy("asm0dey") } returns mock
        expect(userService.userBy("asm0dey")) toBe mock
    }

    @Test
    fun countAdmins() {
        every { userRepo.countAdmins() } returns Int.MAX_VALUE - 17
        userService.countAdmins().should.equal(Int.MAX_VALUE - 17)
    }

    @Test
    fun updateUser() {
        val userInfo = UserInfoDTO("ad", "ad", "ad", "ad", "ad", "ad", "ad", "ad")
        userService.updateUser("as", userInfo)
        verify(exactly = 1) { userRepo.updateUser("as", userInfo) }
        confirmVerified(userRepo)
    }

    @Test
    fun listUsers() {
        val userDTO = mock<UserDTO>()
        whenever(userRepo.listUsers()).thenReturn(listOf(userDTO))
        userService.listUsers().should.elements(userDTO)
    }

    @Test
    fun `user is deletable if it's not admin`() {
        whenever(userRepo.rolesBy("as")).thenReturn(setOf("another"))
        userService.mayDeleteUser("as").should.be.`true`
    }

    @Test
    fun `user is deletable if there are more then one admin`() {
        whenever(userRepo.rolesBy("as")).thenReturn(setOf("another", "admin"))
        whenever(userRepo.countAdmins()).thenReturn(2)
        userService.mayDeleteUser("as").should.be.`true`
    }

    @Test
    fun `user is not deletable if he is last admin standing`() {
        whenever(userRepo.rolesBy("as")).thenReturn(setOf("another", "admin"))
        whenever(userRepo.countAdmins()).thenReturn(1)
        userService.mayDeleteUser("as").should.be.`false`
    }

    @Test
    fun deleteUser() {
        every { userRepo.deleteUser("as") } returns 1
        userService.deleteUser("as").should.equal(1)
    }

    @Test
    fun `don't delete user`() {
        whenever(userRepo.deleteUser("as")).thenReturn(0)
        userService.deleteUser("as").should.equal(0)
    }
}
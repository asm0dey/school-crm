package org.ort.school.app.service

import com.nhaarman.mockito_kotlin.*
import com.winterbe.expekt.should
import org.ort.school.app.model.UserInfoDTO
import org.ort.school.app.repo.UserDTO
import org.ort.school.app.repo.UserRepo
import kotlin.test.BeforeTest
import kotlin.test.Test

class UserServiceTest {

    private lateinit var userRepo: UserRepo

    private lateinit var userService: UserService

    @BeforeTest
    fun setup() {
        userRepo = mock()
        userService = UserService(userRepo, PasswordService())
    }

    @Test
    fun createUser() {
        val userInfo = UserInfoDTO("ad", "ad", "ad", "ad", "ad", "ad", "ad", "ad")
        userService.createUser(userInfo)
        verify(userRepo, times(1)).createUser(check {
            it.lastname.should.equal("ad")
            it.firstname.should.equal("ad")
            it.patronymic.should.equal("ad")
            it.password.should.startWith("$2a$10")
            it.passwordConfirm.should.be.`null`
            it.role.should.equal("ad")
            it.username.should.equal("ad")
            it.email.should.equal("ad")
        })
    }

    @Test
    fun hasUsers() {
        whenever(userRepo.hasUsers()).thenReturn(true)
        userService.hasUsers().should.be.`true`
    }

    @Test
    fun hasUsers2() {
        whenever(userRepo.hasUsers()).thenReturn(false)
        userService.hasUsers().should.be.`false`
    }

    @Test
    fun userBy() {
        val mock = mock<UserDTO>()
        whenever(userRepo.userBy("asm0dey")).thenReturn(mock)
        userService.userBy("asm0dey").should.equal(mock)
    }

    @Test
    fun countAdmins() {
        whenever(userRepo.countAdmins()).thenReturn(Int.MAX_VALUE - 17)
        userService.countAdmins().should.equal(Int.MAX_VALUE - 17)
    }

    @Test
    fun updateUser() {
        val userInfo = UserInfoDTO("ad", "ad", "ad", "ad", "ad", "ad", "ad", "ad")
        userService.updateUser("as", userInfo)
        verify(userRepo, times(1)).updateUser("as", userInfo)
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
        whenever(userRepo.deleteUser("as")).thenReturn(1)
        userService.deleteUser("as").should.equal(1)
    }

    @Test
    fun `don't delete user`() {
        whenever(userRepo.deleteUser("as")).thenReturn(0)
        userService.deleteUser("as").should.equal(0)
    }
}
package org.ort.school.app.service

import com.google.inject.Inject
import org.ort.school.app.model.UserInfoDTO
import org.ort.school.app.repo.UserRepo

class UserService @Inject constructor(
        private val userRepo: UserRepo,
        private val passwordService: PasswordService
) {
    fun createUser(userInfo: UserInfoDTO) = userRepo.createUser(userInfo.copy(password = passwordService.encryptPassword(userInfo.password!!)))
    fun hasUsers() = userRepo.hasUsers()
    fun userBy(username: String) = userRepo.userBy(username)
    fun countAdmins() = userRepo.countAdmins()
    fun updateUser(username: String, userInfoDTO: UserInfoDTO) = userRepo.updateUser(username, userInfoDTO)
    fun listUsers() = userRepo.listUsers()
    fun mayDeleteUser(userToDelete: String): Boolean {
        val roles = userRepo.rolesBy(userToDelete)
        if (!roles.contains("admin")) return true
        if (userRepo.countAdmins() > 1) return true
        return false
    }

    fun deleteUser(username: String) =
            userRepo.deleteUser(username)

}
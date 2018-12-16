package org.ort.school.app.service

import com.google.inject.Inject
import org.ort.school.app.repo.GroupRepo

class GroupService @Inject constructor(private val groupRepo: GroupRepo) {
    fun createGroup(name: String) {
        groupRepo.createGroup(name)
    }

}

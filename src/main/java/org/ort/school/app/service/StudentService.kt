package org.ort.school.app.service

import com.google.inject.Inject
import org.ort.school.app.repo.StudentRepo

class StudentService @Inject constructor(private val studentRepo: StudentRepo){
    fun studentByName(filter: String) = studentRepo.studentByName(filter)
}
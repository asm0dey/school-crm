package org.ort.school.app.service

import com.google.inject.Inject
import org.ort.school.app.repo.DegreeRepo
import org.ort.school.app.routes.DegreeDTO

class DegreeService @Inject constructor(private val degreeRepo: DegreeRepo) {
    fun listDegrees() = degreeRepo.listDegrees()
    fun createDegree(degree: DegreeDTO) {
        degreeRepo.createDegree(degree)
    }

    fun listDegreeNames() = degreeRepo.listDegreeNames()
}
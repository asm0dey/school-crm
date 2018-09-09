package org.ort.school.app.service

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.winterbe.expekt.should
import org.junit.Test
import org.ort.school.app.repo.DegreeRepo
import org.ort.school.app.repo.GradeInfoDTO
import org.ort.school.app.repo.GradeWithParents
import org.ort.school.app.repo.ParentWithChildren
import org.ort.school.app.routes.DegreeDTO

class DegreeServiceTest {

    @Test
    fun listDegrees() {
        val degreeRepo = mock<DegreeRepo>()
        val degreeService = DegreeService(degreeRepo)
        val degrees = listOf(GradeWithParents(GradeInfoDTO(1, "s"), listOf(ParentWithChildren(mock(), mock()))))
        whenever(degreeRepo.listDegrees()).thenReturn(degrees)
        degreeService.listDegrees().should.equal(degrees)
        verify(degreeRepo, times(1)).listDegrees()
    }

    @Test
    fun createDegree() {
        val degreeRepo = mock<DegreeRepo>()
        val degreeService = DegreeService(degreeRepo)
        val degree = DegreeDTO("s", 1)
        degreeService.createDegree(degree)
        verify(degreeRepo, times(1)).createDegree(degree)
    }

    @Test
    fun listDegreeNames() {
        val degreeRepo = mock<DegreeRepo>()
        val degreeService = DegreeService(degreeRepo)
        val degreeNames = listOf(1 to "s")
        whenever(degreeRepo.listDegreeNames()).thenReturn(degreeNames)
        degreeService.listDegreeNames().should.equal(degreeNames)
        verify(degreeRepo, times(1)).listDegreeNames()
    }

    @Test
    fun degreesAndParentsBy() {
        val degreeRepo = mock<DegreeRepo>()
        val degreeService = DegreeService(degreeRepo)
        val degreeIds = listOf(1)
        degreeService.degreesAndParentsBy(degreeIds)
        verify(degreeRepo, times(1)).degreesAndParentsBy(degreeIds)
    }
}
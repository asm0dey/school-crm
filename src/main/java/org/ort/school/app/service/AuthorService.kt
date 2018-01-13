package org.ort.school.app.service

import com.google.inject.Inject

class AuthorService @Inject constructor(private val emailService: EmailService, private val degreeService: DegreeService) {
    fun sendLetter(degreeIds: List<Int>, letterContent: String, subject: String) {
        val degreesAndParents = degreeService.degreesAndParentsBy(degreeIds)
        emailService.sendMails(degreesAndParents, letterContent, subject)
    }
}
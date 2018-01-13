package org.ort.school.app.service

import com.google.inject.Inject
import com.mashape.unirest.http.Unirest
import com.typesafe.config.Config
import com.github.salomonbrys.kotson.*

class EmailService @Inject constructor(private val config: Config) {
    fun sendMails(degreesAndParents: Map<String, MutableList<ParentInfo>>, letterContent: String, subject: String) {

        degreesAndParents
                .map { (degree, parents) ->
                    val request = Unirest.post("https://api.mailgun.net/v3/" + "sandbox4df70ec7508c4883970606b2d590f0e9.mailgun.org" + "/messages")
                            .basicAuth("api", config.getString("mailgun.key"))
                            .queryString("from", "Excited User <ort@sandbox4df70ec7508c4883970606b2d590f0e9.mailgun.org>")
                            .queryString("subject", subject)
                            .queryString("html", letterContent)
                    parents.distinct().forEach { parent ->
                        request.queryString("to", parent.email)
                    }
                    request
                            .field("recipient-variables", createParentsJson(parents, degree))
                            .asJson()
                }
    }

    private fun createParentsJson(parents: MutableList<ParentInfo>, degree: String): String {
        return jsonObject(parents
                .map { parent ->
                    parent.email!! to jsonObject(
                            "fullname" to parent.displayName,
                            "degree" to degree
                    )
                }).toString()
    }

}
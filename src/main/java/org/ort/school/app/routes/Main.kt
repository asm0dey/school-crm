package org.ort.school.app.routes

import com.google.inject.Inject
import com.google.inject.Singleton
import com.mashape.unirest.http.Unirest
import org.jooby.Result
import org.jooby.Results
import org.jooby.mvc.GET
import org.jooby.mvc.POST
import org.jooby.mvc.Path
import org.ort.school.app.model.UserInfoDTO
import org.ort.school.app.service.DegreeService
import org.ort.school.app.service.SubscribeDTO
import org.ort.school.app.service.SubscribeService
import org.ort.school.app.service.UserService
import org.ort.school.app.validate.FirstUser
import javax.validation.Validator

@Singleton
@Path("/")
class Main @Inject constructor(
        private val validator: Validator,
        private val userService: UserService,
        private val degreeService: DegreeService,
        private val subscribeService: SubscribeService
) {

    @GET
    fun home(): Result {
        if (!isInitialized()) return Results.tempRedirect("init")

        return renderIndex().put("errors", mapOf<String, String>())
    }

    @GET
    @Path("/init")
    fun init(): Result =
            if (!isInitialized()) Results.html("init")
            else Results.redirect("/")

    @POST
    @Path("/init")
    fun createFirstUser(userInfo: UserInfoDTO): Result {
        val validate = validator.validate(userInfo, FirstUser::class.java)
        if (validate.size > 0) {
            val map = validate.associate { it -> it.propertyPath.toString() to it.message }
            return Results.html("init").put("errors", map)
        }
        userService.createUser(userInfo.copy(role = "admin"))
        return Results.redirect("/")
    }

    @POST
    @Path("/")
    fun subscribe(subscribeDTO: SubscribeDTO): Result {
        val errors = validator.validate(subscribeDTO).associate { it.propertyPath.toString() to it.message }
        if (errors.isNotEmpty()) {
            return renderIndex().put("errors", errors)
        }
        subscribeService.subscribe(subscribeDTO)
        return renderIndex()
    }

    private fun renderIndex() = Results.html("index").put("degrees", degreeService.listDegreeNames())

    private fun isInitialized() = userService.hasUsers()


}

fun main(args: Array<String>) {

    val request = Unirest.post("https://api.mailgun.net/v3/" + "sandbox4df70ec7508c4883970606b2d590f0e9.mailgun.org" + "/messages")
            .basicAuth("api", "apikey")
            .queryString("from", "Excited User <ort@sandbox4df70ec7508c4883970606b2d590f0e9.mailgun.org>")
            .queryString("to", "pavel.finkelshtein@gmail.com")
            .queryString("to", "asm0dey@asm0dey.ru")
            .queryString("subject", "Hello, %recipient.first%!")
            .queryString("text", "Hello, %recipient.first%!\nIf you wish to unsubscribe, click <https://mailgun.com/unsubscribe/%recipient.id%>")
            .queryString("html", "<h1>Hello, %recipient.first%!</h1>\n<p>Hello, %recipient.first%!\n    If you wish to unsubscribe, click <a href='%unsubscribe_url%'>this link</a></p>")
            .field("recipient-variables", "{\n  \"pavel.finkelshtein@gmail.com\": {\n    \"first\": \"Bob\",\n    \"id\": 1\n  },\n  \"asm0dey@asm0dey.ru\": {\n    \"first\": \"Alice\",\n    \"id\": 2\n  }\n}")
            .asJson()

    println(request.body.toString())

}
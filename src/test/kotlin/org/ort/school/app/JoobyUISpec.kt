package org.ort.school.app
/*

import com.automation.remarks.kirk.Browser
import com.automation.remarks.kirk.Kirk.Companion.drive
import com.automation.remarks.kirk.Page
import com.automation.remarks.kirk.conditions.text
import io.kotlintest.specs.BehaviorSpec
import org.testcontainers.containers.DockerComposeContainer
import java.io.File

abstract class JoobyUISpec(body: JoobyUISpec.() -> Unit = {}) : BehaviorSpec() {
    class KDockerComposeContainer(i: File) : DockerComposeContainer<KDockerComposeContainer>(i)
    companion object{
        @JvmStatic
        private val compose = KDockerComposeContainer(File("src/test/resources/docker-compose.yml"))
                .withLocalCompose(true)
//            .withPull(false)
                .withExposedService("chrome", 4444)
                .apply { start() }

    }
    init {
        System.setProperty("kirk.remote.url", "http://localhost:${compose.getServicePort("chrome", 4444)}/wd/hub")
        System.setProperty("kirk.baseUrl", "http://crm:8080")
        body()
    }
}

class JoobyUiIT : JoobyUISpec({
    drive {
        given("I open site first time") {
            open("/")
            `when`("I look at it") {
                then("I can see first setup prompt there") {
                    at(::InitPage).element("#first-launch").shouldHave(text("Первый запуск"))
                    at(::InitPage)
                }
            }
            `when`("I've created first") {
                at(::InitPage).createFirstUser()
                then("I can see Wecome message") {
                    at(::MainPage).element("h1").shouldHave(text("Добро пожаловать"))
                }
            }
        }
    }

})

class InitPage(browser: Browser) : Page(browser) {
    fun createFirstUser(): MainPage {
        element("#username").setValue("admin")
        element("#password").setValue("adminadmin")
        element("#passwordConfirm").setValue("adminadmin")
        element("#email").setValue("nobody@nowhere.com")
        element("#send").click()
        return MainPage(browser)
    }
}

class MainPage(browser: Browser) : Page(browser) {
    override val url: String?
        get() = "/"
}
*/

package org.ort.school.app.service

import com.winterbe.expekt.should
import io.kotlintest.matchers.boolean.shouldBeFalse
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.matchers.string.shouldStartWith
import io.kotlintest.properties.assertAll
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.ShouldSpec
import org.junit.jupiter.api.Assertions.assertNotEquals

// String Fun Should Feature Behavior
// styles - encrypted - exception - check - matchers - focus
class PasswordServiceKTest
package org.ort.school.app.service

import io.kotlintest.data.suspend.forall
import io.kotlintest.matchers.string.shouldEndWith
import io.kotlintest.matchers.string.shouldStartWith
import io.kotlintest.properties.assertAll
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.FreeSpec
import io.kotlintest.specs.ShouldSpec
import io.kotlintest.tables.row
import kotlin.test.assertNotEquals

//import com.winterbe.expekt.should
// String Fun Should Feature Behavior Free
// styles - encrypted - exception - check - matchers - focus
class PasswordServiceKTest

package org.ort.school.app.service

import com.sangupta.murmur.Murmur2
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.alphanumeric
import io.kotest.property.arbitrary.katakana
import io.kotest.property.arbitrary.merge
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class MurMurTest : ShouldSpec({
    context("!our murmur match reference murmur") {
        checkAll(Arb.string(0..100, Arb.katakana().merge(Arb.alphanumeric()))) {
            MurMur2Hash.evaluate(it) shouldBe Murmur2.hash(it.toByteArray(), it.length, 0)
        }

    }
})

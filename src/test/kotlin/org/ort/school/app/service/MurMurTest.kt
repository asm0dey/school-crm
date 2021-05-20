package org.ort.school.app.service

import com.sangupta.murmur.Murmur2
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll

class MurMurTest : ShouldSpec({
    context("our murmur match reference murmur") {
        checkAll(Arb.string(100, Arb.katakana().merge(Arb.alphanumeric()).merge(Arb.hiragana()))) {
            MurMur2Hash.evaluate(it) shouldBe Murmur2.hash(it.toByteArray(), it.length, 0)
        }
    }
})

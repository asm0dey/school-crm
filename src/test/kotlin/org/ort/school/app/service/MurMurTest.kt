package org.ort.school.app.service

import com.sangupta.murmur.Murmur2
import io.kotest.core.datatest.forAll
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll

class MurMurTest : ShouldSpec({
    context("our murmur match reference murmur") {
        forAll("admin", "god", "love") {
            MurMur2Hash.evaluate(it) shouldBe Murmur2.hash(it.toByteArray(), it.length, 0)
        }
/*
        checkAll(Arb.string(20, Arb.alphanumeric() + Arb.hiragana() + Arb.egyptianHieroglyphs())) {
            MurMur2Hash.evaluate(it) shouldBe Murmur2.hash(it.toByteArray(), it.length, 0)
        }
*/
    }
})

infix operator fun <X> Arb<X>.plus(arb: Arb<X>): Arb<X> = this.merge(arb)
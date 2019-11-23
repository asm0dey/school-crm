package org.ort.school.app.service

import com.sangupta.murmur.Murmur2
import io.kotlintest.data.forall
import io.kotlintest.properties.Gen
import io.kotlintest.properties.assertAll
import io.kotlintest.properties.shrinking.Shrinker
import io.kotlintest.properties.shrinking.StringShrinker
import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec
import io.kotlintest.tables.row
import kotlin.random.Random

class MurMurTest : ShouldSpec({
    "our murmur"{
        should("match reference murmur") {
            forall(
                    row("a"),
                    row("test"),
                    row("longstring")) {
                MurMur2Hash.evaluate(it) shouldBe Murmur2.hash(it.toByteArray(), it.length, 0)
            }
            assertAll(Gen.wideString()) { input: String ->
                MurMur2Hash.evaluate(input) shouldBe Murmur2.hash(input.toByteArray(), input.length, 0)
            }
        }
    }
})

fun Gen.Companion.wideString(maxSize: Int = 100): Gen<String> = object : Gen<String> {
    override fun constants(): Iterable<String> = listOf()
    override fun random(): Sequence<String> = generateSequence { nextWideString(Random.nextInt(maxSize)) }
    override fun shrinker(): Shrinker<String>? = StringShrinker
}

fun Gen.Companion.nextWideString(length: Int): String {
    return (0 until Random.nextInt(0, if (length < 1) 1 else length))
            .map { Random.nextInt(0, Short.MAX_VALUE.toInt()).toChar() }
            .joinToString("")
}


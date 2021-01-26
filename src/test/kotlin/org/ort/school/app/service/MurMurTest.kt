package org.ort.school.app.service

import com.sangupta.murmur.Murmur2
import io.kotest.core.datatest.forAll
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class MurMurTest : ShouldSpec({
    context("our murmur match reference murmur") {
        forAll(
            "a",
            "test",
            "longstring"
        ) {
            MurMur2Hash.evaluate(it) shouldBe Murmur2.hash(it.toByteArray(), it.length, 0)
        }

    }
})

/*
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

*/

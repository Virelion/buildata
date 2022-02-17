/*
 * Copyright 2021 Maciej Ziemba
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.virelion.buildata.benchmark

import io.github.virelion.buildata.Buildable

@Buildable
data class Root(
    val a: Lvl1,
    val b: Lvl1,
    val c: Lvl1,
    val d: Lvl1,
    val e: Lvl1,
    val f: Lvl1,
    val g: Lvl1,
    val i: Lvl1,
    val j: Lvl1,
    val k: Lvl1
)

fun root() = Root(lvl1(), lvl1(), lvl1(), lvl1(), lvl1(), lvl1(), lvl1(), lvl1(), lvl1(), lvl1())

fun Root_Builder.fill() {
    this {
        a { fill() }
        b { fill() }
        c { fill() }
        d { fill() }
        e { fill() }
        f { fill() }
        g { fill() }
        i { fill() }
        j { fill() }
        k { fill() }
    }
}

@Buildable
data class Lvl1(
    val a: Lvl2,
    val b: Lvl2,
    val c: Lvl2,
    val d: Lvl2,
    val e: Lvl2,
    val f: Lvl2,
    val g: Lvl2,
    val i: Lvl2,
    val j: Lvl2,
    val k: Lvl2
)

fun lvl1() = Lvl1(lvl2(), lvl2(), lvl2(), lvl2(), lvl2(), lvl2(), lvl2(), lvl2(), lvl2(), lvl2())

fun Lvl1_Builder.fill() {
    this {
        a { fill() }
        b { fill() }
        c { fill() }
        d { fill() }
        e { fill() }
        f { fill() }
        g { fill() }
        i { fill() }
        j { fill() }
        k { fill() }
    }
}

@Buildable
data class Lvl2(
    val a: Lvl3,
    val b: Lvl3,
    val c: Lvl3,
    val d: Lvl3,
    val e: Lvl3,
    val f: Lvl3,
    val g: Lvl3,
    val i: Lvl3,
    val j: Lvl3,
    val k: Lvl3
)

fun lvl2() = Lvl2(lvl3(), lvl3(), lvl3(), lvl3(), lvl3(), lvl3(), lvl3(), lvl3(), lvl3(), lvl3())

inline fun Lvl2_Builder.fill() {
    this {
        a { fill() }
        b { fill() }
        c { fill() }
        d { fill() }
        e { fill() }
        f { fill() }
        g { fill() }
        i { fill() }
        j { fill() }
        k { fill() }
    }
}

@Buildable
data class Lvl3(
    val a: Lvl4,
    val b: Lvl4,
    val c: Lvl4,
    val d: Lvl4,
    val e: Lvl4,
    val f: Lvl4,
    val g: Lvl4,
    val i: Lvl4,
    val j: Lvl4,
    val k: Lvl4
)

fun lvl3() = Lvl3(lvl4(), lvl4(), lvl4(), lvl4(), lvl4(), lvl4(), lvl4(), lvl4(), lvl4(), lvl4())

inline fun Lvl3_Builder.fill() {
    this {
        a { fill() }
        b { fill() }
        c { fill() }
        d { fill() }
        e { fill() }
        f { fill() }
        g { fill() }
        i { fill() }
        j { fill() }
        k { fill() }
    }
}

@Buildable
data class Lvl4(
    val a: Lvl5,
    val b: Lvl5,
    val c: Lvl5,
    val d: Lvl5,
    val e: Lvl5,
    val f: Lvl5,
    val g: Lvl5,
    val i: Lvl5,
    val j: Lvl5,
    val k: Lvl5
)

fun lvl4() = Lvl4(lvl5(), lvl5(), lvl5(), lvl5(), lvl5(), lvl5(), lvl5(), lvl5(), lvl5(), lvl5())

inline fun Lvl4_Builder.fill() {
    this {
        a { fill() }
        b { fill() }
        c { fill() }
        d { fill() }
        e { fill() }
        f { fill() }
        g { fill() }
        i { fill() }
        j { fill() }
        k { fill() }
    }
}

@Buildable
data class Lvl5(
    val a: Lvl6,
    val b: Lvl6,
    val c: Lvl6,
    val d: Lvl6,
    val e: Lvl6,
    val f: Lvl6,
    val g: Lvl6,
    val i: Lvl6,
    val j: Lvl6,
    val k: Lvl6
)

fun lvl5() = Lvl5(lvl6(), lvl6(), lvl6(), lvl6(), lvl6(), lvl6(), lvl6(), lvl6(), lvl6(), lvl6())

inline fun Lvl5_Builder.fill() {
    this {
        a { fill() }
        b { fill() }
        c { fill() }
        d { fill() }
        e { fill() }
        f { fill() }
        g { fill() }
        i { fill() }
        j { fill() }
        k { fill() }
    }
}

@Buildable
data class Lvl6(
    val a: String,
    val b: String,
    val c: String,
    val d: String,
    val e: String,
    val f: String,
    val g: String,
    val i: String,
    val j: String,
    val k: String
)

fun lvl6() = Lvl6("", "", "", "", "", "", "", "", "", "")

inline fun Lvl6_Builder.fill() {
    this {
        a = ""
        b = ""
        c = ""
        d = ""
        e = ""
        f = ""
        g = ""
        i = ""
        j = ""
        k = ""
    }
}

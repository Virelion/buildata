package io.github.virelion.buildata.benchmark

import io.github.virelion.buildata.Buildable

@Buildable
data class Root(
    val a: @Buildable Lvl1,
    val b: @Buildable Lvl1,
    val c: @Buildable Lvl1,
    val d: @Buildable Lvl1,
    val e: @Buildable Lvl1,
    val f: @Buildable Lvl1,
    val g: @Buildable Lvl1,
    val i: @Buildable Lvl1,
    val j: @Buildable Lvl1,
    val k: @Buildable Lvl1
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
    val a: @Buildable Lvl2,
    val b: @Buildable Lvl2,
    val c: @Buildable Lvl2,
    val d: @Buildable Lvl2,
    val e: @Buildable Lvl2,
    val f: @Buildable Lvl2,
    val g: @Buildable Lvl2,
    val i: @Buildable Lvl2,
    val j: @Buildable Lvl2,
    val k: @Buildable Lvl2
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
    val a: @Buildable Lvl3,
    val b: @Buildable Lvl3,
    val c: @Buildable Lvl3,
    val d: @Buildable Lvl3,
    val e: @Buildable Lvl3,
    val f: @Buildable Lvl3,
    val g: @Buildable Lvl3,
    val i: @Buildable Lvl3,
    val j: @Buildable Lvl3,
    val k: @Buildable Lvl3
)

fun lvl2() = Lvl2(lvl3(), lvl3(), lvl3(), lvl3(), lvl3(), lvl3(), lvl3(), lvl3(), lvl3(), lvl3())

fun Lvl2_Builder.fill() {
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
    val a: @Buildable Lvl4,
    val b: @Buildable Lvl4,
    val c: @Buildable Lvl4,
    val d: @Buildable Lvl4,
    val e: @Buildable Lvl4,
    val f: @Buildable Lvl4,
    val g: @Buildable Lvl4,
    val i: @Buildable Lvl4,
    val j: @Buildable Lvl4,
    val k: @Buildable Lvl4
)

fun lvl3() = Lvl3(lvl4(), lvl4(), lvl4(), lvl4(), lvl4(), lvl4(), lvl4(), lvl4(), lvl4(), lvl4())

fun Lvl3_Builder.fill() {
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
    val a: @Buildable Lvl5,
    val b: @Buildable Lvl5,
    val c: @Buildable Lvl5,
    val d: @Buildable Lvl5,
    val e: @Buildable Lvl5,
    val f: @Buildable Lvl5,
    val g: @Buildable Lvl5,
    val i: @Buildable Lvl5,
    val j: @Buildable Lvl5,
    val k: @Buildable Lvl5
)

fun lvl4() = Lvl4(lvl5(), lvl5(), lvl5(), lvl5(), lvl5(), lvl5(), lvl5(), lvl5(), lvl5(), lvl5())

fun Lvl4_Builder.fill() {
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
    val a: @Buildable Lvl6,
    val b: @Buildable Lvl6,
    val c: @Buildable Lvl6,
    val d: @Buildable Lvl6,
    val e: @Buildable Lvl6,
    val f: @Buildable Lvl6,
    val g: @Buildable Lvl6,
    val i: @Buildable Lvl6,
    val j: @Buildable Lvl6,
    val k: @Buildable Lvl6
)

fun lvl5() = Lvl5(lvl6(), lvl6(), lvl6(), lvl6(), lvl6(), lvl6(), lvl6(), lvl6(), lvl6(), lvl6())

fun Lvl5_Builder.fill() {
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

fun Lvl6_Builder.fill() {
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
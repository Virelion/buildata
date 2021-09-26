package io.github.virelion.buildata.benchmark

import io.github.virelion.buildata.Buildable

@Buildable
data class RootWithDefaults(
    val a: Lvl1WithDefaults = Lvl1WithDefaults(),
    val b: Lvl1WithDefaults = Lvl1WithDefaults(),
    val c: Lvl1WithDefaults = Lvl1WithDefaults(),
    val d: Lvl1WithDefaults = Lvl1WithDefaults(),
    val e: Lvl1WithDefaults = Lvl1WithDefaults(),
    val f: Lvl1WithDefaults = Lvl1WithDefaults(),
    val g: Lvl1WithDefaults = Lvl1WithDefaults(),
    val i: Lvl1WithDefaults = Lvl1WithDefaults(),
    val j: Lvl1WithDefaults = Lvl1WithDefaults(),
    val k: Lvl1WithDefaults = Lvl1WithDefaults()
)

fun RootWithDefaults_Builder.fill() {
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
data class Lvl1WithDefaults(
    val a: Lvl2WithDefaults = Lvl2WithDefaults(),
    val b: Lvl2WithDefaults = Lvl2WithDefaults(),
    val c: Lvl2WithDefaults = Lvl2WithDefaults(),
    val d: Lvl2WithDefaults = Lvl2WithDefaults(),
    val e: Lvl2WithDefaults = Lvl2WithDefaults(),
    val f: Lvl2WithDefaults = Lvl2WithDefaults(),
    val g: Lvl2WithDefaults = Lvl2WithDefaults(),
    val i: Lvl2WithDefaults = Lvl2WithDefaults(),
    val j: Lvl2WithDefaults = Lvl2WithDefaults(),
    val k: Lvl2WithDefaults = Lvl2WithDefaults()
)

fun Lvl1WithDefaults_Builder.fill() {
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
data class Lvl2WithDefaults(
    val a: Lvl3WithDefaults = Lvl3WithDefaults(),
    val b: Lvl3WithDefaults = Lvl3WithDefaults(),
    val c: Lvl3WithDefaults = Lvl3WithDefaults(),
    val d: Lvl3WithDefaults = Lvl3WithDefaults(),
    val e: Lvl3WithDefaults = Lvl3WithDefaults(),
    val f: Lvl3WithDefaults = Lvl3WithDefaults(),
    val g: Lvl3WithDefaults = Lvl3WithDefaults(),
    val i: Lvl3WithDefaults = Lvl3WithDefaults(),
    val j: Lvl3WithDefaults = Lvl3WithDefaults(),
    val k: Lvl3WithDefaults = Lvl3WithDefaults()
)

fun Lvl2WithDefaults_Builder.fill() {
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
data class Lvl3WithDefaults(
    val a: Lvl4WithDefaults = Lvl4WithDefaults(),
    val b: Lvl4WithDefaults = Lvl4WithDefaults(),
    val c: Lvl4WithDefaults = Lvl4WithDefaults(),
    val d: Lvl4WithDefaults = Lvl4WithDefaults(),
    val e: Lvl4WithDefaults = Lvl4WithDefaults(),
    val f: Lvl4WithDefaults = Lvl4WithDefaults(),
    val g: Lvl4WithDefaults = Lvl4WithDefaults(),
    val i: Lvl4WithDefaults = Lvl4WithDefaults(),
    val j: Lvl4WithDefaults = Lvl4WithDefaults(),
    val k: Lvl4WithDefaults = Lvl4WithDefaults()
)

fun Lvl3WithDefaults_Builder.fill() {
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
data class Lvl4WithDefaults(
    val a: Lvl5WithDefaults = Lvl5WithDefaults(),
    val b: Lvl5WithDefaults = Lvl5WithDefaults(),
    val c: Lvl5WithDefaults = Lvl5WithDefaults(),
    val d: Lvl5WithDefaults = Lvl5WithDefaults(),
    val e: Lvl5WithDefaults = Lvl5WithDefaults(),
    val f: Lvl5WithDefaults = Lvl5WithDefaults(),
    val g: Lvl5WithDefaults = Lvl5WithDefaults(),
    val i: Lvl5WithDefaults = Lvl5WithDefaults(),
    val j: Lvl5WithDefaults = Lvl5WithDefaults(),
    val k: Lvl5WithDefaults = Lvl5WithDefaults()
)

fun Lvl4WithDefaults_Builder.fill() {
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
data class Lvl5WithDefaults(
    val a: Lvl6WithDefaults = Lvl6WithDefaults(),
    val b: Lvl6WithDefaults = Lvl6WithDefaults(),
    val c: Lvl6WithDefaults = Lvl6WithDefaults(),
    val d: Lvl6WithDefaults = Lvl6WithDefaults(),
    val e: Lvl6WithDefaults = Lvl6WithDefaults(),
    val f: Lvl6WithDefaults = Lvl6WithDefaults(),
    val g: Lvl6WithDefaults = Lvl6WithDefaults(),
    val i: Lvl6WithDefaults = Lvl6WithDefaults(),
    val j: Lvl6WithDefaults = Lvl6WithDefaults(),
    val k: Lvl6WithDefaults = Lvl6WithDefaults()
)

fun Lvl5WithDefaults_Builder.fill() {
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
data class Lvl6WithDefaults(
    val a: String = "",
    val b: String = "",
    val c: String = "",
    val d: String = "",
    val e: String = "",
    val f: String = "",
    val g: String = "",
    val i: String = "",
    val j: String = "",
    val k: String = ""
)

fun Lvl6WithDefaults_Builder.fill() {
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

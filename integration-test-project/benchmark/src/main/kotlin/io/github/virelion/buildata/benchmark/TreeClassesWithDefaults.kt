package io.github.virelion.buildata.benchmark

import io.github.virelion.buildata.Buildable

@Buildable
data class RootWithDefaults(
    val a: @Buildable Lvl1WithDefaults = Lvl1WithDefaults(),
    val b: @Buildable Lvl1WithDefaults = Lvl1WithDefaults(),
    val c: @Buildable Lvl1WithDefaults = Lvl1WithDefaults(),
    val d: @Buildable Lvl1WithDefaults = Lvl1WithDefaults(),
    val e: @Buildable Lvl1WithDefaults = Lvl1WithDefaults(),
    val f: @Buildable Lvl1WithDefaults = Lvl1WithDefaults(),
    val g: @Buildable Lvl1WithDefaults = Lvl1WithDefaults(),
    val i: @Buildable Lvl1WithDefaults = Lvl1WithDefaults(),
    val j: @Buildable Lvl1WithDefaults = Lvl1WithDefaults(),
    val k: @Buildable Lvl1WithDefaults = Lvl1WithDefaults()
)

@Buildable
data class Lvl1WithDefaults(
    val a: @Buildable Lvl2WithDefaults = Lvl2WithDefaults(),
    val b: @Buildable Lvl2WithDefaults = Lvl2WithDefaults(),
    val c: @Buildable Lvl2WithDefaults = Lvl2WithDefaults(),
    val d: @Buildable Lvl2WithDefaults = Lvl2WithDefaults(),
    val e: @Buildable Lvl2WithDefaults = Lvl2WithDefaults(),
    val f: @Buildable Lvl2WithDefaults = Lvl2WithDefaults(),
    val g: @Buildable Lvl2WithDefaults = Lvl2WithDefaults(),
    val i: @Buildable Lvl2WithDefaults = Lvl2WithDefaults(),
    val j: @Buildable Lvl2WithDefaults = Lvl2WithDefaults(),
    val k: @Buildable Lvl2WithDefaults = Lvl2WithDefaults()
)

@Buildable
data class Lvl2WithDefaults(
    val a: @Buildable Lvl3WithDefaults = Lvl3WithDefaults(),
    val b: @Buildable Lvl3WithDefaults = Lvl3WithDefaults(),
    val c: @Buildable Lvl3WithDefaults = Lvl3WithDefaults(),
    val d: @Buildable Lvl3WithDefaults = Lvl3WithDefaults(),
    val e: @Buildable Lvl3WithDefaults = Lvl3WithDefaults(),
    val f: @Buildable Lvl3WithDefaults = Lvl3WithDefaults(),
    val g: @Buildable Lvl3WithDefaults = Lvl3WithDefaults(),
    val i: @Buildable Lvl3WithDefaults = Lvl3WithDefaults(),
    val j: @Buildable Lvl3WithDefaults = Lvl3WithDefaults(),
    val k: @Buildable Lvl3WithDefaults = Lvl3WithDefaults()
)

@Buildable
data class Lvl3WithDefaults(
    val a: @Buildable Lvl4WithDefaults = Lvl4WithDefaults(),
    val b: @Buildable Lvl4WithDefaults = Lvl4WithDefaults(),
    val c: @Buildable Lvl4WithDefaults = Lvl4WithDefaults(),
    val d: @Buildable Lvl4WithDefaults = Lvl4WithDefaults(),
    val e: @Buildable Lvl4WithDefaults = Lvl4WithDefaults(),
    val f: @Buildable Lvl4WithDefaults = Lvl4WithDefaults(),
    val g: @Buildable Lvl4WithDefaults = Lvl4WithDefaults(),
    val i: @Buildable Lvl4WithDefaults = Lvl4WithDefaults(),
    val j: @Buildable Lvl4WithDefaults = Lvl4WithDefaults(),
    val k: @Buildable Lvl4WithDefaults = Lvl4WithDefaults()
)

@Buildable
data class Lvl4WithDefaults(
    val a: @Buildable Lvl5WithDefaults = Lvl5WithDefaults(),
    val b: @Buildable Lvl5WithDefaults = Lvl5WithDefaults(),
    val c: @Buildable Lvl5WithDefaults = Lvl5WithDefaults(),
    val d: @Buildable Lvl5WithDefaults = Lvl5WithDefaults(),
    val e: @Buildable Lvl5WithDefaults = Lvl5WithDefaults(),
    val f: @Buildable Lvl5WithDefaults = Lvl5WithDefaults(),
    val g: @Buildable Lvl5WithDefaults = Lvl5WithDefaults(),
    val i: @Buildable Lvl5WithDefaults = Lvl5WithDefaults(),
    val j: @Buildable Lvl5WithDefaults = Lvl5WithDefaults(),
    val k: @Buildable Lvl5WithDefaults = Lvl5WithDefaults()
)

@Buildable
data class Lvl5WithDefaults(
    val a: @Buildable Lvl6WithDefaults = Lvl6WithDefaults(),
    val b: @Buildable Lvl6WithDefaults = Lvl6WithDefaults(),
    val c: @Buildable Lvl6WithDefaults = Lvl6WithDefaults(),
    val d: @Buildable Lvl6WithDefaults = Lvl6WithDefaults(),
    val e: @Buildable Lvl6WithDefaults = Lvl6WithDefaults(),
    val f: @Buildable Lvl6WithDefaults = Lvl6WithDefaults(),
    val g: @Buildable Lvl6WithDefaults = Lvl6WithDefaults(),
    val i: @Buildable Lvl6WithDefaults = Lvl6WithDefaults(),
    val j: @Buildable Lvl6WithDefaults = Lvl6WithDefaults(),
    val k: @Buildable Lvl6WithDefaults = Lvl6WithDefaults()
)

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

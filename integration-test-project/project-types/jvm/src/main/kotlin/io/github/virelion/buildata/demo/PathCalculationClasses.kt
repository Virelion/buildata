package io.github.virelion.buildata.demo

import io.github.virelion.buildata.Buildable

@Buildable
data class Root(
    val inner1: Inner1 = Inner1(),
    val nullableLeaf: LeafNode? = null
)

@Buildable
data class Inner1(
    val inner2: Inner2 = Inner2(),
    val leafWithNullables: LeafWithNullables = LeafWithNullables(),
    // lists
    val innerList: List<Inner2> = listOf(),
    val listOfNullables: List<Inner2?> = listOf(null),
    val nullableList: List<Inner2>? = null,
    val nullableListOfNullables: List<Inner2?>? = listOf(null),
    // map
    val innerMap: Map<String, Inner2> = mapOf(),
    val mapOfNullables: Map<String, Inner2?> = mapOf("null" to null),
    val nullableMap: Map<String, Inner2>? = null,
    val nullableMapOfNullables: Map<String, Inner2?>? =  mapOf("null" to null)
)

@Buildable
data class Inner2(
    val leaf: LeafNode = LeafNode()
)

@Buildable
data class LeafNode(
    val string: String = "",
    val boolean: Boolean = false,
    val int: Int = 0,
    val uInt: UInt = 0u,
    val long: Long = 0L,
    val uLong: ULong = 0uL,
    val byte: Byte = 0,
    val uByte: UByte = 0x0u,
    val short: Short = 0,
    val uShort: UShort = 0u,
    val float: Float = 0.0f,
    val double: Double = 0.0
)

@Buildable
data class LeafWithNullables(
    val string: String? = "",
    val boolean: Boolean? = false,
    val int: Int? = 0,
    val uInt: UInt? = 0u,
    val long: Long? = 0L,
    val uLong: ULong? = 0uL,
    val byte: Byte? = 0,
    val uByte: UByte? = 0x0u,
    val short: Short? = 0,
    val uShort: UShort? = 0u,
    val float: Float? = 0.0f,
    val double: Double? = 0.0
)

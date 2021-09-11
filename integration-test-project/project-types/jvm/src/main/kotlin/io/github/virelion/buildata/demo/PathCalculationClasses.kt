package io.github.virelion.buildata.demo

import io.github.virelion.buildata.Buildable

@Buildable
data class Root(
    val inner1: @Buildable Inner1 = Inner1(),
    val nullableLeaf: @Buildable LeafNode? = null
)

@Buildable
data class Inner1(
    val inner2: @Buildable Inner2 = Inner2(),
//    val innerList: List< Inner2> = listOf(),
//    val innerMap: Map<String, Inner2> = mapOf(),
    val leafWithNullables: @Buildable LeafWithNullables = LeafWithNullables()
)

@Buildable
data class Inner2(
    val leaf: @Buildable LeafNode = LeafNode()
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

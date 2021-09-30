package io.github.virelion.buildata.integration.path.inner

import io.github.virelion.buildata.Buildable
import io.github.virelion.buildata.path.PathReflection

@Buildable
@PathReflection
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
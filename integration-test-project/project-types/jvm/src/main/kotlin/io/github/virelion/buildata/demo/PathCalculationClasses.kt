package io.github.virelion.buildata.demo

import com.fasterxml.jackson.annotation.JsonAlias
import io.github.virelion.buildata.Buildable
import io.github.virelion.buildata.path.PathElementName
import io.github.virelion.buildata.path.PathReflection
import kotlinx.serialization.SerialName

@Buildable
@PathReflection
data class Root(
    val inner1: Inner1 = Inner1(),
    val nullableLeaf: LeafNode? = null
)

@Buildable
@PathReflection
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
@PathReflection
data class Inner2(
    val leaf: LeafNode = LeafNode()
)

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

@Buildable
@PathReflection
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

@PathReflection
data class AnnotatedLeaf(
    @PathElementName("PATH_ELEMENT_NAME")
    val pathElementNameAnnotated: String,

    @JsonAlias("JACKSON_JSON_ALIAS")
    val jacksonAnnotatedWithAlias: String,

    @SerialName("KOTLINX_SERIALIZATION_SERIAL_NAME")
    val kotlinxSerializationAnnotated: String
)
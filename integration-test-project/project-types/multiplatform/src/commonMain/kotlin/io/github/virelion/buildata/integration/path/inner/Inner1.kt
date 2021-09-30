package io.github.virelion.buildata.integration.path.inner

import io.github.virelion.buildata.Buildable
import io.github.virelion.buildata.path.PathReflection

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
    val nullableMapOfNullables: Map<String, Inner2?>? = mapOf("null" to null)
)
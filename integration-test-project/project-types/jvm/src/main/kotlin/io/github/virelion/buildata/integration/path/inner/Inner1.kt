package io.github.virelion.buildata.integration.path.inner

import io.github.virelion.buildata.Buildable
import io.github.virelion.buildata.path.PathReflection

@Buildable
@PathReflection
data class Inner1(
    val inner2: Inner2 = Inner2(),
    val leafWithNullables: LeafWithNullables = LeafWithNullables(),

    // lists
    val innerList: ArrayList<Inner2> = arrayListOf(),
    val listOfNullables: ArrayList<Inner2?> = arrayListOf(null),
    val nullableList: ArrayList<Inner2>? = null,
    val nullableListOfNullables: ArrayList<Inner2?>? = arrayListOf(null),

    // map
    val innerMap: LinkedHashMap<String, Inner2> = linkedMapOf(),
    val mapOfNullables: LinkedHashMap<String, Inner2?> = linkedMapOf("null" to null),
    val nullableMap: LinkedHashMap<String, Inner2>? = null,
    val nullableMapOfNullables: LinkedHashMap<String, Inner2?>? = linkedMapOf("null" to null),

    // int map
    val innerIntMap: LinkedHashMap<Int, Inner2> = linkedMapOf(),
    val intMapOfNullables: LinkedHashMap<Int, Inner2?> = linkedMapOf(0 to null),
    val nullableIntMap: LinkedHashMap<Int, Inner2>? = null,
    val nullableIntMapOfNullables: LinkedHashMap<Int, Inner2?>? = linkedMapOf(0 to null),

//    // array
//    val innerArray: Array<Inner2> = arrayOf(),
//    val arrayOfNullables: Array<Inner2?> = arrayOf(null),
//    val nullableArray: Array<Inner2>? = null,
//    val nullableArrayOfNullables: Array<Inner2?>? = arrayOf(null)
)
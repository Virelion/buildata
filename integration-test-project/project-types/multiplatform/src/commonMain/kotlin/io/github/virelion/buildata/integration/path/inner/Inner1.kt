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

    // array
    val innerArray: Array<Inner2> = arrayOf(),
    val arrayOfNullables: Array<Inner2?> = arrayOf(null),
    val nullableArray: Array<Inner2>? = null,
    val nullableArrayOfNullables: Array<Inner2?>? = arrayOf(null)
)

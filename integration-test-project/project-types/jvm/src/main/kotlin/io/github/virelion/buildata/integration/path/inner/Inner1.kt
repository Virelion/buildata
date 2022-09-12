/*
 * Copyright 2022 Maciej Ziemba
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

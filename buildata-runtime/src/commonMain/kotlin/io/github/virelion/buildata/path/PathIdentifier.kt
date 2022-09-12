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

package io.github.virelion.buildata.path

/**
 * Abstraction for various path element identification.
 */
sealed class PathIdentifier

/**
 * Element accessed as [Int] index.
 *
 * Occurs when accessed element is part of the List.
 * ```kotlin
 * @PathReflection
 * data class Data(val list: List<String>)
 *
 * KClass<Data>.path().list[2]
 * ```
 */
data class IntIndexPathIdentifier(val index: Int) : PathIdentifier()

/**
 * Element accessed as [String] index.
 *
 * Occurs when accessed element is part of the Map with String keys.
 * ```kotlin
 * @PathReflection
 * data class Data(val map: Map<String, String>)
 *
 * KClass<Data>.path().map["element"]
 * ```
 */
data class StringIndexPathIdentifier(val index: String) : PathIdentifier()

/**
 * Element accessed as [String] property name.
 *
 * Occurs when accessed element is regular class member.
 * ```kotlin
 * @PathReflection
 * data class Data(val str: String)
 *
 * KClass<Data>.path().str
 * ```
 */
data class StringNamePathIdentifier(val name: String) : PathIdentifier()

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

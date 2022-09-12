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

package io.github.virelion.buildata.integration.path

import io.github.virelion.buildata.integration.path.inner.Inner2
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MapPathCalculation {
    @Test
    fun testMapAccess() {
        val root = Root::class.build {
            inner1 {
                innerMap = mapOf("key" to Inner2())
            }
        }

        with(root.withPath().inner1.innerMap["key"].leaf.string) {
            assertEquals("", value())
            assertEquals("$.inner1.innerMap['key'].leaf.string", path().jsonPath)
        }
    }

    @Test
    fun testListAccessOutOfBoundsElement() {
        val root = Root::class.build {
            inner1 {
                innerMap = mapOf("key" to Inner2())
            }
        }
        with(root.withPath().inner1.innerMap["missingKey"].leaf.string) {
            assertNull(value())
            assertEquals("$.inner1.innerMap['missingKey'].leaf.string", path().jsonPath)
        }
    }

    @Test
    fun testMapOfNullablesAccess() {
        val root = Root::class.build {
            inner1 {
                mapOfNullables = mapOf("null" to null)
            }
        }

        with(root.withPath().inner1.mapOfNullables["null"].leaf.string) {
            assertEquals(null, value())
            assertEquals("$.inner1.mapOfNullables['null'].leaf.string", path().jsonPath)
        }
    }

    @Test
    fun testNullableMapAccess() {
        val root = Root::class.build {
            inner1 {
                nullableMap = mapOf("key" to Inner2())
            }
        }

        with(root.withPath().inner1.nullableMap["key"].leaf.string) {
            assertEquals("", value())
            assertEquals("$.inner1.nullableMap['key'].leaf.string", path().jsonPath)
        }
    }

    @Test
    fun testNullableMapOfNullablesAccess() {
        val root = Root::class.build {
            inner1 {
                nullableMapOfNullables = mapOf("key" to Inner2())
            }
        }

        with(root.withPath().inner1.nullableMapOfNullables["key"].leaf.string) {
            assertEquals("", value())
            assertEquals("$.inner1.nullableMapOfNullables['key'].leaf.string", path().jsonPath)
        }
    }
}

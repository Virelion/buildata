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

package io.github.virelion.buildata.integration.access

import io.github.virelion.buildata.access.ElementNotFoundException
import io.github.virelion.buildata.path.IntIndexPathIdentifier
import io.github.virelion.buildata.path.StringIndexPathIdentifier
import io.github.virelion.buildata.path.StringNamePathIdentifier
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DynamicAccessTest {
    private val root = DynamicAccessRoot(
        mapOf("root" to "Root"),
        mapOf(1 to "Root"),
        listOf("root"),
        arrayOf("arrayElement"),
        DynamicAccessInner1(
            mapOf("inner1" to "Inner1"),
            listOf("inner1"),
            mapOf(1 to "element"),
            arrayOf("arrayElement"),
        ),
        null,
        "value"
    )

    @Test
    fun isValueAccessPossible() {
        with(root.dynamicAccessor) {
            assertEquals(root.map, this["map"])
            assertEquals(root.list, this["list"])
            assertEquals(root.array, this["array"])
            assertEquals(root.intMap, this["intMap"])
            assertEquals(root.element, this["element"])
            assertNull(this["nullable_element"])

            assertEquals(root.element.map, this["element.map"])
            assertEquals(root.element.list, this["element.list"])
            assertEquals(root.element.intMap, this["element.intMap"])
            assertEquals(root.element.array, this["element.array"])

            assertEquals(root.element.map["inner1"], this["element.map['inner1']"])
            assertEquals(root.element.list[0], this["element.list[0]"])
            assertEquals(root.element.intMap[1], this["element.intMap[1]"])
            assertEquals(root.element.array[0], this["element.array[0]"])
            assertEquals("custom", this["element.customStringAccessible['custom']"])
            assertEquals("custom", this["element.customIntAccessible[42]"])
            assertEquals("value", this["CUSTOM_NAME"])
        }
    }

    @Test
    fun cannotFindElement() {
        with(root.dynamicAccessor) {
            assertFailsWith<ElementNotFoundException> {
                println(this.get<String>("notAnElement"))
            }.apply {
                assertTrue(this.pathProcessed.path.isEmpty())
                assertEquals(root, lastItemProcessed)
                assertEquals(StringNamePathIdentifier("notAnElement"), lastProcessedPathIdentifier)
            }
        }
    }

    @Test
    fun cannotFindElementOnStringIndexMap() {
        with(root.dynamicAccessor) {
            assertFailsWith<ElementNotFoundException> {
                println(this.get<String>("map['element']"))
            }.apply {
                assertEquals(listOf(StringNamePathIdentifier("map")), this.pathProcessed.path)
                assertEquals(root.map, lastItemProcessed)
                assertEquals(StringIndexPathIdentifier("element"), lastProcessedPathIdentifier)
            }
        }
    }

    @Test
    fun cannotFindElementOnIntIndexMap() {
        with(root.dynamicAccessor) {
            assertFailsWith<ElementNotFoundException> {
                println(this.get<String>("intMap[42]"))
            }.apply {
                assertEquals(listOf(StringNamePathIdentifier("intMap")), this.pathProcessed.path)
                assertEquals(root.intMap, lastItemProcessed)
                assertEquals(IntIndexPathIdentifier(42), lastProcessedPathIdentifier)
            }
        }
    }

    @Test
    fun cannotFindElementOnIntIndexList() {
        with(root.dynamicAccessor) {
            assertFailsWith<ElementNotFoundException> {
                println(this.get<String>("list[42]"))
            }.apply {
                assertEquals(listOf(StringNamePathIdentifier("list")), this.pathProcessed.path)
                assertEquals(root.list, lastItemProcessed)
                assertEquals(IntIndexPathIdentifier(42), lastProcessedPathIdentifier)
            }
        }
    }

    @Test
    fun cannotFindElementOnIntIndexArray() {
        with(root.dynamicAccessor) {
            assertFailsWith<ElementNotFoundException> {
                println(this.get<String>("array[42]"))
            }.apply {
                assertEquals(listOf(StringNamePathIdentifier("array")), this.pathProcessed.path)
                assertEquals(root.array, lastItemProcessed)
                assertEquals(IntIndexPathIdentifier(42), lastProcessedPathIdentifier)
            }
        }
    }

    @Test
    fun cannotFindElementOnCustomStringAccessible() {
        with(root.dynamicAccessor) {
            assertFailsWith<ElementNotFoundException> {
                println(this.get<String>("element.customStringAccessible['otherKey']"))
            }.apply {
                assertEquals(listOf(StringNamePathIdentifier("element"), StringNamePathIdentifier("customStringAccessible")), this.pathProcessed.path)
                assertEquals(root.element.customStringAccessible, lastItemProcessed)
                assertEquals(StringIndexPathIdentifier("otherKey"), lastProcessedPathIdentifier)
            }
        }
    }

    @Test
    fun cannotFindElementOnCustomIntAccessible() {
        with(root.dynamicAccessor) {
            assertFailsWith<ElementNotFoundException> {
                println(this.get<String>("element.customIntAccessible[1]"))
            }.apply {
                assertEquals(listOf(StringNamePathIdentifier("element"), StringNamePathIdentifier("customIntAccessible")), this.pathProcessed.path)
                assertEquals(root.element.customIntAccessible, lastItemProcessed)
                assertEquals(IntIndexPathIdentifier(1), lastProcessedPathIdentifier)
            }
        }
    }

    @Test
    fun complexScenarioException() {
        val data = ComplexData(
            mapOf("mapKey" to listOf(root))
        )
        with(data.dynamicAccessor) {
            assertFailsWith<ElementNotFoundException> {
                println(this.get<String>("$.data['mapKey'][0].element.customIntAccessible[42]"))
            }.apply {
                assertEquals(
                    listOf(
                        StringNamePathIdentifier("data"),
                        StringIndexPathIdentifier("mapKey"),
                        IntIndexPathIdentifier(0)
                    ),
                    this.pathProcessed.path
                )
                assertEquals(data.data["mapKey"]!![0], lastItemProcessed)
                assertEquals(StringNamePathIdentifier("element"), lastProcessedPathIdentifier)
            }
        }
    }
}

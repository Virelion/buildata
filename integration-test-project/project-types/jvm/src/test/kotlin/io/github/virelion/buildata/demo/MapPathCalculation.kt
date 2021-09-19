package io.github.virelion.buildata.demo

import io.github.virelion.buildata.path.MissingElementException
import io.github.virelion.buildata.path.jsonPath
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MapPathCalculation {
    @Test
    fun testMapAccess() {
        val root = Root::class.build {
            inner1 {
                innerMap = mapOf("key" to Inner2())
            }
        }

        with(root.valueWithPath { inner1.innerMap["key"].leaf.string }) {
            assertEquals("", value)
            assertEquals("$.inner1.innerMap['key'].leaf.string", jsonPath)
        }
    }

    @Test
    fun testListAccessOutOfBoundsElement() {
        val root = Root::class.build {
            inner1 {
                innerMap = mapOf("key" to Inner2())
            }
        }
        var failed = false
        try {
            root.valueWithPath { inner1.innerMap["notAKey"].leaf.string }
        } catch (e: MissingElementException) {
            failed = true
            assertEquals("notAKey", e.index)
            assertEquals("$.inner1.innerMap", e.path.jsonPath)
            assertEquals("There is no item on index 'notAKey' of '$.inner1.innerMap'", e.message)
        }
        assertTrue(failed)
    }

    @Test
    fun testMapOfNullablesAccess() {
        val root = Root::class.build {
            inner1 {
                mapOfNullables = mapOf("null" to null)
            }
        }

        with(root.valueWithPath { inner1.mapOfNullables["null"].leaf.string }) {
            assertEquals(null, value)
            assertEquals("$.inner1.mapOfNullables['null'].leaf.string", jsonPath)
        }
    }

    @Test
    fun testNullableMapAccess() {
        val root = Root::class.build {
            inner1 {
                nullableMap = mapOf("key" to Inner2())
            }
        }

        with(root.valueWithPath { inner1.nullableMap["key"].leaf.string }) {
            assertEquals("", value)
            assertEquals("$.inner1.nullableMap['key'].leaf.string", jsonPath)
        }
    }

    @Test
    fun testNullableMapOfNullablesAccess() {
        val root = Root::class.build {
            inner1 {
                nullableMapOfNullables = mapOf("key" to Inner2())
            }
        }

        with(root.valueWithPath { inner1.nullableMapOfNullables["key"].leaf.string }) {
            assertEquals("", value)
            assertEquals("$.inner1.nullableMapOfNullables['key'].leaf.string", jsonPath)
        }
    }
}

package io.github.virelion.buildata.demo

import org.junit.Test
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
            assertEquals("$.inner1.mapOfNullables['null'].leaf.string",  path().jsonPath)
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
            assertEquals("",  value())
            assertEquals("$.inner1.nullableMap['key'].leaf.string",  path().jsonPath)
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
            assertEquals("",  value())
            assertEquals("$.inner1.nullableMapOfNullables['key'].leaf.string",  path().jsonPath)
        }
    }
}

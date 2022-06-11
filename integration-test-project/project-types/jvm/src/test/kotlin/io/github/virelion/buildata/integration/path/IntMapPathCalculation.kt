package io.github.virelion.buildata.integration.path

import io.github.virelion.buildata.integration.path.inner.Inner2
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class IntMapPathCalculation {
    @Test
    fun testMapAccess() {
        val root = Root::class.build {
            inner1 {
                innerIntMap = linkedMapOf(3 to Inner2())
            }
        }

        with(root.withPath().inner1.innerIntMap[3].leaf.string) {
            assertEquals("", value())
            assertEquals("$.inner1.innerIntMap[3].leaf.string", path().jsonPath)
        }
    }

    @Test
    fun testMapAccessOutOfBoundsElement() {
        val root = Root::class.build {
            inner1 {
                innerIntMap = linkedMapOf(3 to Inner2())
            }
        }
        with(root.withPath().inner1.innerIntMap[4].leaf.string) {
            assertNull(value())
            assertEquals("$.inner1.innerIntMap[4].leaf.string", path().jsonPath)
        }
    }

    @Test
    fun testMapOfNullablesAccess() {
        val root = Root::class.build {
            inner1 {
                intMapOfNullables = linkedMapOf(3 to null)
            }
        }

        with(root.withPath().inner1.intMapOfNullables[3].leaf.string) {
            assertEquals(null, value())
            assertEquals("$.inner1.intMapOfNullables[3].leaf.string", path().jsonPath)
        }
    }

    @Test
    fun testNullableMapAccess() {
        val root = Root::class.build {
            inner1 {
                nullableIntMap = linkedMapOf(3 to Inner2())
            }
        }

        with(root.withPath().inner1.nullableIntMap[3].leaf.string) {
            assertEquals("", value())
            assertEquals("$.inner1.nullableIntMap[3].leaf.string", path().jsonPath)
        }
    }

    @Test
    fun testNullableMapOfNullablesAccess() {
        val root = Root::class.build {
            inner1 {
                nullableIntMapOfNullables = linkedMapOf(3 to Inner2())
            }
        }

        with(root.withPath().inner1.nullableIntMapOfNullables[3].leaf.string) {
            assertEquals("", value())
            assertEquals("$.inner1.nullableIntMapOfNullables[3].leaf.string", path().jsonPath)
        }
    }
}

package io.github.virelion.buildata.integration.path

import io.github.virelion.buildata.integration.path.inner.Inner2
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ArrayPathCalculation {
    @Test
    fun testArrayAccess() {
        val root = Root::class.build {
            inner1 {
                innerArray = arrayOf(Inner2())
            }
        }

        with(root.withPath().inner1.innerArray[0].leaf.string) {
            assertEquals("", value())
            assertEquals("$.inner1.innerArray[0].leaf.string", path().jsonPath)
        }
    }

    @Test
    fun testArrayAccessOutOfBoundsElement() {
        val root = Root::class.build {
            inner1 {
                innerArray = arrayOf(Inner2())
            }
        }
        with(root.withPath().inner1.innerArray[1].leaf.string) {
            assertNull(value())
            assertEquals("$.inner1.innerArray[1].leaf.string", path().jsonPath)
        }
    }

    @Test
    fun testArrayOfNullablesAccess() {
        val root = Root::class.build {
            inner1 {
                arrayOfNullables = arrayOf(null)
            }
        }

        with(root.withPath().inner1.arrayOfNullables[0].leaf.string) {
            assertEquals(null, value())
            assertEquals("$.inner1.arrayOfNullables[0].leaf.string", path().jsonPath)
        }
    }

    @Test
    fun testNullableArrayAccess() {
        val root = Root::class.build {
            inner1 {
                nullableArray = arrayOf(Inner2())
            }
        }

        with(root.withPath().inner1.nullableArray[0].leaf.string) {
            assertEquals("", value())
            assertEquals("$.inner1.nullableArray[0].leaf.string", path().jsonPath)
        }
    }

    @Test
    fun testNullableArrayOfNullablesAccess() {
        val root = Root::class.build {
            inner1 {
                nullableArrayOfNullables = arrayOf(Inner2())
            }
        }

        with(root.withPath().inner1.nullableArrayOfNullables[0].leaf.string) {
            assertEquals("", value())
            assertEquals("$.inner1.nullableArrayOfNullables[0].leaf.string", path().jsonPath)
        }
    }
}

package io.github.virelion.buildata.integration.path

import io.github.virelion.buildata.integration.path.inner.Inner2
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ListPathCalculation {
    @Test
    fun testListAccess() {
        val root = Root::class.build {
            inner1 {
                innerList = arrayListOf(Inner2())
            }
        }

        with(root.withPath().inner1.innerList[0].leaf.string) {
            assertEquals("", value())
            assertEquals("$.inner1.innerList[0].leaf.string", path().jsonPath)
        }
    }

    @Test
    fun testListAccessOutOfBoundsElement() {
        val root = Root::class.build {
            inner1 {
                innerList = arrayListOf(Inner2())
            }
        }
        with(root.withPath().inner1.innerList[1].leaf.string) {
            assertNull(value())
            assertEquals("$.inner1.innerList[1].leaf.string", path().jsonPath)
        }
    }

    @Test
    fun testListOfNullablesAccess() {
        val root = Root::class.build {
            inner1 {
                listOfNullables = listOf(null)
            }
        }

        with(root.withPath().inner1.listOfNullables[0].leaf.string) {
            assertEquals(null, value())
            assertEquals("$.inner1.listOfNullables[0].leaf.string", path().jsonPath)
        }
    }

    @Test
    fun testNullableListAccess() {
        val root = Root::class.build {
            inner1 {
                nullableList = listOf(Inner2())
            }
        }

        with(root.withPath().inner1.nullableList[0].leaf.string) {
            assertEquals("", value())
            assertEquals("$.inner1.nullableList[0].leaf.string", path().jsonPath)
        }
    }

    @Test
    fun testNullableListOfNullablesAccess() {
        val root = Root::class.build {
            inner1 {
                nullableListOfNullables = listOf(Inner2())
            }
        }

        with(root.withPath().inner1.nullableListOfNullables[0].leaf.string) {
            assertEquals("", value())
            assertEquals("$.inner1.nullableListOfNullables[0].leaf.string", path().jsonPath)
        }
    }
}

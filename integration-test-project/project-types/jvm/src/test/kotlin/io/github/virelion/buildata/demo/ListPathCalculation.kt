package io.github.virelion.buildata.demo

import io.github.virelion.buildata.path.MissingElementException
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ListPathCalculation {
    @Test
    fun testListAccess() {
        val root = Root::class.build {
            inner1 {
                innerList = listOf(Inner2())
            }
        }

        with(root.withPath().inner1.innerList[0].leaf.string) {
            assertEquals("", value())
            assertEquals("$.inner1.innerList[0].leaf.string",  path().jsonPath)
        }
    }

    @Test
    fun testListAccessOutOfBoundsElement() {
        val root = Root::class.build {
            inner1 {
                innerList = listOf(Inner2())
            }
        }
        var failed = false
        try {
            root.withPath().inner1.innerList[1].leaf.string
        } catch (e: MissingElementException) {
            failed = true
            assertEquals("1", e.index)
            assertEquals("$.inner1.innerList", e.path.jsonPath)
            assertEquals("There is no item on index '1' of '$.inner1.innerList'", e.message)
        }
        assertTrue(failed)
    }

    @Test
    fun testListOfNullablesAccess() {
        val root = Root::class.build {
            inner1 {
                listOfNullables = listOf(null)
            }
        }

        with(root.withPath().inner1.listOfNullables[0].leaf.string) {
            assertEquals(null,  value())
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
            assertEquals("",  value())
            assertEquals("$.inner1.nullableList[0].leaf.string",  path().jsonPath)
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
            assertEquals("",  value())
            assertEquals("$.inner1.nullableListOfNullables[0].leaf.string", path().jsonPath)
        }
    }
}

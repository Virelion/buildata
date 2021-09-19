package io.github.virelion.buildata.demo

import io.github.virelion.buildata.path.valueWithPath
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PathCalculation {
    @Test
    fun testScalarsValuesWithPaths() {
        val root = Root::class.build {
            inner1 {
                inner2 {
                    leaf {
                        string = "Test"
                        boolean = true
                        int = 1
                        uInt = 2u
                        long = 3
                        uLong = 4uL
                        byte = 0x5
                        uByte = 0x6u
                        short = 7
                        uShort = 8u
                        float = 9.0f
                        double = 10.0
                    }
                }
            }
        }

        with(root.path { inner1.inner2.leaf }) {
            with(valueWithPath { string }) {
                assertEquals("Test", value)
                assertEquals("$.inner1.inner2.leaf.string", jsonPath)
            }
            with(valueWithPath { boolean }) {
                assertEquals(true, value)
                assertEquals("$.inner1.inner2.leaf.boolean", jsonPath)
            }
            with(valueWithPath { int }) {
                assertEquals(1, value)
                assertEquals("$.inner1.inner2.leaf.int", jsonPath)
            }
            with(valueWithPath { uInt }) {
                assertEquals(2u, value)
                assertEquals("$.inner1.inner2.leaf.uInt", jsonPath)
            }
            with(valueWithPath { long }) {
                assertEquals(3, value)
                assertEquals("$.inner1.inner2.leaf.long", jsonPath)
            }
            with(valueWithPath { uLong }) {
                assertEquals(4u, value)
                assertEquals("$.inner1.inner2.leaf.uLong", jsonPath)
            }
            with(valueWithPath { byte }) {
                assertEquals(0x5, value)
                assertEquals("$.inner1.inner2.leaf.byte", jsonPath)
            }
            with(valueWithPath { uByte }) {
                assertEquals(0x6u, value)
                assertEquals("$.inner1.inner2.leaf.uByte", jsonPath)
            }
            with(valueWithPath { short }) {
                assertEquals(7, value)
                assertEquals("$.inner1.inner2.leaf.short", jsonPath)
            }
            with(valueWithPath { uShort }) {
                assertEquals(8u, value)
                assertEquals("$.inner1.inner2.leaf.uShort", jsonPath)
            }
            with(valueWithPath { float }) {
                assertEquals(9.0f, value)
                assertEquals("$.inner1.inner2.leaf.float", jsonPath)
            }
            with(valueWithPath { double }) {
                assertEquals(10.0, value)
                assertEquals("$.inner1.inner2.leaf.double", jsonPath)
            }
        }
    }

    @Test
    fun testNullableScalarsValuesWithPaths() {
        val root = Root::class.build {
            inner1 {
                leafWithNullables {
                    string = "Test"
                    boolean = true
                    int = 1
                    uInt = 2u
                    long = 3
                    uLong = 4uL
                    byte = 0x5
                    uByte = 0x6u
                    short = 7
                    uShort = 8u
                    float = 9.0f
                    double = 10.0
                }
            }
        }

        with(root.path { inner1.leafWithNullables }) {
            with(valueWithPath { string }) {
                assertEquals("Test", value)
                assertEquals("$.inner1.leafWithNullables.string", jsonPath)
            }
            with(valueWithPath { boolean }) {
                assertEquals(true, value)
                assertEquals("$.inner1.leafWithNullables.boolean", jsonPath)
            }
            with(valueWithPath { int }) {
                assertEquals(1, value)
                assertEquals("$.inner1.leafWithNullables.int", jsonPath)
            }
            with(valueWithPath { uInt }) {
                assertEquals(2u, value)
                assertEquals("$.inner1.leafWithNullables.uInt", jsonPath)
            }
            with(valueWithPath { long }) {
                assertEquals(3, value)
                assertEquals("$.inner1.leafWithNullables.long", jsonPath)
            }
            with(valueWithPath { uLong }) {
                assertEquals(4u, value)
                assertEquals("$.inner1.leafWithNullables.uLong", jsonPath)
            }
            with(valueWithPath { byte }) {
                assertEquals(0x5, value)
                assertEquals("$.inner1.leafWithNullables.byte", jsonPath)
            }
            with(valueWithPath { uByte }) {
                assertEquals(0x6u, value)
                assertEquals("$.inner1.leafWithNullables.uByte", jsonPath)
            }
            with(valueWithPath { short }) {
                assertEquals(7, value)
                assertEquals("$.inner1.leafWithNullables.short", jsonPath)
            }
            with(valueWithPath { uShort }) {
                assertEquals(8u, value)
                assertEquals("$.inner1.leafWithNullables.uShort", jsonPath)
            }
            with(valueWithPath { float }) {
                assertEquals(9.0f, value)
                assertEquals("$.inner1.leafWithNullables.float", jsonPath)
            }
            with(valueWithPath { double }) {
                assertEquals(10.0, value)
                assertEquals("$.inner1.leafWithNullables.double", jsonPath)
            }
        }
    }

    @Test
    fun testScalarsFromNullNode() {
        val root = Root()

        with(root.path { nullableLeaf }) {
            with(valueWithPath { string }) {
                assertNull(value)
                assertEquals("$.nullableLeaf.string", jsonPath)
            }
            with(valueWithPath { boolean }) {
                assertNull(value)
                assertEquals("$.nullableLeaf.boolean", jsonPath)
            }
            with(valueWithPath { int }) {
                assertNull(value)
                assertEquals("$.nullableLeaf.int", jsonPath)
            }
            with(valueWithPath { uInt }) {
                assertNull(value)
                assertEquals("$.nullableLeaf.uInt", jsonPath)
            }
            with(valueWithPath { long }) {
                assertNull(value)
                assertEquals("$.nullableLeaf.long", jsonPath)
            }
            with(valueWithPath { uLong }) {
                assertNull(value)
                assertEquals("$.nullableLeaf.uLong", jsonPath)
            }
            with(valueWithPath { byte }) {
                assertNull(value)
                assertEquals("$.nullableLeaf.byte", jsonPath)
            }
            with(valueWithPath { uByte }) {
                assertNull(value)
                assertEquals("$.nullableLeaf.uByte", jsonPath)
            }
            with(valueWithPath { short }) {
                assertNull(value)
                assertEquals("$.nullableLeaf.short", jsonPath)
            }
            with(valueWithPath { uShort }) {
                assertNull(value)
                assertEquals("$.nullableLeaf.uShort", jsonPath)
            }
            with(valueWithPath { float }) {
                assertNull(value)
                assertEquals("$.nullableLeaf.float", jsonPath)
            }
            with(valueWithPath { double }) {
                assertNull(value)
                assertEquals("$.nullableLeaf.double", jsonPath)
            }
        }
    }

    @Test
    fun testRelativePath() {
        val leaf = LeafNode(string = "TestString")
        with(leaf.valueWithPath { string }) {
            assertEquals("TestString", value)
            assertEquals("$.string", jsonPath)
        }
    }
}

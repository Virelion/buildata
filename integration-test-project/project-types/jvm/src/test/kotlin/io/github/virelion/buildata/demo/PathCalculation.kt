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
                assertEquals("$.inner1.inner2.leaf.string", xpath)
            }
            with(valueWithPath { boolean }) {
                assertEquals( true, value)
                assertEquals("$.inner1.inner2.leaf.boolean", xpath)
            }
            with(valueWithPath { int }) {
                assertEquals( 1, value)
                assertEquals("$.inner1.inner2.leaf.int", xpath)
            }
            with(valueWithPath { uInt }) {
                assertEquals( 2u, value)
                assertEquals("$.inner1.inner2.leaf.uInt", xpath)
            }
            with(valueWithPath { long }) {
                assertEquals( 3, value)
                assertEquals("$.inner1.inner2.leaf.long", xpath)
            }
            with(valueWithPath { uLong }) {
                assertEquals( 4u, value)
                assertEquals("$.inner1.inner2.leaf.uLong", xpath)
            }
            with(valueWithPath { byte }) {
                assertEquals( 0x5, value)
                assertEquals("$.inner1.inner2.leaf.byte", xpath)
            }
            with(valueWithPath { uByte }) {
                assertEquals( 0x6u, value)
                assertEquals("$.inner1.inner2.leaf.uByte", xpath)
            }
            with(valueWithPath { short }) {
                assertEquals( 7, value)
                assertEquals("$.inner1.inner2.leaf.short", xpath)
            }
            with(valueWithPath { uShort }) {
                assertEquals( 8u, value)
                assertEquals("$.inner1.inner2.leaf.uShort", xpath)
            }
            with(valueWithPath { float }) {
                assertEquals( 9.0f, value)
                assertEquals("$.inner1.inner2.leaf.float", xpath)
            }
            with(valueWithPath { double }) {
                assertEquals( 10.0, value)
                assertEquals("$.inner1.inner2.leaf.double", xpath)
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
                assertEquals("$.inner1.leafWithNullables.string", xpath)
            }
            with(valueWithPath { boolean }) {
                assertEquals( true, value)
                assertEquals("$.inner1.leafWithNullables.boolean", xpath)
            }
            with(valueWithPath { int }) {
                assertEquals( 1, value)
                assertEquals("$.inner1.leafWithNullables.int", xpath)
            }
            with(valueWithPath { uInt }) {
                assertEquals( 2u, value)
                assertEquals("$.inner1.leafWithNullables.uInt", xpath)
            }
            with(valueWithPath { long }) {
                assertEquals( 3, value)
                assertEquals("$.inner1.leafWithNullables.long", xpath)
            }
            with(valueWithPath { uLong }) {
                assertEquals( 4u, value)
                assertEquals("$.inner1.leafWithNullables.uLong", xpath)
            }
            with(valueWithPath { byte }) {
                assertEquals( 0x5, value)
                assertEquals("$.inner1.leafWithNullables.byte", xpath)
            }
            with(valueWithPath { uByte }) {
                assertEquals( 0x6u, value)
                assertEquals("$.inner1.leafWithNullables.uByte", xpath)
            }
            with(valueWithPath { short }) {
                assertEquals( 7, value)
                assertEquals("$.inner1.leafWithNullables.short", xpath)
            }
            with(valueWithPath { uShort }) {
                assertEquals( 8u, value)
                assertEquals("$.inner1.leafWithNullables.uShort", xpath)
            }
            with(valueWithPath { float }) {
                assertEquals( 9.0f, value)
                assertEquals("$.inner1.leafWithNullables.float", xpath)
            }
            with(valueWithPath { double }) {
                assertEquals( 10.0, value)
                assertEquals("$.inner1.leafWithNullables.double", xpath)
            }
        }
    }

    @Test
    fun testScalarsFromNullNode() {
        val root = Root()

        with(root.path { nullableLeaf }) {
            with(valueWithPath { string }) {
                assertNull(value)
                assertEquals("$.nullableLeaf.string", xpath)
            }
            with(valueWithPath { boolean }) {
                assertNull(value)
                assertEquals("$.nullableLeaf.boolean", xpath)
            }
            with(valueWithPath { int }) {
                assertNull(value)
                assertEquals("$.nullableLeaf.int", xpath)
            }
            with(valueWithPath { uInt }) {
                assertNull(value)
                assertEquals("$.nullableLeaf.uInt", xpath)
            }
            with(valueWithPath { long }) {
                assertNull(value)
                assertEquals("$.nullableLeaf.long", xpath)
            }
            with(valueWithPath { uLong }) {
                assertNull(value)
                assertEquals("$.nullableLeaf.uLong", xpath)
            }
            with(valueWithPath { byte }) {
                assertNull(value)
                assertEquals("$.nullableLeaf.byte", xpath)
            }
            with(valueWithPath { uByte }) {
                assertNull(value)
                assertEquals("$.nullableLeaf.uByte", xpath)
            }
            with(valueWithPath { short }) {
                assertNull(value)
                assertEquals("$.nullableLeaf.short", xpath)
            }
            with(valueWithPath { uShort }) {
                assertNull(value)
                assertEquals("$.nullableLeaf.uShort", xpath)
            }
            with(valueWithPath { float }) {
                assertNull(value)
                assertEquals("$.nullableLeaf.float", xpath)
            }
            with(valueWithPath { double }) {
                assertNull(value)
                assertEquals("$.nullableLeaf.double", xpath)
            }
        }
    }

    @Test
    fun testRelativePath() {
        val leaf = LeafNode(string = "TestString")
        with(leaf.valueWithPath { string }) {
            assertEquals( "TestString", value)
            assertEquals("$.string", xpath)
        }
    }
}

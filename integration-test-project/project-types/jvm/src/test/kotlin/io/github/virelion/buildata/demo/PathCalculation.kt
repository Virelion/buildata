package io.github.virelion.buildata.demo

import kotlin.test.Test
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

        with(root.withPath().inner1.inner2.leaf) {
            with(string) {
                assertEquals("Test", value())
                assertEquals("$.inner1.inner2.leaf.string", path().jsonPath)
            }
            with(boolean) {
                assertEquals(true, value())
                assertEquals("$.inner1.inner2.leaf.boolean", path().jsonPath)
            }
            with(int) {
                assertEquals(1, value())
                assertEquals("$.inner1.inner2.leaf.int", path().jsonPath)
            }
            with(uInt) {
                assertEquals(2u, value())
                assertEquals("$.inner1.inner2.leaf.uInt", path().jsonPath)
            }
            with(long) {
                assertEquals(3, value())
                assertEquals("$.inner1.inner2.leaf.long", path().jsonPath)
            }
            with(uLong) {
                assertEquals(4u, value())
                assertEquals("$.inner1.inner2.leaf.uLong", path().jsonPath)
            }
            with(byte) {
                assertEquals(0x5, value())
                assertEquals("$.inner1.inner2.leaf.byte", path().jsonPath)
            }
            with(uByte) {
                assertEquals(0x6u, value())
                assertEquals("$.inner1.inner2.leaf.uByte", path().jsonPath)
            }
            with(short) {
                assertEquals(7, value())
                assertEquals("$.inner1.inner2.leaf.short", path().jsonPath)
            }
            with(uShort) {
                assertEquals(8u, value())
                assertEquals("$.inner1.inner2.leaf.uShort", path().jsonPath)
            }
            with(float) {
                assertEquals(9.0f, value())
                assertEquals("$.inner1.inner2.leaf.float", path().jsonPath)
            }
            with(double) {
                assertEquals(10.0, value())
                assertEquals("$.inner1.inner2.leaf.double", path().jsonPath)
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

        with(root.withPath().inner1.leafWithNullables) {
            with(string) {
                assertEquals("Test", value())
                assertEquals("$.inner1.leafWithNullables.string", path().jsonPath)
            }
            with(boolean) {
                assertEquals(true, value())
                assertEquals("$.inner1.leafWithNullables.boolean", path().jsonPath)
            }
            with(int) {
                assertEquals(1, value())
                assertEquals("$.inner1.leafWithNullables.int", path().jsonPath)
            }
            with(uInt) {
                assertEquals(2u, value())
                assertEquals("$.inner1.leafWithNullables.uInt", path().jsonPath)
            }
            with(long) {
                assertEquals(3, value())
                assertEquals("$.inner1.leafWithNullables.long", path().jsonPath)
            }
            with(uLong) {
                assertEquals(4u, value())
                assertEquals("$.inner1.leafWithNullables.uLong", path().jsonPath)
            }
            with(byte) {
                assertEquals(0x5, value())
                assertEquals("$.inner1.leafWithNullables.byte", path().jsonPath)
            }
            with(uByte) {
                assertEquals(0x6u, value())
                assertEquals("$.inner1.leafWithNullables.uByte", path().jsonPath)
            }
            with(short) {
                assertEquals(7, value())
                assertEquals("$.inner1.leafWithNullables.short", path().jsonPath)
            }
            with(uShort) {
                assertEquals(8u, value())
                assertEquals("$.inner1.leafWithNullables.uShort", path().jsonPath)
            }
            with(float) {
                assertEquals(9.0f, value())
                assertEquals("$.inner1.leafWithNullables.float", path().jsonPath)
            }
            with(double) {
                assertEquals(10.0, value())
                assertEquals("$.inner1.leafWithNullables.double", path().jsonPath)
            }
        }
    }

    @Test
    fun testScalarsFromNullNode() {
        val root = Root()

        with(root.withPath().nullableLeaf) {
            with(string) {
                assertNull(value())
                assertEquals("$.nullableLeaf.string", path().jsonPath)
            }
            with(boolean) {
                assertNull(value())
                assertEquals("$.nullableLeaf.boolean", path().jsonPath)
            }
            with(int) {
                assertNull(value())
                assertEquals("$.nullableLeaf.int", path().jsonPath)
            }
            with(uInt) {
                assertNull(value())
                assertEquals("$.nullableLeaf.uInt", path().jsonPath)
            }
            with(long) {
                assertNull(value())
                assertEquals("$.nullableLeaf.long", path().jsonPath)
            }
            with(uLong) {
                assertNull(value())
                assertEquals("$.nullableLeaf.uLong", path().jsonPath)
            }
            with(byte) {
                assertNull(value())
                assertEquals("$.nullableLeaf.byte", path().jsonPath)
            }
            with(uByte) {
                assertNull(value())
                assertEquals("$.nullableLeaf.uByte", path().jsonPath)
            }
            with(short) {
                assertNull(value())
                assertEquals("$.nullableLeaf.short", path().jsonPath)
            }
            with(uShort) {
                assertNull(value())
                assertEquals("$.nullableLeaf.uShort", path().jsonPath)
            }
            with(float) {
                assertNull(value())
                assertEquals("$.nullableLeaf.float", path().jsonPath)
            }
            with(double) {
                assertNull(value())
                assertEquals("$.nullableLeaf.double", path().jsonPath)
            }
        }
    }

    @Test
    fun testPathCalculationOnEmptyNode() {
        with(Root::class.path().inner1.innerList[42].leaf.boolean) {
            assertNull(value())
            assertEquals("$.inner1.innerList[42].leaf.boolean", path().jsonPath)
        }
    }

    @Test
    fun testCustomNamesAnnotated() {
        with(AnnotatedLeaf::class.path()) {
            assertEquals("$.PATH_ELEMENT_NAME", pathElementNameAnnotated.path().jsonPath)
            assertEquals("$.JACKSON_JSON_ALIAS", jacksonAnnotatedWithAlias.path().jsonPath)
            assertEquals("$.KOTLINX_SERIALIZATION_SERIAL_NAME", kotlinxSerializationAnnotated.path().jsonPath)
        }
    }
}

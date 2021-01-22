package com.github.virelion.buildata.demo

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class CompositeBuilderTest {
    @Test
    fun canBuildWholeDataTree() {
        val item = CompositeDataClass::class.build {
            innerClass {
                value = "INNER_CLASS"
                level2 {
                    value = "LEVEL2"
                    level3 {
                        value = "LEVEL3"
                    }
                }
            }
        }

        with(item) {
            assertNotNull(innerClass).apply {
                assertEquals("INNER_CLASS", value)
                with(level2) {
                    assertEquals("LEVEL2", value)
                    with(level3) {
                        assertEquals("LEVEL3", value)
                    }
                }
            }
        }
    }

    @Test
    fun canAssignToNonBuildableDeepInsideTree() {
        val itemBuilder = CompositeDataClass::class.builder().apply {
            innerClass {
                value = "INNER_CLASS"
                level2 {
                    value = "LEVEL2"
                    level3 {
                        value = "LEVEL3"
                    }
                }
            }
        }

        itemBuilder {
            innerClass {
                level2 {
                    level3 {
                        value = "CHANGED"
                    }
                }
            }
        }

        with(itemBuilder.build()) {
            assertNotNull(innerClass).apply {
                assertEquals("INNER_CLASS", value)
                with(level2) {
                    assertEquals("LEVEL2", value)
                    with(level3) {
                        assertEquals("CHANGED", value)
                    }
                }
            }
        }
    }

    @Test
    fun canDirectlyAssignToBuildableProp() {
        val item = CompositeDataClass::class.build {
            innerClass {
                value = "INNER_CLASS"
                level2 {
                    value = "LEVEL2"
                    level3 = Level3Class("LEVEL3")
                }
            }
        }

        with(item) {
            assertNotNull(innerClass).apply {
                assertEquals("INNER_CLASS", value)
                with(level2) {
                    assertEquals("LEVEL2", value)
                    with(level3) {
                        assertEquals("LEVEL3", value)
                    }
                }
            }
        }
    }

    @Test
    fun canSetNullOnNullableProperty() {
        val item = CompositeDataClass::class.build {
            innerClass = null
        }

        with(item) {
            assertNull(innerClass)
        }
    }
}

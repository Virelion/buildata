/*
 * Copyright 2021 Maciej Ziemba
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.virelion.buildata.integration.builder

import io.github.virelion.buildata.integration.builder.inner.Level2Class
import io.github.virelion.buildata.integration.builder.inner.Level3Class
import io.github.virelion.buildata.integration.builder.inner.build
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

    @Test
    fun properDefaultsAreSet() {
        val item = Level2Class::class.build {
            level3 = Level3Class("")
            value = ""
        }

        with(item) {
            assertNull(nullableLevel3WithNullDefault)
            assertEquals(Level3Class("DEFAULT"), nullableLevel3WithDefault)
            assertEquals(Level3Class("DEFAULT"), level3WithDefault)
        }
    }
}

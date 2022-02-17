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

import io.github.virelion.buildata.UninitializedPropertyException
import io.github.virelion.buildata.integration.access.NonCompositeDataClass
import io.github.virelion.buildata.integration.access.build
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class NonCompositeBuilderTest {
    @Test
    fun nonDefaultRequired() {
        val item = NonCompositeDataClass::class.build {
            noDefaultRequired = "TestValue"
            noDefaultOptional = null
        }
        with(item) {
            assertEquals("TestValue", noDefaultRequired)
            assertNull(noDefaultOptional)
            assertEquals("defaultRequired", requiredWithDefault)
            assertEquals("defaultOptional", optionalWithDefault)
        }
    }

    @Test
    fun nonDefaultRequired_missing() {
        try {
            NonCompositeDataClass::class.build {
                noDefaultOptional = null
            }
        } catch (e: UninitializedPropertyException) {
            assertEquals(e.message, "Property noDefaultRequired was not set during build process.")
        }
    }

    @Test
    fun nonDefaultoptional() {
        val item = NonCompositeDataClass::class.build {
            noDefaultRequired = "TestValue"
            noDefaultOptional = "TestValue"
        }
        with(item) {
            assertEquals("TestValue", noDefaultRequired)
            assertEquals("TestValue", noDefaultOptional)
        }
    }

    @Test
    fun nonDefaultoptional_missing() {
        try {
            NonCompositeDataClass::class.build {
                noDefaultRequired = "TestValue"
            }
        } catch (e: UninitializedPropertyException) {
            assertEquals(e.message, "Property noDefaultOptional was not set during build process.")
        }
    }

    @Test
    fun defaultRequired() {
        val item = NonCompositeDataClass::class.build {
            noDefaultRequired = "TestValue"
            noDefaultOptional = null
            requiredWithDefault = "req"
        }
        with(item) {
            assertEquals("req", requiredWithDefault)
        }
    }

    @Test
    fun defaultOptional() {
        val item = NonCompositeDataClass::class.build {
            noDefaultRequired = "TestValue"
            noDefaultOptional = null
            optionalWithDefault = "opt"
        }
        with(item) {
            assertEquals("opt", optionalWithDefault)
        }
    }

    @Test
    fun defaultOptional_setNull() {
        val item = NonCompositeDataClass::class.build {
            noDefaultRequired = "TestValue"
            noDefaultOptional = null
            optionalWithDefault = null
        }
        with(item) {
            assertNull(optionalWithDefault)
        }
    }
}

package io.github.virelion.buildata.demo

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
        } catch (e: IllegalArgumentException) {
            assertEquals(e.message, "Property noDefaultRequired was not initialized")
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
        } catch (e: IllegalArgumentException) {
            assertEquals(e.message, "Property noDefaultOptional was not initialized")
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

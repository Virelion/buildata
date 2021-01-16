package com.github.virelion.buildata

import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class NonCompositeBuilderTest {
    @Test
    @JsName("Non_default_required")
    fun `Non default required`() {
        val item = NonCompositeDataClass::class.build {
            noDefaultRequired = "TestValue"
            noDefaultOptional = null
        }
        with(item) {
            assertEquals("TestValue", noDefaultRequired)
            assertNull(noDefaultOptional)
            assertEquals("default", requiredWithDefault)
            assertEquals("default", opionalWithDefault)
        }
    }

    @Test
    @JsName("Non_default_required_missing")
    fun `Non default required - missing`() {
        try {
            NonCompositeDataClass::class.build {
                noDefaultOptional = null
            }
        } catch (e: IllegalArgumentException) {
            assertEquals(e.message,"Property noDefaultRequired was not initialized")
        }
    }

    @Test
    @JsName("Non_default_optional")
    fun `Non default optional`() {
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
    @JsName("Non_default_optional_missing")
    fun `Non default optional - missing`() {
        try {
            NonCompositeDataClass::class.build {
                noDefaultRequired = "TestValue"
            }
        } catch (e: IllegalArgumentException) {
            assertEquals(e.message,"Property noDefaultOptional was not initialized")
        }
    }

    @Test
    @JsName("default_required")
    fun `Default required`() {
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
    @JsName("default_optional")
    fun `Default optional`() {
        val item = NonCompositeDataClass::class.build {
            noDefaultRequired = "TestValue"
            noDefaultOptional = null
            opionalWithDefault = "opt"
        }
        with(item) {
            assertEquals("opt", opionalWithDefault)
        }
    }

    @Test
    @JsName("default_optional_set_null")
    fun `Default optional - set null`() {
        val item = NonCompositeDataClass::class.build {
            noDefaultRequired = "TestValue"
            noDefaultOptional = null
            opionalWithDefault = null
        }
        with(item) {
            assertNull(opionalWithDefault)
        }
    }
}
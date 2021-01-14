package com.github.virelion.buildata

import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class NonCompositeBuilderTest {
    @Test
    @JsName("Non_default_required")
    fun `Non default required`() {
        val item = NonCompositeDataClass::class.build {
            noDefaultRequired = "TestValue"
        }
        with(item) {
            assertEquals("TestValue", noDefaultRequired)
        }
    }
}
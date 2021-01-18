package com.github.virelion.buildata.demo

import kotlin.test.Test
import kotlin.test.assertEquals

class IntegrationTests {

    @Test
    fun canBuildNonCompositeDataClass() {
        val nonCompositeDataClass = NonCompositeDataClass::class.build {
            noDefaultRequired = "noDefaultRequired"
            noDefaultOptional = "noDefaultOptional"
            requiredWithDefault = "requiredWithDefault"
            optionalWithDefault = "optionalWithDefault"
        }

        with(nonCompositeDataClass) {
            assertEquals(noDefaultRequired, "noDefaultRequired")
            assertEquals(noDefaultOptional, "noDefaultOptional")
            assertEquals(requiredWithDefault, "requiredWithDefault")
            assertEquals(optionalWithDefault, "optionalWithDefault")
        }
    }
}

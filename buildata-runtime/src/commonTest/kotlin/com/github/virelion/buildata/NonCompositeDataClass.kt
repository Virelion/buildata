package com.github.virelion.buildata

import kotlin.reflect.KClass

// definition
data class NonCompositeDataClass(
        val noDefaultRequired: String,
        val noDefaultOptional: String?,
        val requiredWithDefault: String,
        val optionalWithDefault: String?
)

// generated
fun KClass<NonCompositeDataClass>.builder(): NonCompositeDataClass_Builder {
    return NonCompositeDataClass_Builder()
}

fun KClass<NonCompositeDataClass>.build(
        builder: NonCompositeDataClass_Builder.() -> Unit
): NonCompositeDataClass {
    return NonCompositeDataClass_Builder().apply { builder() }.build()
}

class NonCompositeDataClass_Builder: Builder<NonCompositeDataClass> {
    var noDefaultRequired: String by BuilderElementProperty(null, false)
    var noDefaultOptional: String? by BuilderElementProperty(null, true)
    var requiredWithDefault: String by BuilderElementProperty({ "default" }, false)
    var optionalWithDefault: String? by BuilderElementProperty({ "default" }, true)

    override fun build(): NonCompositeDataClass {
        return NonCompositeDataClass(
                noDefaultRequired = noDefaultRequired,
                noDefaultOptional = noDefaultOptional,
                requiredWithDefault = requiredWithDefault,
                optionalWithDefault = optionalWithDefault,
        )
    }
}
package com.github.virelion.buildata.demo

import com.github.virelion.buildata.Buildable

@Buildable
data class NonCompositeDataClass(
    val noDefaultRequired: String,
    val noDefaultOptional: String?,
    val requiredWithDefault: String,
    val optionalWithDefault: String?
)

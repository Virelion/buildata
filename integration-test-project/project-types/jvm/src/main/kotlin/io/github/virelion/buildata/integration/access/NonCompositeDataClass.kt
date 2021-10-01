package io.github.virelion.buildata.integration.access

import io.github.virelion.buildata.Buildable

@Buildable
data class NonCompositeDataClass(
    val noDefaultRequired: String,
    val noDefaultOptional: String?,
    val requiredWithDefault: String = "defaultRequired",
    val optionalWithDefault: String? = "defaultOptional"
)

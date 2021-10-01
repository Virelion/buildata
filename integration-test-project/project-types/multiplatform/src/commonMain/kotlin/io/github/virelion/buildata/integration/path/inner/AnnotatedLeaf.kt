package io.github.virelion.buildata.integration.path.inner

import io.github.virelion.buildata.path.PathElementName
import io.github.virelion.buildata.path.PathReflection
import kotlinx.serialization.SerialName

@PathReflection
data class AnnotatedLeaf(
    @PathElementName("PATH_ELEMENT_NAME")
    val pathElementNameAnnotated: String,

    @SerialName("KOTLINX_SERIALIZATION_SERIAL_NAME")
    val kotlinxSerializationAnnotated: String
)
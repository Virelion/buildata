package io.github.virelion.buildata.integration.path.inner

import com.fasterxml.jackson.annotation.JsonAlias
import io.github.virelion.buildata.path.PathElementName
import io.github.virelion.buildata.path.PathReflection
import kotlinx.serialization.SerialName

@PathReflection
data class AnnotatedLeaf(
    @PathElementName("PATH_ELEMENT_NAME")
    val pathElementNameAnnotated: String,

    @JsonAlias("JACKSON_JSON_ALIAS")
    val jacksonAnnotatedWithAlias: String,

    @SerialName("KOTLINX_SERIALIZATION_SERIAL_NAME")
    val kotlinxSerializationAnnotated: String
)
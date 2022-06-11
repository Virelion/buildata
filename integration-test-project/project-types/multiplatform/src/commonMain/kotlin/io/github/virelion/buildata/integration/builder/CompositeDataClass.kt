package io.github.virelion.buildata.integration.builder

import io.github.virelion.buildata.Buildable
import io.github.virelion.buildata.integration.builder.inner.Level1Class

@Buildable
data class CompositeDataClass(
    val innerClass: Level1Class?
)

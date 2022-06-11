package io.github.virelion.buildata.integration.builder.inner

import io.github.virelion.buildata.Buildable

@Buildable
data class Level1Class(
    val level2: Level2Class,
    val value: String,
)

package io.github.virelion.buildata.integration.builder.inner

import io.github.virelion.buildata.Buildable

@Buildable
data class Level2Class(
    val level3: Level3Class,
    val level3WithDefault: Level3Class = Level3Class("DEFAULT"),
    val nullableLevel3WithDefault: Level3Class? = Level3Class("DEFAULT"),
    val nullableLevel3WithNullDefault: Level3Class? = null,
    val value: String
)

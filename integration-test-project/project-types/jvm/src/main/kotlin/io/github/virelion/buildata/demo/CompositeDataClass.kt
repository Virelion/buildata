package io.github.virelion.buildata.demo

import io.github.virelion.buildata.Buildable

@Buildable
data class CompositeDataClass(
    val innerClass: @Buildable Level1Class?
)

@Buildable
data class Level1Class(
    val level2: @Buildable Level2Class,
    val value: String,
)

@Buildable
data class Level2Class(
    val level3: @Buildable Level3Class,
    val level3WithDefault: @Buildable Level3Class = Level3Class("DEFAULT"),
    val nullableLevel3WithDefault: @Buildable Level3Class? = Level3Class("DEFAULT"),
    val nullableLevel3WithNullDefault: @Buildable Level3Class? = null,
    val value: String
)

@Buildable
data class Level3Class(
    val value: String
)

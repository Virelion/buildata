package com.github.virelion.buildata.demo

import com.github.virelion.buildata.Buildable

@Buildable
data class CompositeDataClass(
    val innerClass: @Buildable Level1Class
)

@Buildable
data class Level1Class(
    val level2: @Buildable Level2Class,
    val value: String,
)

@Buildable
data class Level2Class(
    val level3: @Buildable Level3Class,
    val value: String
)

@Buildable
data class Level3Class(
    val value: String
)

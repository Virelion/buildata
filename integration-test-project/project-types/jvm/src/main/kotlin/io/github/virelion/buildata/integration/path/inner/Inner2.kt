package io.github.virelion.buildata.integration.path.inner

import io.github.virelion.buildata.Buildable
import io.github.virelion.buildata.path.PathReflection

@Buildable
@PathReflection
data class Inner2(
    val leaf: LeafNode = LeafNode()
)

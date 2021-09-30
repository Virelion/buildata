package io.github.virelion.buildata.integration.path

import io.github.virelion.buildata.Buildable
import io.github.virelion.buildata.integration.path.inner.Inner1
import io.github.virelion.buildata.integration.path.inner.LeafNode
import io.github.virelion.buildata.path.PathReflection

@Buildable
@PathReflection
data class Root(
    val inner1: Inner1 = Inner1(),
    val nullableLeaf: LeafNode? = null
)

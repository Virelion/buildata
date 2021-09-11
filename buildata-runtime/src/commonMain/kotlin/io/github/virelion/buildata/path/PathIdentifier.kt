package io.github.virelion.buildata.path

sealed class PathIdentifier
class IntIndexPathIdentifier(val index: Int) : PathIdentifier()
class StringIndexPathIdentifier(val index: String) : PathIdentifier()
class StringNamePathIdentifier(val name: String) : PathIdentifier()
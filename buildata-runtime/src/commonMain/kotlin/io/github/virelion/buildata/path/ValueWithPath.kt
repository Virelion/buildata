package io.github.virelion.buildata.path

data class ValueWithPath<T>(val path: List<PathIdentifier>, val value: T) {
    val jsonPath : String by lazy { path.jsonPath }
}
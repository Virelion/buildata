package io.github.virelion.buildata.path

interface PathReflectionPropertyWrapper<out T> {
    fun value(): T
    fun path(): RecordedPath
}

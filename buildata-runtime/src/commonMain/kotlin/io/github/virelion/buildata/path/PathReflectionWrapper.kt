package io.github.virelion.buildata.path

interface PathReflectionWrapper<out T> {
    fun value(): T
    fun path(): RecordedPath
}

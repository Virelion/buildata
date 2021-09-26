package io.github.virelion.buildata.path

/**
 * Value paired with [RecordedPath]
 */
interface PathReflectionWrapper<out T> {
    fun value(): T
    fun path(): RecordedPath
}

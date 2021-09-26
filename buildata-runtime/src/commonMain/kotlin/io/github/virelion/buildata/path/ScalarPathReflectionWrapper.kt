package io.github.virelion.buildata.path

/**
 * [PathReflectionWrapper] implementation for nullable and non-nullable scalars.
 */
data class ScalarPathReflectionWrapper<T>(
    val __value: T,
    val __path: RecordedPath
) : PathReflectionWrapper<T> {
    override fun value() = __value
    override fun path() = __path
}
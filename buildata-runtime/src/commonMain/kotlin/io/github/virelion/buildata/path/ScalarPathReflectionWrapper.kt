package io.github.virelion.buildata.path

data class ScalarPathReflectionWrapper<T>(
    val __value: T,
    val __path: RecordedPath
) : PathReflectionWrapper<T> {
    override fun value() = __value
    override fun path() = __path
}
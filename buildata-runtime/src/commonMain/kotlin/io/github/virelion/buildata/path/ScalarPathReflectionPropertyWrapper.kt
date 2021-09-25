package io.github.virelion.buildata.path

data class ScalarPathReflectionPropertyWrapper<T>(
    val __value: T,
    val __path: RecordedPath
) : PathReflectionPropertyWrapper<T> {
    override fun value() = __value
    override fun path() = __path
}
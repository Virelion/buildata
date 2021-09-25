package io.github.virelion.buildata.path

class ListPathReflectionRecorder<T, Wrapped : PathReflectionPropertyWrapper<T>>(
    private val __item: List<T>,
    private val __path: RecordedPath,
    private val wrapMethod: (RecordedPath, Int, T?) -> Wrapped
) : PathReflectionPropertyWrapper<List<T>> {
    operator fun get(index: Int): Wrapped {
        if (index >= __item.size) {
            throw MissingElementException(index.toString(), __path)
        }
        return wrapMethod(__path, index, __item[index])
    }

    fun toList(): List<Wrapped> {
        return __item.mapIndexed { index, it ->
            wrapMethod(__path, index, it)
        }
    }

    override fun value() = __item
    override fun path() = __path
}
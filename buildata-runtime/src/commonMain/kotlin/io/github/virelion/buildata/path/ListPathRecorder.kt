package io.github.virelion.buildata.path

class ListPathRecorder<T, Wrapped : PathPropertyWrapper<T>>(
    override val item: List<T>,
    override val pathRecorder: PathRecorder,
    private val wrapMethod: (PathRecorder, T?) -> Wrapped
) : PathPropertyWrapper<List<T>> {
    operator fun get(index: Int): Wrapped {
        if (index >= item.size) {
            throw MissingElementException(index.toString(), pathRecorder.identifiers)
        }
        pathRecorder.push(IntIndexPathIdentifier(index))
        return wrapMethod(pathRecorder, item[index])
    }

    override fun clone(): PathPropertyWrapper<List<T>> {
        return ListPathRecorder(item, pathRecorder, wrapMethod)
    }

    fun toList(): List<Wrapped> {
        return item.mapIndexed { index, it ->
            wrapMethod(
                pathRecorder.clone().apply {
                    push(IntIndexPathIdentifier(index))
                },
                it
            )
        }
    }
}
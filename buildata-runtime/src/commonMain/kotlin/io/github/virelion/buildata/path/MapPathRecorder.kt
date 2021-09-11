package io.github.virelion.buildata.path

class MapPathRecorder<T, Wrapped : PathPropertyWrapper<T>>(
    override val item: Map<String, T>,
    override val pathRecorder: PathRecorder,
    private val wrapMethod: (PathRecorder, T?) -> Wrapped
) : PathPropertyWrapper<Map<String, T>> {
    operator fun get(key: String): Wrapped {
        pathRecorder.push(StringIndexPathIdentifier(key))
        return item[key]?.let{ wrapMethod(pathRecorder, it) } ?: throw NoSuchElementException(key)
    }

    override fun clone(): PathPropertyWrapper<Map<String, T>> {
        return MapPathRecorder(item, pathRecorder.clone(), wrapMethod)
    }

    fun toMap(): Map<String, Wrapped> {
        return item.mapValues { (key, value) ->
            wrapMethod(
                pathRecorder.clone().apply {
                    push(StringIndexPathIdentifier(key))
                },
                value
            )
        }
    }
}
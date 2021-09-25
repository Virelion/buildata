package io.github.virelion.buildata.path

class MapPathReflectionRecorder<T, Wrapped : PathReflectionPropertyWrapper<T>>(
    private val __value: Map<String, T>,
    private val __path: RecordedPath,
    private val wrapMethod: (RecordedPath, String, T?) -> Wrapped
) : PathReflectionPropertyWrapper<Map<String, T>> {
    operator fun get(key: String): Wrapped {
        if(key !in __value) throw MissingElementException(key, __path)
        return wrapMethod(__path, key, __value[key])
    }

    fun toMap(): Map<String, Wrapped> {
        return __value.mapValues { (key, value) ->
            wrapMethod(__path, key, value)
        }
    }

    override fun value() = __value
    override fun path() = __path
}
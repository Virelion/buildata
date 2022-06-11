package io.github.virelion.buildata.path

/**
 * Map implementation for seamless path wrapped elements access
 *
 * Accessing element that is not in original map will return nullable wrapper.
 */
class PathReflectionMap<Key, Type, Wrapper : PathReflectionWrapper<Type?>> internal constructor(
    private val delegate: Map<Key, Wrapper>,
    private val nullWrapperProvider: (Key) -> Wrapper,
    private val keyTransformation: (Key) -> PathIdentifier
) : Map<Key, Wrapper> by delegate {

    constructor(
        originalMap: Map<Key, Type>,
        pathToList: RecordedPath,
        wrapperProvider: (Type?, RecordedPath) -> Wrapper,
        keyTransformation: (Key) -> PathIdentifier
    ) : this(
        delegate = originalMap.mapValues { (key, value) ->
            wrapperProvider(
                value,
                pathToList + keyTransformation(key)
            )
        },
        nullWrapperProvider = { key -> wrapperProvider(null, pathToList + keyTransformation(key)) },
        keyTransformation
    )

    override fun get(key: Key): Wrapper {
        if (key !in delegate) {
            return nullWrapperProvider(key)
        }

        return delegate[key] ?: nullWrapperProvider(key)
    }
}

package io.github.virelion.buildata.path

class PathReflectionMap<Type, Wrapper : PathReflectionWrapper<Type?>> internal constructor(
    private val delegate: Map<String, Wrapper>,
    private val nullWrapperProvider: (String) -> Wrapper
) : Map<String, Wrapper> by delegate {

    constructor(
        originalMap: Map<String, Type>,
        pathToList: RecordedPath,
        wrapperProvider: (Type?, RecordedPath) -> Wrapper
    ) : this(
        delegate = originalMap.mapValues { (key, value) ->
            wrapperProvider(
                value,
                pathToList + StringIndexPathIdentifier(key)
            )
        },
        nullWrapperProvider = { key -> wrapperProvider(null, pathToList + StringIndexPathIdentifier(key)) }
    )


    override fun get(key: String): Wrapper {
        if (key !in delegate) {
            return nullWrapperProvider(key)
        }

        return delegate[key] ?: nullWrapperProvider(key)
    }
}
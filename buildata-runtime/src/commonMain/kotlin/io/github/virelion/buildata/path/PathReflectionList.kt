package io.github.virelion.buildata.path

/**
 * List implementation for seamless path wrapped elements access
 *
 * Accessing element that is out of bound will return nullable wrapper.
 */
class PathReflectionList<Type, Wrapper : PathReflectionWrapper<Type?>> internal constructor(
    private val delegate: List<Wrapper>,
    private val nullWrapperProvider: (Int) -> Wrapper
) : List<Wrapper> by delegate {
    constructor(
        originalList: List<Type>,
        pathToList: RecordedPath,
        wrapperProvider: (Type?, RecordedPath) -> Wrapper
    ) : this(
        delegate = originalList.mapIndexed { index, item ->
            wrapperProvider(
                item,
                pathToList + IntIndexPathIdentifier(index)
            )
        },
        nullWrapperProvider = { index -> wrapperProvider(null, pathToList + IntIndexPathIdentifier(index)) }
    )

    override fun get(index: Int): Wrapper {
        if (index >= size) {
            return nullWrapperProvider(index)
        }

        return delegate[index]
    }
}

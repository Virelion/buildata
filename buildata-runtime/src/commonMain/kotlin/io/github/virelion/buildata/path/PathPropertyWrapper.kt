package io.github.virelion.buildata.path

interface PathPropertyWrapper<out T> {
    val item: T
    val pathRecorder: PathRecorder

    val path: List<PathIdentifier> get() = pathRecorder.identifiers

    fun clone(): PathPropertyWrapper<T>
}

fun <R, Node : PathPropertyWrapper<R>, T, Wrapper : PathPropertyWrapper<T>> Node.valueWithPath(
    recording: Node.() -> Wrapper
): ValueWithPath<T> {
    @Suppress("UNCHECKED_CAST")
    val cloned = this.clone() as Node
    val wrapper = cloned.path(recording)

    return ValueWithPath(wrapper.pathRecorder.identifiers.toList(), wrapper.item)
}

fun <R, Node : PathPropertyWrapper<R>, T, Wrapper : PathPropertyWrapper<T>> Node.path(
    recording: Node.() -> Wrapper
): Wrapper {
    @Suppress("UNCHECKED_CAST")
    val cloned = this.clone() as Node
    return cloned.recording()
}
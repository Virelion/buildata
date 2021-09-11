package io.github.virelion.buildata.path

class PathRecorder(
    val identifiers: MutableList<PathIdentifier> = mutableListOf()
) {
    fun push(pathIdentifier: PathIdentifier) {
        identifiers.add(pathIdentifier)
    }

    fun clone(): PathRecorder = PathRecorder(identifiers.toMutableList())
}
package io.github.virelion.buildata.path

class MissingElementException(
    val index: String,
    val path: List<PathIdentifier>
): IndexOutOfBoundsException("There is no item on index '$index' of '${path.jsonPath}'")
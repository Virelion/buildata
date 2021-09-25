package io.github.virelion.buildata.path

class MissingElementException(
    val index: String,
    val path: RecordedPath
): IndexOutOfBoundsException("There is no item on index '$index' of '${path.jsonPath}'")
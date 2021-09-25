package io.github.virelion.buildata.path

import kotlin.jvm.JvmInline

@JvmInline
value class RecordedPath(private val item: List<PathIdentifier> = listOf()) {
    constructor(vararg identifiers: PathIdentifier) : this(identifiers.toList())

    operator fun plus(identifier: PathIdentifier): RecordedPath {
        return RecordedPath(item + identifier)
    }

    operator fun plus(path: RecordedPath): RecordedPath {
        return RecordedPath(item + path.item)
    }

    val jsonPath : String get() {
        val builder = StringBuilder()
        builder.append("$")
        item.forEach {
            when(it) {
                is IntIndexPathIdentifier -> builder.append("[${it.index}]")
                is StringIndexPathIdentifier -> builder.append("['${it.index}']")
                is StringNamePathIdentifier -> builder.append(".${it.name}")
            }
        }

        return builder.toString()
    }
}
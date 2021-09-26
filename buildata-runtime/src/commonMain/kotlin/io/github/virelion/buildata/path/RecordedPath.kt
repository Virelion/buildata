package io.github.virelion.buildata.path

import kotlin.jvm.JvmInline

@JvmInline
value class RecordedPath(private val item: List<PathIdentifier> = listOf()) {
    operator fun plus(identifier: PathIdentifier): RecordedPath {
        return RecordedPath(item + identifier)
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
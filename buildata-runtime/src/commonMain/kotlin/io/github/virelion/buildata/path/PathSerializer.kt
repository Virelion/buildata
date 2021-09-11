package io.github.virelion.buildata.path

val List<PathIdentifier>.xpath : String get() {
    val builder = StringBuilder()
    builder.append("$")
    forEach {
        when(it) {
            is IntIndexPathIdentifier -> builder.append("[${it.index}]")
            is StringIndexPathIdentifier -> builder.append("['${it.index}']")
            is StringNamePathIdentifier -> builder.append(".${it.name}")
        }
    }

    return builder.toString()
}
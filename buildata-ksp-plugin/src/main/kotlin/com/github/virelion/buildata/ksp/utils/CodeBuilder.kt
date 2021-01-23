package com.github.virelion.buildata.ksp.utils

internal class CodeBuilder(private val indentationDelta: String = "    ") {
    val builder = StringBuilder()
    private var indentation = ""

    var indent = 0
        set(value) {
            field = value

            indentation = buildString {
                repeat(value) { append(indentationDelta) }
            }
        }

    fun appendln(line: String?) {
        if (line != null && line.isNotBlank()) {
            builder.appendLine(indentation + line.trim())
        }
    }

    fun append(line: String?) {
        if (line != null && line.isNotBlank()) {
            builder.append(indentation + line.trim())
        }
    }

    fun emptyLine() {
        builder.appendLine()
    }

    fun indent(block: CodeBuilder.() -> Unit) {
        indentBlock(openingLine = "", enclosingCharacter = "", separator = "", block)
    }

    fun indentBlock(
        openingLine: String,
        enclosingCharacter: String = "{",
        separator: String = " ",
        block: CodeBuilder.() -> Unit
    ) {
        appendln(openingLine.trim() + separator + enclosingCharacter)
        indent++
        this.block()
        indent--
        appendln(REVERSE_ENCLOSING_CHARACTER[enclosingCharacter])
    }

    fun build(block: CodeBuilder.() -> Unit): String {
        this.block()
        return toString()
    }

    override fun toString(): String {
        return builder.toString()
    }

    companion object {
        val REVERSE_ENCLOSING_CHARACTER =
            mapOf(
                "{" to "}",
                "[" to "]",
                "(" to ")",
                "<" to ">"
            )
    }
}

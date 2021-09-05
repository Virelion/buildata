package io.github.virelion.buildata.ksp

import io.github.virelion.buildata.ksp.utils.CodeBuilder

interface GeneratedFileTemplate {
    val pkg: String
    val name: String
    fun generateCode(codeBuilder: CodeBuilder): String
}

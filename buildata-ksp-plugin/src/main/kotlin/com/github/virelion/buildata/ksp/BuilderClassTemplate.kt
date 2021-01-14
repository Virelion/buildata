package com.github.virelion.buildata.ksp

import com.github.virelion.buildata.ksp.utils.CodeBuilder
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

internal class BuilderClassTemplate(
        val pkg: String,
        val originalName: String,
        val properties: List<ClassProperty>
) {
    val builderName = "${originalName}_Builder"

    fun generateCode(codeBuilder: CodeBuilder): String {
        return codeBuilder.build {
            appendln("package $pkg")
            emptyLine()
            appendln("class ${builderName}() {")
            indent {
                properties.forEach {
                    it.generate(builder)
                }
                emptyLine()

                methods.forEach {
                    it.generate(builder)
                }
            }
            appendln("}")
        }
    }
}

class ClassProperty(
        val name: String,
        val type: String,
        val defaultValue: String,
        val optional: Boolean
)
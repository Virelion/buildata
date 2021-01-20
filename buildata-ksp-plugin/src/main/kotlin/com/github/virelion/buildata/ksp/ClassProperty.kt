package com.github.virelion.buildata.ksp

import com.github.virelion.buildata.ksp.extensions.classFQName
import com.github.virelion.buildata.ksp.extensions.typeFQName
import com.github.virelion.buildata.ksp.utils.CodeBuilder
import com.google.devtools.ksp.symbol.KSName
import com.google.devtools.ksp.symbol.KSType

internal class ClassProperty(
        val name: String,
        val type: KSType,
        val hasDefaultValue: Boolean,
        val optional: Boolean,
        val buildable: Boolean
) {
    val backingPropName = "${name}_Element"

    fun generatePropertyDeclaration(codeBuilder: CodeBuilder) {
        codeBuilder.build {
            if (buildable) {
                val builderName = BuilderClassTemplate.createBuilderName(type.classFQName())
                appendln("val $name = $builderName()")
                appendln("fun $name(block: $builderName.() -> Unit) {")
                indent {
                    appendln("$name.block()")
                }
                appendln("}")
            } else {
                appendln("private val $backingPropName = BuilderElementProperty<${type.typeFQName()}>()")
                appendln("var $name by $backingPropName")
            }
        }
    }

    fun generateDataClassInitialization(codeBuilder: CodeBuilder) {
        codeBuilder.build {
            if(buildable) {
                appendln("$name = ${name}.build(),")
            } else {
                appendln("$name = $name,")
            }
        }
    }
}

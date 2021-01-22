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
        val nullable: Boolean,
        val buildable: Boolean
) {
    val backingPropName = "${name}_Element"

    fun generatePropertyDeclaration(codeBuilder: CodeBuilder) {
        codeBuilder.build {
            if (buildable) {
                val builderName = BuilderClassTemplate.createBuilderName(type.classFQName())
                val elementPropName = if(nullable) {
                    "BuilderNullableCompositeElementProperty"
                } else {
                    "BuilderCompositeElementProperty"
                }
                appendln("private val $backingPropName = $elementPropName<${type.classFQName()}, $builderName> { $builderName() }")
                appendln("var $name by $backingPropName")
                appendln("fun $name(block: $builderName.() -> Unit) {")
                indent {
                    appendln("$backingPropName.useBuilder(block)")
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
            appendln("$name = $name,")
        }
    }

    fun generatePopulateWithLine(codeBuilder: CodeBuilder) {
        codeBuilder.build {
            appendln("$name = it.$name")
        }
    }
}

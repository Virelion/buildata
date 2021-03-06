package io.github.virelion.buildata.ksp

import com.google.devtools.ksp.symbol.KSType
import io.github.virelion.buildata.ksp.extensions.classFQName
import io.github.virelion.buildata.ksp.extensions.typeFQName
import io.github.virelion.buildata.ksp.utils.CodeBuilder

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
                val elementPropName = if (nullable) {
                    "BuilderNullableCompositeElementProperty"
                } else {
                    "BuilderCompositeElementProperty"
                }
                appendln("private val $backingPropName = $elementPropName<${type.classFQName()}, $builderName> { $builderName() }")
                appendln("var $name by $backingPropName")
                appendln("@BuildataDSL")
                indentBlock("fun $name(block: $builderName.() -> Unit)") {
                    appendln("$backingPropName.useBuilder(block)")
                }
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

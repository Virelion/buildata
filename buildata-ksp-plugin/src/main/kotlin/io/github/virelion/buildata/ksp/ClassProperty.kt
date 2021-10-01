package io.github.virelion.buildata.ksp

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSType
import io.github.virelion.buildata.ksp.extensions.classFQName
import io.github.virelion.buildata.ksp.extensions.className
import io.github.virelion.buildata.ksp.extensions.pkg
import io.github.virelion.buildata.ksp.extensions.typeFQName
import io.github.virelion.buildata.ksp.utils.CodeBuilder

class ClassProperty(
    val name: String,
    val type: KSType,
    val hasDefaultValue: Boolean,
    val nullable: Boolean,
    val annotations: Sequence<KSAnnotation>,
    val buildable: Boolean,
    val pathReflection: Boolean
) {
    val backingPropName = "${name}_Element"

    fun generatePropertyDeclaration(codeBuilder: CodeBuilder) {
        codeBuilder.build {
            if (buildable) {
                val builderName = type.pkg() + "." + BuilderClassTemplate.createBuilderName(type.className())
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

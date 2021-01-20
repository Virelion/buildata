package com.github.virelion.buildata.ksp

import com.github.virelion.buildata.ksp.extensions.toFQName
import com.github.virelion.buildata.ksp.utils.CodeBuilder
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Modifier
import com.google.devtools.ksp.symbol.Nullability
import java.lang.IllegalStateException

internal fun KSClassDeclaration.buildClass(): BuilderClassTemplate {
    if(Modifier.DATA !in this.modifiers) {
        throw IllegalStateException("Cannot add create builder for non data class")
    }
    return BuilderClassTemplate(
            pkg = this.packageName.asString(),
            originalName = this.simpleName.getShortName(),
            properties = requireNotNull(this.primaryConstructor) { "${this.qualifiedName} needs to have primary constructor" }
                    .parameters
                    .filter { it.isVar || it.isVal }
                    .map {
                        val type = it.type.resolve()
                        ClassProperty(
                                name = requireNotNull(it.name?.getShortName()) { "${this.qualifiedName} contains nameless property" },
                                type = type.toFQName(),
                                hasDefaultValue = it.hasDefault,
                                optional = type.nullability == Nullability.NULLABLE
                        )
                    }
    )
}

internal class BuilderClassTemplate(
        val pkg: String,
        val originalName: String,
        val properties: List<ClassProperty>
) {
    private val propertiesWithDefault = properties.filter { it.hasDefaultValue }
    private val propertiesWithoutDefault = properties.filter { !it.hasDefaultValue }

    val builderName = "${originalName}_Builder"

    fun generateCode(codeBuilder: CodeBuilder): String {
        return codeBuilder.build {
            appendln("package $pkg")
            emptyLine()
            imports.forEach {
                appendln("import $it")
            }
            emptyLine()
            appendln("fun KClass<$originalName>.builder(): $builderName {")
            indent {
                appendln("return $builderName()")
            }
            appendln("}")
            emptyLine()
            appendln("fun KClass<$originalName>.build(")
            indent {
                appendln("builder: $builderName.() -> Unit")
            }
            appendln("): $originalName {")
            indent {
                appendln("return $builderName().apply { builder() }.build()")
            }
            appendln("}")
            emptyLine()
            appendln("@BuildataDSL")
            appendln("class ${builderName}() : Builder<$originalName> {")
            indent {
                properties.forEach {
                    it.generateCode(this)
                }
                emptyLine()
                appendln("override fun build(): $originalName {")
                indent {
                    appendln("var result = $originalName(")
                    indent {
                        propertiesWithoutDefault.forEach {
                            appendln("${it.name} = ${it.name},")
                        }
                    }
                    appendln(")")

                    propertiesWithDefault.forEach {
                        appendln("if (${it.backingPropName}.initialized) {")
                        indent {
                            appendln("result = result.copy(${it.name} = ${it.name})")
                        }
                        appendln("}")
                    }

                    appendln("return result")
                }
                appendln("}")
            }
            appendln("}")
        }
    }

    companion object {
        val imports: List<String> = listOf(
                "com.github.virelion.buildata.Builder",
                "com.github.virelion.buildata.BuilderElementProperty",
                "com.github.virelion.buildata.BuildataDSL",
                "kotlin.reflect.KClass"
        ).sorted()

    }
}

internal class ClassProperty(
        val name: String,
        val type: String,
        val hasDefaultValue: Boolean,
        val optional: Boolean
) {
    val backingPropName = "${name}_Element"

    fun generateCode(codeBuilder: CodeBuilder) {
        codeBuilder.appendln("var $backingPropName = BuilderElementProperty<$type>($hasDefaultValue, $optional)")
        codeBuilder.appendln("var ${name}: $type by $backingPropName")
    }
}
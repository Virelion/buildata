package com.github.virelion.buildata.ksp

import com.github.virelion.buildata.ksp.Constants.BUILDABLE_FQNAME
import com.github.virelion.buildata.ksp.extensions.typeFQName
import com.github.virelion.buildata.ksp.utils.CodeBuilder
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Modifier
import com.google.devtools.ksp.symbol.Nullability

internal fun KSClassDeclaration.buildClass(): BuilderClassTemplate {
    if (Modifier.DATA !in this.modifiers) {
        throw IllegalStateException("Cannot add create builder for non data class")
    }
    val fqName = this.packageName.asString() + "." + this.simpleName.getShortName()
    return BuilderClassTemplate(
            pkg = this.packageName.asString(),
            originalName = this.simpleName.getShortName(),
            properties = requireNotNull(this.primaryConstructor) { "$fqName needs to have primary constructor" }
                    .parameters
                    .filter { it.isVar || it.isVal }
                    .map { parameter ->
                        val type = parameter.type.resolve()
                        ClassProperty(
                                name = requireNotNull(parameter.name?.getShortName()) { "$fqName contains nameless property" },
                                type = type,
                                hasDefaultValue = parameter.hasDefault,
                                nullable = type.nullability == Nullability.NULLABLE,
                                buildable = type.annotations
                                        .any { annotation ->
                                            annotation.annotationType.resolve().typeFQName() == BUILDABLE_FQNAME
                                        }
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

    val builderName = createBuilderName(originalName)

    fun generateCode(codeBuilder: CodeBuilder): String {
        return codeBuilder.build {
            appendln("package $pkg")
            emptyLine()
            imports.forEach {
                appendln("import $it // ktlint-disable")
            }
            emptyLine()
            generateClassBuilderExtension()
            emptyLine()
            generateClassBuildExtension()
            emptyLine()
            generateBuilderInvokeExtension()
            emptyLine()
            generateBuilderClass()
        }
    }

    fun CodeBuilder.generateClassBuilderExtension() {
        appendln("fun KClass<$originalName>.builder(): $builderName {")
        indent {
            appendln("return $builderName()")
        }
        appendln("}")
    }

    fun CodeBuilder.generateClassBuildExtension() {
        appendln("fun KClass<$originalName>.build(")
        indent {
            appendln("builder: $builderName.() -> Unit")
        }
        appendln("): $originalName {")
        indent {
            appendln("return $builderName().apply { builder() }.build()")
        }
        appendln("}")
    }

    fun CodeBuilder.generateBuilderInvokeExtension() {
        appendln("operator fun $builderName.invoke(")
        indent {
            appendln("block: $builderName.() -> Unit")
        }
        appendln(") {")
        indent {
            appendln("block()")
        }
        appendln("}")
    }

    fun CodeBuilder.generateBuilderClass() {
        appendln("@BuildataDSL")
        appendln("class ${builderName}() : Builder<$originalName> {")
        indent {
            properties.forEach {
                it.generatePropertyDeclaration(this)
            }
            emptyLine()
            generateBuildFunction()
            emptyLine()
            generateBuilderPopulateWithFunction()
        }
        appendln("}")
    }

    fun CodeBuilder.generateBuildFunction() {
        appendln("override fun build(): $originalName {")
        indent {
            appendln("var result = $originalName(")
            indent {
                propertiesWithoutDefault.forEach {
                    it.generateDataClassInitialization(this)
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

    fun CodeBuilder.generateBuilderPopulateWithFunction() {
        appendln("override fun populateWith(source: $originalName) {")
        indent {
            appendln("source.let {")
            indent {
                properties.forEach {
                    it.generatePopulateWithLine(this)
                }
            }
            appendln("}")
        }
        appendln("}")
    }

    companion object {
        val imports: List<String> = listOf(
                "com.github.virelion.buildata.*",
                "kotlin.reflect.KClass"
        ).sorted()

        fun createBuilderName(name: String): String {
            return "${name}_Builder"
        }

    }
}

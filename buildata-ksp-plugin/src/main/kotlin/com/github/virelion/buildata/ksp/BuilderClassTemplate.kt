package com.github.virelion.buildata.ksp

import com.github.virelion.buildata.ksp.extensions.toFQName
import com.github.virelion.buildata.ksp.utils.CodeBuilder
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Nullability

internal fun KSClassDeclaration.buildClass(): BuilderClassTemplate {
    return BuilderClassTemplate(
            pkg = this.packageName.asString(),
            originalName = this.simpleName.getShortName(),
            properties = this.getAllProperties().map {
                val type = it.type.resolve()
                ClassProperty(
                    name = it.simpleName.getShortName(),
                    type = type.toFQName(),
                    defaultValue = null,
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
            appendln("class ${builderName}() : Builder<$originalName> {")
            indent {
                properties.forEach {
                    it.generateCode(this)
                }
                emptyLine()
                appendln("override fun build(): $originalName {")
                indent {
                    appendln("return $originalName(")
                    indent {
                        properties.forEach {
                            appendln("${it.name} = ${it.name},")
                        }
                    }
                    appendln(")")
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
                "kotlin.reflect.KClass"
        ).sorted()

    }
}

internal class ClassProperty(
        val name: String,
        val type: String,
        val defaultValue: String?,
        val optional: Boolean
) {
    fun generateCode(codeBuilder: CodeBuilder) {
        codeBuilder.appendln("var ${name}: ${type} by BuilderElementProperty(${generateDefaultValueLambda()}, $optional)")
    }

    fun generateDefaultValueLambda(): String {
        return defaultValue?.let { "{ $it } "} ?: "null"
    }
}
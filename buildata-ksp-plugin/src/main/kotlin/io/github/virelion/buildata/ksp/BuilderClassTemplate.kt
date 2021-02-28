package io.github.virelion.buildata.ksp

import io.github.virelion.buildata.ksp.utils.CodeBuilder

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
        indentBlock("fun KClass<$originalName>.builder(): $builderName") {
            appendln("return $builderName()")
        }
    }

    fun CodeBuilder.generateClassBuildExtension() {
        appendln("@BuildataDSL")
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
        appendln("@BuildataDSL")
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
        indentBlock("class $builderName() : Builder<$originalName>") {
            properties.forEach {
                it.generatePropertyDeclaration(this)
            }
            emptyLine()
            generateBuildFunction()
            emptyLine()
            generateBuilderPopulateWithFunction()
        }
    }

    fun CodeBuilder.generateBuildFunction() {
        indentBlock("override fun build(): $originalName") {
            indentBlock("var result = $originalName", separator = "", enclosingCharacter = "(") {
                propertiesWithoutDefault.forEach {
                    it.generateDataClassInitialization(this)
                }
            }

            propertiesWithDefault.forEach {
                indentBlock("if (${it.backingPropName}.initialized)") {
                    appendln("result = result.copy(${it.name} = ${it.name})")
                }
            }

            appendln("return result")
        }
    }

    fun CodeBuilder.generateBuilderPopulateWithFunction() {
        indentBlock("override fun populateWith(source: $originalName)") {
            indentBlock("source.let") {
                properties.forEach {
                    it.generatePopulateWithLine(this)
                }
            }
        }
    }

    companion object {
        val imports: List<String> = listOf(
            "io.github.virelion.buildata.*",
            "kotlin.reflect.KClass"
        ).sorted()

        fun createBuilderName(name: String): String {
            return "${name}_Builder"
        }
    }
}

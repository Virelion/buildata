package io.github.virelion.buildata.ksp

import io.github.virelion.buildata.ksp.extensions.typeForDocumentation
import io.github.virelion.buildata.ksp.utils.CodeBuilder

internal class BuilderClassTemplate(
    val pkg: String,
    val originalName: String,
    val properties: List<ClassProperty>
) {
    private val propertiesWithDefault = properties.filter { it.hasDefaultValue }
    private val propertiesWithoutDefault = properties.filter { !it.hasDefaultValue }

    private val propertiesDocumentation = buildString {
        if (propertiesWithoutDefault.isNotEmpty()) {
            appendLine()
            appendLine("__Required elements__: ")
            propertiesWithoutDefault.forEach {
                appendLine("- ${it.name}: ${it.type.typeForDocumentation()}")
            }
        }
        if (propertiesWithDefault.isNotEmpty()) {
            appendLine()
            appendLine("__Optional elements__:")
            propertiesWithDefault.forEach {
                appendLine("- ${it.name}: ${it.type.typeForDocumentation()}")
            }
        }
    }

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
        appendDocumentation("Create builder for [$originalName]")
        indentBlock("fun KClass<$originalName>.builder(): $builderName") {
            appendln("return $builderName()")
        }
    }

    fun CodeBuilder.generateClassBuildExtension() {
        appendDocumentation(
            "Construct [$originalName] using builder DSL.",
            propertiesDocumentation,
            """
                @param builder DSL lambda
                @throws [io.gihtub.virelion.buildata.UninitializedPropertyException] when any required property is not set
                @return [$originalName] 
            """.trimIndent()
        )
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
        appendDocumentation(
            "Invoke [$originalName] building DSL",
            propertiesDocumentation,
            """
                @param builder DSL lambda
                @throws [io.gihtub.virelion.buildata.UninitializedPropertyException] when any required property is not set
            """.trimIndent()
        )
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
        appendDocumentation(
            "Builder for [$originalName]",
            propertiesDocumentation
        )
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
        appendDocumentation(
            """
                Build [$originalName] object
                
                @return data object 
                @throws [io.gihtub.virelion.buildata.UninitializedPropertyException] when any required property is not set
            """.trimIndent()
        )
        indentBlock("override fun build(): $originalName") {
            indentBlock("var result = $originalName", separator = "", enclosingCharacter = "(") {
                propertiesWithoutDefault.forEach {
                    it.generateDataClassInitialization(this)
                }
            }

            if (propertiesWithDefault.isNotEmpty()) {
                propertiesWithDefault.forEach {
                    appendln("var ${it.name}_Component = result.${it.name}")

                    indentBlock("if (${it.backingPropName}.initialized)") {
                        appendln("${it.name}_Component = ${it.name}")
                    }
                }

                indentBlock("result = result.copy", enclosingCharacter = "(", separator = "") {
                    propertiesWithDefault.forEach {
                        appendln("${it.name} = ${it.name}_Component,")
                    }
                }
            }
            appendln("return result")
        }
    }

    fun CodeBuilder.generateBuilderPopulateWithFunction() {
        appendDocumentation(
            """
                Sets all builder properties from source object.
                
                @param source object that will populate builder.
            """.trimIndent()
        )
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

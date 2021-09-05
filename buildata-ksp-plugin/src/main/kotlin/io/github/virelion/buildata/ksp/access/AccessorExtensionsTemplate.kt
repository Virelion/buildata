package io.github.virelion.buildata.ksp.access

import io.github.virelion.buildata.ksp.GeneratedFileTemplate
import io.github.virelion.buildata.ksp.utils.CodeBuilder

class AccessorExtensionsTemplate(
    override val pkg: String,
    val originalName: String,
    val properties: List<String>
) : GeneratedFileTemplate {
    override val name: String get() = "${originalName}_AccessorExtension"

    override fun generateCode(codeBuilder: CodeBuilder): String {
        return codeBuilder.build {
            appendln("package $pkg")
            emptyLine()
            imports.forEach {
                appendln("import $it // ktlint-disable")
            }
            emptyLine()
            createAccessorExtensionProperty()
            emptyLine()
            createPropertyAccessExtensionFunction()
            emptyLine()
            createPropertyValueAccessExtensionFunction()
        }
    }

    private fun CodeBuilder.createAccessorExtensionProperty() {
        appendDocumentation(
            """
                Accessor that allows for dynamic access (via property name) to properties and values.
            """.trimIndent()
        )
        appendln("val $originalName.dynamicAccessor: DynamicAccessor<$originalName> get() = DynamicAccessor(this)")
    }

    private fun CodeBuilder.createPropertyAccessExtensionFunction() {
        appendDocumentation(
            """
                Get property of class under specified name
                
                @param name property name
                @throws [io.github.virelion.buildata.access.MissingPropertyException] if property is missing
                @returns property object or null in case it is missing
            """.trimIndent()
        )
        indentBlock("fun DynamicAccessor<$originalName>.getProperty(name: String): KProperty0<*>?") {
            indentBlock("return when(name)") {
                properties.forEach { propertyName ->
                    appendln(""""$propertyName" -> this.target::`$propertyName`""")
                }
                appendln("""else -> throw MissingPropertyException(name, "$pkg.$originalName")""")
            }
        }
    }

    private fun CodeBuilder.createPropertyValueAccessExtensionFunction() {
        appendDocumentation(
            """
                Get value of property under specified name
                
                @param name property name
                @throws [io.github.virelion.buildata.access.MissingPropertyException] if property is missing
                @returns property value
            """.trimIndent()
        )
        indentBlock("operator fun <T> DynamicAccessor<$originalName>.get(name: String): T") {
            indentBlock("return when(name)") {
                properties.forEach { propertyName ->
                    appendln(""""$propertyName" -> this.target.`$propertyName`""")
                }
                appendln("""else -> throw MissingPropertyException(name, "$pkg.$originalName")""")
            }
            append(" as T")
        }
    }

    companion object {
        val imports: List<String> = listOf(
            "io.github.virelion.buildata.access.*",
            "kotlin.reflect.KProperty0"
        ).sorted()
    }
}

package io.github.virelion.buildata.ksp.path

import com.google.devtools.ksp.symbol.Nullability
import io.github.virelion.buildata.ksp.ClassProperty
import io.github.virelion.buildata.ksp.GeneratedFileTemplate
import io.github.virelion.buildata.ksp.extensions.className
import io.github.virelion.buildata.ksp.extensions.typeFQName
import io.github.virelion.buildata.ksp.utils.CodeBuilder
import io.github.virelion.buildata.ksp.utils.isScalar
import io.github.virelion.buildata.ksp.utils.nullableIdentifier

class PathPropertyWrapperTemplate(
    override val pkg: String,
    val originalName: String,
    val properties: List<ClassProperty>
) : GeneratedFileTemplate {
    override val name: String = "${originalName}_PathPropertyWrapper"
    private val nullableName: String = "${originalName}_NullablePathPropertyWrapper"

    override fun generateCode(codeBuilder: CodeBuilder): String {
        return codeBuilder.build {
            appendln("package $pkg")
            emptyLine()
            imports.forEach {
                appendln("import $it // ktlint-disable")
            }
            emptyLine()
            createPathPropertyWrapperClass(nullable = false)
            emptyLine()
            createPathPropertyWrapperClass(nullable = true)
            emptyLine()
            createExtensionPathMethod()
            emptyLine()
        }
    }

    private fun CodeBuilder.createPathPropertyWrapperClass(nullable: Boolean) {
        val nId = nullableIdentifier(nullable)
        val className = if (nullable) {
            nullableName
        } else {
            name
        }
        appendDocumentation(
            """
                TODO
            """.trimIndent()
        )
        indentBlock("class $className(override val item: $originalName$nId, override val pathRecorder: PathRecorder) : PathPropertyWrapper<$originalName${nId}>") {
            properties.forEach {
                createPropertyEntry(it, nullable || it.type.nullability == Nullability.NULLABLE)
            }
            emptyLine()
            createCloneMethod(className)
        }
    }

    private fun CodeBuilder.createPropertyEntry(classProperty: ClassProperty, nullable: Boolean = false) {
        val nId = nullableIdentifier(nullable)
        val wrapperName = if (classProperty.type.isScalar()) {
            "ScalarPathPropertyWrapper"
        } else {
            if (nullable) {
                classProperty.type.className() + "_NullablePathPropertyWrapper"
            } else {
                classProperty.type.className() + "_PathPropertyWrapper"
            }
        }

        val wrapperType = if (classProperty.type.isScalar()) {
            wrapperName + "<${classProperty.type.typeFQName()}$nId>"
        } else {
            wrapperName
        }

        indentBlock("val ${classProperty.name}: $wrapperType by PathRecorderProperty(pathRecorder)") {
            indentBlock(wrapperName, enclosingCharacter = "(") {
                appendln("item$nId.${classProperty.name},")
                appendln("pathRecorder")
            }
        }
    }

    private fun CodeBuilder.createExtensionPathMethod() {
        appendDocumentation(
            """
                TODO
            """.trimIndent()
        )
        indentBlock("fun <T, Wrapper : PathPropertyWrapper<T>> $originalName.valueWithPath", enclosingCharacter = "(") {
            appendln("recording: $name.() -> Wrapper")
        }
        append("= $name(this, PathRecorder()).valueWithPath(recording)")
        emptyLine()
        appendDocumentation(
            """
                TODO
            """.trimIndent()
        )
        indentBlock("fun <T, Wrapper : PathPropertyWrapper<T>> $originalName.path", enclosingCharacter = "(") {
            appendln("recording: $name.() -> Wrapper")
        }
        append("= $name(this, PathRecorder()).path(recording)")
    }

    private fun CodeBuilder.createCloneMethod(className: String) {
        appendDocumentation(
            """
                TODO
            """.trimIndent()
        )
        indentBlock("override fun clone(): $className") {
            appendln("return $className(item, pathRecorder.clone())")
        }
    }

    companion object {
        val imports: List<String> = listOf(
            "io.github.virelion.buildata.path.*",
        ).sorted()
    }
}

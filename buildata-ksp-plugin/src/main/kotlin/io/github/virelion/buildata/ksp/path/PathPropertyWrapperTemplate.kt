package io.github.virelion.buildata.ksp.path

import com.google.devtools.ksp.innerArguments
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Nullability
import io.github.virelion.buildata.ksp.ClassProperty
import io.github.virelion.buildata.ksp.GeneratedFileTemplate
import io.github.virelion.buildata.ksp.extensions.className
import io.github.virelion.buildata.ksp.extensions.typeFQName
import io.github.virelion.buildata.ksp.utils.*

class PathPropertyWrapperTemplate(
    override val pkg: String,
    val originalName: String,
    val properties: List<ClassProperty>
) : GeneratedFileTemplate {
    companion object {
        val imports: List<String> = listOf(
            "io.github.virelion.buildata.path.*",
        ).sorted()
    }

    override val name: String = "${originalName}_PathReflectionPropertyWrapper"
    private val nullableWrapperName: String = "${originalName}_NullablePathReflectionPropertyWrapper"

    override fun generateCode(codeBuilder: CodeBuilder): String {
        return codeBuilder.build {
            appendln("package $pkg")
            emptyLine()
            imports.forEach {
                appendln("import $it // ktlint-disable")
            }
            emptyLine()
            createPathReflectionPropertyWrapperClass(nullable = false)
            emptyLine()
            createPathReflectionPropertyWrapperClass(nullable = true)
            emptyLine()
            createExtensionPathMethod()
            emptyLine()
        }
    }

    private fun CodeBuilder.createPathReflectionPropertyWrapperClass(nullable: Boolean) {
        val nId = nullableIdentifier(nullable)
        val className = if (nullable) {
            nullableWrapperName
        } else {
            name
        }
        appendDocumentation(
            """
                TODO
            """.trimIndent()
        )
        indentBlock(
            "class $className",
            enclosingCharacter = "(",
            sufix = " : PathReflectionPropertyWrapper<$originalName${nId}> {"
        ) {
            appendln("private val __value: $originalName$nId,")
            appendln("private val __path: RecordedPath")
        }
        indent {
            properties.forEach {
                createPropertyEntry(it, nullable || it.type.nullability == Nullability.NULLABLE)
            }
            emptyLine()
            createOverrides()
        }
        appendln("}")
    }


    private fun CodeBuilder.createPropertyEntry(classProperty: ClassProperty, nullable: Boolean) {
        if (classProperty.type.isList()) {
            createCollectionPropertyEntry(classProperty, nullable, CollectionType.LIST)
            return
        }
        if (classProperty.type.isMapWithStringKey()) {
            createCollectionPropertyEntry(classProperty, nullable, CollectionType.STRING)
            return
        }

        val nId = nullableIdentifier(nullable)
        val wrapperName = getWrapperName(classProperty.type, nullable)

        val wrapperType = if (classProperty.type.isScalar()) {
            wrapperName + "<${classProperty.type.typeFQName()}$nId>"
        } else {
            wrapperName
        }

        indentBlock("val ${classProperty.name}: $wrapperType by lazy") {
            indentBlock(wrapperName, enclosingCharacter = "(") {
                appendln("value()$nId.${classProperty.name},")
                appendln("""path() + StringNamePathIdentifier("${classProperty.name}")""")
            }
        }
    }

    private fun getWrapperName(type: KSType, nullable: Boolean): String {
        return if (type.isScalar()) {
            "ScalarPathReflectionPropertyWrapper"
        } else {
            if (nullable) {
                type.className() + "_NullablePathReflectionPropertyWrapper"
            } else {
                type.className() + "_PathReflectionPropertyWrapper"
            }
        }
    }

    private fun getWrapperType(type: KSType, nullable: Boolean): String {
        return if (type.isScalar()) {
            getWrapperName(type, nullable) + "<${type.typeFQName()}>"
        } else {
            getWrapperName(type, nullable)
        }
    }

    private fun CodeBuilder.createCollectionPropertyEntry(
        classProperty: ClassProperty,
        nullable: Boolean,
        collectionType: CollectionType
    ) {
        val defaultInitializer = when (collectionType) {
            CollectionType.LIST -> "listOf()"
            CollectionType.STRING -> "mapOf()"
        }

        val recorderClassImpl = when (collectionType) {
            CollectionType.LIST -> "ListPathReflectionRecorder"
            CollectionType.STRING -> "MapPathReflectionRecorder"
        }

        val indexClassIdentifierImpl = when (collectionType) {
            CollectionType.LIST -> "IntIndexPathIdentifier"
            CollectionType.STRING -> "StringIndexPathIdentifier"
        }

        val itemType =
            requireNotNull(classProperty.type.innerArguments.last().type?.resolve()) { "Unable to resolve type of list ${classProperty.name} in $name" }
        val itemNullable = itemType.nullability != Nullability.NOT_NULL || nullable

        val itemNId = nullableIdentifier(itemNullable)
        val forceNotNull = if (!itemNullable) {
            "!!"
        } else {
            ""
        }

        val wrapperType = getWrapperType(itemType, itemNullable)
        indentBlock("val ${classProperty.name}: $recorderClassImpl<${itemType.typeFQName()}$itemNId, $wrapperType> by lazy") {
            indentBlock(
                """$recorderClassImpl(value()${itemNId}.${classProperty.name}${
                    if (itemNullable) {
                        " ?: $defaultInitializer"
                    } else {
                        ""
                    }
                }, path() + StringNamePathIdentifier("${classProperty.name}"))"""
            ) {
                appendln("path, index, item ->")
                indentBlock(getWrapperName(itemType, itemNullable), enclosingCharacter = "(") {
                    appendln("item$forceNotNull,")
                    appendln("""path + $indexClassIdentifierImpl(index)""")
                }
            }
        }
    }

    private fun CodeBuilder.createOverrides() {
        appendDocumentation(
            """
                TODO
            """.trimIndent()
        )
        appendln("override fun value() = __value")
        emptyLine()
        appendDocumentation(
            """
                TODO
            """.trimIndent()
        )
        appendln("override fun path() = __path")
    }

    private fun CodeBuilder.createExtensionPathMethod() {
        appendDocumentation(
            """
                TODO
            """.trimIndent()
        )
        appendln("fun $originalName.withPath() = $name(this, RecordedPath())")
    }
}

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
    override val name: String = "${originalName}_PathPropertyWrapper"
    private val nullableWrapperName: String = "${originalName}_NullablePathPropertyWrapper"

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
            nullableWrapperName
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


    private fun CodeBuilder.createPropertyEntry(classProperty: ClassProperty, nullable: Boolean) {
        if (classProperty.type.isList()) {
            createCollectionPropertyEntry(classProperty, nullable, CollectionType.LIST)
            return
        }
        if(classProperty.type.isMapWithStringKey()) {
            createCollectionPropertyEntry(classProperty, nullable, CollectionType.STRING_KEY_MAP)
            return
        }

        val nId = nullableIdentifier(nullable)
        val wrapperName = getWrapperName(classProperty.type, nullable)

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

    private fun getWrapperName(type: KSType, nullable: Boolean): String {
        return if (type.isScalar()) {
            "ScalarPathPropertyWrapper"
        } else {
            if (nullable) {
                type.className() + "_NullablePathPropertyWrapper"
            } else {
                type.className() + "_PathPropertyWrapper"
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

    private fun CodeBuilder.createCollectionPropertyEntry(classProperty: ClassProperty, nullable: Boolean, collectionType: CollectionType) {
        val defaultInitializer = when(collectionType) {
            CollectionType.LIST -> "listOf()"
            CollectionType.STRING_KEY_MAP -> "mapOf()"
        }

        val recorderClassImpl = when(collectionType) {
            CollectionType.LIST -> "ListPathRecorder"
            CollectionType.STRING_KEY_MAP -> "MapPathRecorder"
        }

        val itemType = requireNotNull(classProperty.type.innerArguments.last().type?.resolve()) { "Unable to resolve type of list ${classProperty.name} in $name"}
        val itemNullable = itemType.nullability != Nullability.NOT_NULL || nullable

        val itemNId = nullableIdentifier(itemNullable)
        val forceNotNull = if(!itemNullable) { "!!" } else { "" }
        val wrapperType = getWrapperType(itemType, itemNullable)
        indentBlock("val ${classProperty.name}: $recorderClassImpl<${itemType.typeFQName()}$itemNId, $wrapperType> by PathRecorderProperty(pathRecorder)") {
            indentBlock("$recorderClassImpl(item${itemNId}.${classProperty.name}${if(itemNullable) { " ?: $defaultInitializer"} else {""} }, pathRecorder)") {
                appendln("recorder, it -> ${getWrapperName(itemType, itemNullable)}(it$forceNotNull, recorder)")
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

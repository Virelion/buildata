package io.github.virelion.buildata.ksp.path

import com.google.devtools.ksp.innerArguments
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Nullability
import io.github.virelion.buildata.ksp.ClassProperty
import io.github.virelion.buildata.ksp.GeneratedFileTemplate
import io.github.virelion.buildata.ksp.extensions.className
import io.github.virelion.buildata.ksp.extensions.typeFQName
import io.github.virelion.buildata.ksp.utils.CodeBuilder
import io.github.virelion.buildata.ksp.utils.isList
import io.github.virelion.buildata.ksp.utils.isScalar
import io.github.virelion.buildata.ksp.utils.nullableIdentifier

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
            createListPropertyEntry(classProperty, nullable)
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

    /*
        val innerList: ListPathRecorder<Inner2, Inner2_PathPropertyWrapper> by PathRecorderProperty(pathRecorder) {
        ListPathRecorder(item.innerList, pathRecorder) { recorder, it ->
            Inner2_PathPropertyWrapper(it!!, recorder)
        }
    }
    val listOfNullables: ListPathRecorder<Inner2?, Inner2_NullablePathPropertyWrapper> by PathRecorderProperty(
        pathRecorder
    ) {
        ListPathRecorder(item.listOfNullables, pathRecorder) { recorder, it ->
            Inner2_NullablePathPropertyWrapper(it, recorder)
        }
    }
     */

    private fun CodeBuilder.createListPropertyEntry(classProperty: ClassProperty, nullable: Boolean) {
        val itemType = requireNotNull(classProperty.type.innerArguments.last().type?.resolve()) { "Unable to resolve type of list ${classProperty.name} in $name"}
        val itemNullable = itemType.nullability != Nullability.NOT_NULL || nullable

        val itemNId = nullableIdentifier(itemNullable)
        val forceNotNull = if(!itemNullable) { "!!" } else { "" }
        val wrapperType = getWrapperType(itemType, itemNullable)
        indentBlock("val ${classProperty.name}: ListPathRecorder<${itemType.typeFQName()}$itemNId, $wrapperType> by PathRecorderProperty(pathRecorder)") {
            indentBlock("ListPathRecorder(item${itemNId}.${classProperty.name}${if(itemNullable) { " ?: listOf()"} else {""} }, pathRecorder)") {
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

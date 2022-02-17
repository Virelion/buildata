/*
 * Copyright 2021 Maciej Ziemba
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.virelion.buildata.ksp.path

import com.google.devtools.ksp.innerArguments
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Nullability
import io.github.virelion.buildata.ksp.BuildataCodegenException
import io.github.virelion.buildata.ksp.ClassProperty
import io.github.virelion.buildata.ksp.GeneratedFileTemplate
import io.github.virelion.buildata.ksp.extensions.classFQName
import io.github.virelion.buildata.ksp.extensions.className
import io.github.virelion.buildata.ksp.extensions.typeFQName
import io.github.virelion.buildata.ksp.utils.CodeBuilder
import io.github.virelion.buildata.ksp.utils.isList
import io.github.virelion.buildata.ksp.utils.isMapWithStringKey
import io.github.virelion.buildata.ksp.utils.isScalar
import io.github.virelion.buildata.ksp.utils.nullableIdentifier

class PathPropertyWrapperTemplate(
    override val pkg: String,
    val originalName: String,
    val properties: List<ClassProperty>
) : GeneratedFileTemplate {
    companion object {
        val imports: List<String> = listOf(
            "io.github.virelion.buildata.path.*",
            "kotlin.reflect.KClass"
        ).sorted()
    }

    override val name: String = "${originalName}_PathReflectionWrapper"
    private val nullableWrapperName: String = "${originalName}_NullablePathReflectionWrapper"

    override fun generateCode(codeBuilder: CodeBuilder): String {
        return codeBuilder.build {
            appendln("package $pkg")
            emptyLine()
            imports.forEach {
                appendln("import $it // ktlint-disable")
            }
            emptyLine()
            createPathReflectionWrapperClass(nullable = false)
            emptyLine()
            createPathReflectionWrapperClass(nullable = true)
            emptyLine()
            createExtensionWithPathMethod()
            emptyLine()
            createExtensionPathMethod()
            emptyLine()
        }
    }

    private fun CodeBuilder.createPathReflectionWrapperClass(nullable: Boolean) {
        val nId = nullableIdentifier(nullable)
        val className = if (nullable) {
            nullableWrapperName
        } else {
            name
        }
        appendDocumentation(
            """
                Implementation of [io.github.virelion.buildata.path.PathReflectionWrapper] for${if (!nullable) " not" else ""} nullable item of [$pkg.$originalName]
            """.trimIndent()
        )
        indentBlock(
            "class $className",
            enclosingCharacter = "(",
            sufix = " : PathReflectionWrapper<$originalName$nId> {"
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
        val wrapperFQName = getWrapperFQName(classProperty.type, nullable)

        val wrapperType = if (classProperty.type.isScalar()) {
            wrapperFQName + "<${classProperty.type.typeFQName()}$nId>"
        } else {
            if (!classProperty.pathReflection) {
                throw BuildataCodegenException(
                    """Cannot create path reflection wrapper for: $originalName.
                       Member element ${classProperty.name} not annotated for reflection. 
                       Annotate ${classProperty.type.classFQName()} with @PathReflection"""
                )
            }
            wrapperFQName
        }

        indentBlock("val ${classProperty.name}: $wrapperType by lazy") {
            indentBlock(wrapperFQName, enclosingCharacter = "(") {
                appendln("value()$nId.${classProperty.name},")
                appendln("path() + ${StringNamePathIdentifier(classProperty)}")
            }
        }
    }

    private fun getWrapperName(type: KSType, nullable: Boolean): String {
        return if (type.isScalar()) {
            "ScalarPathReflectionWrapper"
        } else {
            if (nullable) {
                type.className() + "_NullablePathReflectionWrapper"
            } else {
                type.className() + "_PathReflectionWrapper"
            }
        }
    }

    private fun getWrapperFQName(type: KSType, nullable: Boolean): String {
        return if (type.isScalar()) {
            "ScalarPathReflectionWrapper"
        } else {
            if (nullable) {
                type.classFQName() + "_NullablePathReflectionWrapper"
            } else {
                type.classFQName() + "_PathReflectionWrapper"
            }
        }
    }

    private fun getWrapperFQType(type: KSType, nullable: Boolean): String {
        return if (type.isScalar()) {
            getWrapperFQName(type, nullable) + "<${type.typeFQName()}>"
        } else {
            getWrapperFQName(type, nullable)
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
            CollectionType.LIST -> "PathReflectionList"
            CollectionType.STRING -> "PathReflectionMap"
        }

        val itemType =
            requireNotNull(classProperty.type.innerArguments.last().type?.resolve()) { "Unable to resolve type of list ${classProperty.name} in $name" }

        val itemNId = nullableIdentifier(nullable)
        val defaultInitialization = if (nullable) {
            " ?: $defaultInitializer"
        } else {
            ""
        }

        val wrapperFQType = getWrapperFQType(itemType, true)
        indentBlock("val ${classProperty.name}: $recorderClassImpl<${itemType.typeFQName()}, $wrapperFQType> by lazy") {
            indentBlock(recorderClassImpl, enclosingCharacter = "(") {
                appendln("value()$itemNId.${classProperty.name}$defaultInitialization,")
                appendln("path() + ${StringNamePathIdentifier(classProperty)},")
                appendln("::${getWrapperName(itemType, true)}")
            }
        }
    }

    private fun CodeBuilder.createOverrides() {
        appendDocumentation(
            """
                @returns value stored in wrapper node. Null in case value is null or any previous element was null.
            """.trimIndent()
        )
        appendln("override fun value() = __value")
        emptyLine()
        appendDocumentation(
            """
                @returns [io.github.virelion.buildata.path.RecordedPath] even for accessed element
            """.trimIndent()
        )
        appendln("override fun path() = __path")
    }

    private fun CodeBuilder.createExtensionWithPathMethod() {
        appendDocumentation(
            """
                Wrap item in [io.github.virelion.buildata.path.PathReflectionWrapper]. This will treat this item as a root of accessed elements.
                
                @returns [io.github.virelion.buildata.path.PathReflectionWrapper] implementation for this item,
            """.trimIndent()
        )
        appendln("fun $originalName.withPath() = $name(this, RecordedPath())")
    }

    private fun CodeBuilder.createExtensionPathMethod() {
        appendDocumentation(
            """
                Creates path builder for [io.github.virelion.buildata.path.RecordedPath] creation 
                with [$pkg.$originalName] as data root. 
                Value of every node will be always null.
                
                @returns path builder
            """.trimIndent()
        )
        appendln("fun KClass<$originalName>.path() = $nullableWrapperName(null, RecordedPath())")
    }
}

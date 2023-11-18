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
package io.github.virelion.buildata.ksp

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueParameter
import io.github.virelion.buildata.ksp.extensions.classFQName
import io.github.virelion.buildata.ksp.extensions.className
import io.github.virelion.buildata.ksp.extensions.pkg
import io.github.virelion.buildata.ksp.extensions.typeFQName
import io.github.virelion.buildata.ksp.utils.CodeBuilder

class ClassProperty(
    val name: String,
    val type: KSType,
    val nullable: Boolean,
    val buildable: Boolean,
    val pathReflection: Boolean,
    val classProperty: KSPropertyDeclaration?,
    val constructorParameter: KSValueParameter
) {
    val annotations: Sequence<KSAnnotation> get() {
        return constructorParameter.annotations +
            (classProperty?.annotations ?: emptySequence()) +
            (classProperty?.getter?.annotations ?: emptySequence()) +
            (classProperty?.setter?.annotations ?: emptySequence())
    }

    val hasDefaultValue: Boolean get() = constructorParameter.hasDefault
    val backingPropName = "${name}_Element"

    fun generatePropertyDeclaration(codeBuilder: CodeBuilder) {
        codeBuilder.build {
            if (buildable) {
                val builderName = type.pkg() + "." + BuilderClassTemplate.createBuilderName(type.className())
                val elementPropName = if (nullable) {
                    "BuilderNullableCompositeElementProperty"
                } else {
                    "BuilderCompositeElementProperty"
                }
                appendln("private val $backingPropName = $elementPropName<${type.classFQName()}, $builderName> { $builderName() }")
                appendln("var $name by $backingPropName")
                appendln("@BuildataDSL")
                indentBlock("fun $name(block: $builderName.() -> Unit)") {
                    appendln("$backingPropName.useBuilder(block)")
                }
            } else {
                appendln("private val $backingPropName = BuilderElementProperty<${type.typeFQName()}>()")
                appendln("var $name by $backingPropName")
            }
        }
    }

    fun generateDataClassInitialization(codeBuilder: CodeBuilder) {
        codeBuilder.build {
            appendln("$name = $name,")
        }
    }

    fun generatePopulateWithLine(codeBuilder: CodeBuilder) {
        codeBuilder.build {
            appendln("$name = it.$name")
        }
    }

    fun generateDirectAccessLine(): String {
        return if (buildable) {
            if (nullable) {
                "if($backingPropName.setToNull) null else $backingPropName.builder"
            } else {
                "$backingPropName.builder"
            }
        } else {
            "$backingPropName.container"
        }
    }
}

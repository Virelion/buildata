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
package io.github.virelion.buildata.ksp.utils

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeAlias
import io.github.virelion.buildata.ksp.extensions.classFQName
import io.github.virelion.buildata.ksp.extensions.className
import io.github.virelion.buildata.ksp.utils.TypeConstants.LIST_TYPES
import io.github.virelion.buildata.ksp.utils.TypeConstants.MAP_TYPES
import io.github.virelion.buildata.ksp.utils.TypeConstants.SCALARS

object TypeConstants {
    val SCALARS = setOf(
        "String",
        "Unit",
        "Boolean",
        "Int",
        "UInt",
        "Long",
        "ULong",
        "UByte",
        "Byte",
        "Short",
        "UShort",
        "Float",
        "Double",
        "Any"
    )

    val LIST_TYPES = setOf(
        "kotlin.collections.List",
        "java.util.List",
        "kotlin.collections.MutableList"
    )

    val MAP_TYPES = setOf(
        "kotlin.collections.Map",
        "kotlin.collections.MutableMap",
        "java.util.Map"
    )
}

fun KSType.isScalar(): Boolean {
    return this.className() in SCALARS
}

fun nullableIdentifier(nullable: Boolean) = if (nullable) "?" else ""

fun KSType.isList(): Boolean {
    return LIST_TYPES
        .map { isClassOrChildOfClass(it) }
        .reduce { prev, current -> prev || current }
}

fun KSType.isArray(): Boolean {
    return this.classFQName() == "kotlin.Array"
}

fun KSType.isMap(): Boolean {
    return MAP_TYPES
        .map { isClassOrChildOfClass(it) }
        .reduce { prev, current -> prev || current }
}

fun KSType.isClassOrChildOfClass(fqClassName: String): Boolean {
    var declaration = this.declaration

    if (declaration is KSTypeAlias) {
        declaration = declaration.type.resolve().declaration
    }

    if (declaration !is KSClassDeclaration) return false

    if (fqClassName == this.classFQName()) return true

    return declaration.superTypes
        .map { it.resolve().classFQName() }
        .contains(fqClassName)
}

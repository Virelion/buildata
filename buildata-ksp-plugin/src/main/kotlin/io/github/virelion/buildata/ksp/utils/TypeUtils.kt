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

import com.google.devtools.ksp.innerArguments
import com.google.devtools.ksp.symbol.KSType
import io.github.virelion.buildata.ksp.extensions.classFQName
import io.github.virelion.buildata.ksp.extensions.className
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
}

fun KSType.isScalar(): Boolean {
    return this.className() in SCALARS
}

fun nullableIdentifier(nullable: Boolean) = if (nullable) "?" else ""

fun KSType.isList(): Boolean {
    return "kotlin.collections.List" == this.classFQName()
}

fun KSType.isMap(): Boolean {
    return "kotlin.collections.Map" == this.classFQName()
}

fun KSType.isMapWithStringKey(): Boolean {
    return isMap() && this.innerArguments.first().type?.resolve()?.classFQName() == "kotlin.String"
}

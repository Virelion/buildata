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
package io.github.virelion.buildata.ksp.extensions

import com.google.devtools.ksp.innerArguments
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Nullability

fun KSType.typeFQName(): String {
    val genericTypes = if (this.innerArguments.isNotEmpty()) {
        this.innerArguments.mapNotNull { it.type?.resolve()?.typeFQName() }
            .joinToString(prefix = "<", postfix = ">", separator = ", ")
    } else ""
    return "${declaration.qualifiedName!!.getQualifier()}.${className()}$genericTypes${nullability.toCode()}"
}

fun KSType.classFQName(): String {
    return listOf(declaration.qualifiedName?.getQualifier() ?: "", className()).joinToString(separator = ".")
}

fun KSType.className(): String {
    return this.declaration.qualifiedName?.getShortName() ?: ""
}

fun KSType.pkg(): String {
    return declaration.qualifiedName?.getQualifier() ?: ""
}

fun KSType.typeForDocumentation(): String {
    return "[${declaration.qualifiedName?.getQualifier() ?: ""}.${className()}]${nullability.toCode()}"
}

val KSClassDeclaration.printableFqName: String
    get() {
        return this.packageName.asString() + "." + this.simpleName.getShortName()
    }

fun Nullability.toCode(): String {
    return when (this) {
        Nullability.NULLABLE -> "?"
        Nullability.NOT_NULL -> ""
        Nullability.PLATFORM -> "?"
    }
}

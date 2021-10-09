package io.github.virelion.buildata.ksp.utils

import com.google.devtools.ksp.innerArguments
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
    return this.classFQName() == ""
}

fun KSType.isMap(): Boolean {
    return MAP_TYPES
            .map { isClassOrChildOfClass(it) }
            .reduce { prev, current -> prev || current }
}

fun KSType.isClassOrChildOfClass(fqClassName: String): Boolean {
    var declaration = this.declaration

    if(declaration is KSTypeAlias) {
        declaration = declaration.type.resolve().declaration
    }

    if (declaration !is KSClassDeclaration) return false

    if(fqClassName == this.classFQName()) return true

    return declaration.superTypes
        .map { it.resolve().classFQName() }
        .contains(fqClassName)
}

fun KSType.isMapWithStringKey(): Boolean {
    return isMap() && this.innerArguments.first().type?.resolve()?.classFQName() == "kotlin.String"
}

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

fun nullableIdentifier(nullable: Boolean) = if(nullable) "?" else ""

fun KSType.isList(): Boolean {
    return "kotlin.collections.List" == this.classFQName()
}

fun KSType.isMap(): Boolean {
    return "kotlin.collections.Map" == this.classFQName()
}

fun KSType.isMapWithStringKey(): Boolean {
    return isMap() && this.innerArguments.first().type?.resolve()?.classFQName() == "kotlin.String"
}
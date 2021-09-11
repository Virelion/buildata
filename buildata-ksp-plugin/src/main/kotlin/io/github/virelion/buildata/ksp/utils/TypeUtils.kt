package io.github.virelion.buildata.ksp.utils

import com.google.devtools.ksp.symbol.KSType
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
    )
}

fun KSType.isScalar(): Boolean {
    return this.className() in SCALARS
}

fun nullableIdentifier(nullable: Boolean) = if(nullable) "?" else ""
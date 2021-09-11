package io.github.virelion.buildata.ksp.extensions

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Nullability

fun KSType.typeFQName(): String {
    return "${declaration.qualifiedName!!.getQualifier()}.${className()}${nullability.toCode()}"
}

fun KSType.classFQName(): String {
    return "${declaration.qualifiedName!!.getQualifier()}.${className()}"
}

fun KSType.className(): String {
    return this.declaration.qualifiedName!!.getShortName()
}

fun KSType.typeForDocumentation(): String {
    return "[${declaration.qualifiedName!!.getQualifier()}.${className()}]${nullability.toCode()}"
}

val KSClassDeclaration.printableFqName: String get() {
    return this.packageName.asString() + "." + this.simpleName.getShortName()
}

fun Nullability.toCode(): String {
    return when (this) {
        Nullability.NULLABLE -> "?"
        Nullability.NOT_NULL -> ""
        Nullability.PLATFORM -> "?"
    }
}

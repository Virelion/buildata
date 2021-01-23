package com.github.virelion.buildata.ksp.extensions

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType

fun KSType.typeFQName(): String {
    return "${this.declaration.qualifiedName!!.getQualifier()}.$this"
}

fun KSType.classFQName(): String {
    return this.declaration.qualifiedName!!.getShortName()
}

val KSClassDeclaration.printableFqName: String get() {
    return this.packageName.asString() + "." + this.simpleName.getShortName()
}

package com.github.virelion.buildata.ksp.extensions

import com.google.devtools.ksp.symbol.KSType

fun KSType.typeFQName(): String {
    return "${this.declaration.qualifiedName!!.getQualifier()}.$this"
}

fun KSType.classFQName(): String {
    return this.declaration.qualifiedName!!.getShortName()
}
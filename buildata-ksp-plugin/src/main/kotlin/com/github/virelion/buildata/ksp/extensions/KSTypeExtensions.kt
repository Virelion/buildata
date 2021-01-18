package com.github.virelion.buildata.ksp.extensions

import com.google.devtools.ksp.symbol.KSType

fun KSType.toFQName(): String {
    return "${this.declaration.qualifiedName!!.getQualifier()}.$this"
}
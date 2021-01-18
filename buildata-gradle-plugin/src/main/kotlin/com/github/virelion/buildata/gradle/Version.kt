package com.github.virelion.buildata.gradle

import org.jetbrains.kotlin.gradle.utils.loadPropertyFromResources

object Version {
    val value: String by lazy {
        loadPropertyFromResources("build.properties", "version")
    }
}

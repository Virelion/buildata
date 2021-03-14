package io.github.virelion.buildata.gradle

enum class ProjectType {
    MULTIPLATFORM, JVM
}

fun supportedProjectTypes(): String = ProjectType.values().map { it.name }.joinToString(separator = ", ")

plugins {
    kotlin("jvm")
    id("org.jlleitschuh.gradle.ktlint")
    id("io.github.virelion.buildata")
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
}

val buildataRuntimeVersion = "0.0.0-SNAPSHOT"

dependencies {
    implementation("io.github.virelion:buildata-runtime:$buildataRuntimeVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")

    testImplementation(kotlin("test-junit"))
}

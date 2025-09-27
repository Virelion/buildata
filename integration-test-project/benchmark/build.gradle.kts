plugins {
    kotlin("jvm")
    id("me.champeau.jmh") version "0.7.2"
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
    implementation(project(":buildata-runtime"))
    testImplementation(kotlin("test-junit"))
}

plugins {
    kotlin("jvm")
    id("me.champeau.jmh") version "0.6.2"
    id("org.jlleitschuh.gradle.ktlint")
    id("io.github.virelion.buildata")
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    google()
}

val buildataRuntimeVersion = "0.0.0-SNAPSHOT"

dependencies {
    implementation("io.github.virelion:buildata-runtime:$buildataRuntimeVersion")

    testImplementation(kotlin("test-junit"))
}

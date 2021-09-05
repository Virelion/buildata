plugins {
    kotlin("jvm")
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

plugins {
    kotlin("jvm")
    id("me.champeau.jmh") version "0.7.3"
    id("org.jlleitschuh.gradle.ktlint")
    id("io.github.virelion.buildata")
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
}

dependencies {
    implementation(project(":buildata-runtime"))
    testImplementation(kotlin("test-junit"))
}

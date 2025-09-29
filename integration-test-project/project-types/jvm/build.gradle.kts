plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp") version "2.2.20-2.0.3"
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
}

dependencies {
    implementation(project(":buildata-runtime"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    testImplementation(kotlin("test-junit"))

    add("ksp", project(":buildata-ksp-plugin"))
}
plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp") version "2.3.11"
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
}

dependencies {
    implementation(project(":buildata-runtime"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.22.2")
    testImplementation(kotlin("test-junit"))

    add("ksp", project(":buildata-ksp-plugin"))
}
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm")
    kotlin("kapt")
    `maven-publish`
    id("org.jlleitschuh.gradle.ktlint")
    signing
}

description = "KSP codegen extension that generate builder code for annotated classes."

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

val kspVersion: String by project

dependencies {
    implementation(kotlin("compiler-embeddable"))
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
    compileOnly("com.google.auto.service:auto-service:1.1.1")
    kapt("com.google.auto.service:auto-service:1.1.1")

    testImplementation(kotlin("test-junit"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    withJavadocJar()
    withSourcesJar()
}

kotlin {
    compilerOptions {
        this.jvmTarget = JvmTarget.JVM_11
    }
}

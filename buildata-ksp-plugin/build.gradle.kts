import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("kapt")
    `maven-publish`
    id("org.jlleitschuh.gradle.ktlint")
}

description = "KSP codegen extension that generate builder code for annotated classes."

repositories {
    mavenLocal()
    mavenCentral()
}

val kspVersion: String by project

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("compiler-embeddable"))
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
    compileOnly("com.google.auto.service:auto-service:1.0-rc6")
    kapt("com.google.auto.service:auto-service:1.0-rc6")

    testImplementation(kotlin("test-junit"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withJavadocJar()
    withSourcesJar()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

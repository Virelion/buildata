buildscript {
    repositories {
        gradlePluginPortal()
        jcenter()
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.6.2")
    }
}

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

sourceSets["main"].withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
    kotlin.srcDir(project.rootDir.absolutePath + "/src/commonMain/kotlin")
}

sourceSets["test"].withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
    kotlin.srcDir(project.rootDir.absolutePath + "/src/commonTest/kotlin")
}

dependencies {
    implementation("io.github.virelion:buildata-runtime:$buildataRuntimeVersion")

    testImplementation(kotlin("test-junit"))
}

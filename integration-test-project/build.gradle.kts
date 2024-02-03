buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.2")
    }
}

plugins {
    val kotlinVersion = "1.9.20"
    kotlin("multiplatform") version kotlinVersion apply false
    kotlin("jvm") version kotlinVersion apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
    id("io.github.virelion.buildata") version "0.0.0-SNAPSHOT" apply false
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
}

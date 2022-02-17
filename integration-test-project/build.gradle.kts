buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.2")
    }
}

plugins {
    val kotlinVersion = "1.6.10"
    kotlin("multiplatform") version kotlinVersion apply false
    kotlin("jvm") version kotlinVersion apply false
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    id("io.github.virelion.buildata") version "0.0.0-SNAPSHOT" apply false
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
}

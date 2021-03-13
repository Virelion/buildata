buildscript {
    repositories {
        gradlePluginPortal()
        jcenter()
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.3")
    }
}

plugins {
    val kotlinVersion = "1.4.32"
    kotlin("multiplatform") version kotlinVersion apply false
    kotlin("jvm") version kotlinVersion apply false
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
    id("io.github.virelion.buildata") version "0.0.0-SNAPSHOT" apply false
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    google()
}

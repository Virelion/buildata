apply(from = "$rootDir/gradle/pom.gradle.kts")

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.2")
    }
}

plugins {
    kotlin("multiplatform") version "2.2.20" apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1" apply false
    id("nebula.release") version "18.0.4"
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}


val configurePOM: ((MavenPublication, Project) -> Unit) by extra

nexusPublishing {
    repositories {
        sonatype {
            // Sonatype Central / OSSRH Staging API
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))

            // Resolve credentials from project properties (or environment variables)
            username.set(providers.gradleProperty("sonatype.username"))
            password.set(providers.gradleProperty("sonatype.password"))
        }
    }
}
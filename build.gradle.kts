apply(from = "$rootDir/gradle/pom.gradle.kts")

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.13.0")
    }
}

plugins {
    kotlin("multiplatform") version "2.2.20" apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1" apply false
    id("nebula.release") version "18.0.4"
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

subprojects {
    afterEvaluate {
        if (this.plugins.hasPlugin("maven-publish")) {
            configureMavenCentralRepository()
        }
    }
}

val configurePOM: ((MavenPublication, Project) -> Unit) by extra

fun Project.configureMavenCentralRepository() {
    configure<PublishingExtension> {
        repositories {
            maven {
                name = "ossrh-staging-api"
                url = uri("https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/")
                credentials {
                    username = project.findProperty("sonatype.username") as? String
                    password = project.findProperty("sonatype.password") as? String
                }
            }
        }
    }
}
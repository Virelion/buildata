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
            // Your Sonatype group ID / namespace
            packageGroup.set("io.github.virelion")

            // Staging profile ID (matches package group for OSSRH)
            stagingProfileId.set("io.github.virelion")

            // OSSRH Staging Endpoints
            // Note: If your Sonatype account was created after Feb 2021,
            // change 'ossrh-staging-api.central.sonatype.com' to 's01.oss.sonatype.org'
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))

            // Resolves credentials passed via -Psonatype.username and -Psonatype.password
            username.set(providers.gradleProperty("sonatype.username"))
            password.set(providers.gradleProperty("sonatype.password"))
        }
    }
}
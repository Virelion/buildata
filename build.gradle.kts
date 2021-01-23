buildscript {
    repositories {
        mavenLocal()
        jcenter()
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.6.2")
    }
}

plugins {
    kotlin("multiplatform") version "1.4.21" apply false
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1" apply false
    id("com.gradle.plugin-publish") version "0.12.0" apply false
    id("nebula.release") version "13.0.0"
}

allprojects {
    repositories {
        mavenCentral()
        jcenter()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        google()
    }
}

subprojects {
    if(!this.plugins.hasPlugin("maven-publish")) {
        apply(plugin = "maven-publish")
    }
    configure<PublishingExtension>  {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/Virelion/buildata")
                credentials {
                    username = System.getProperty("org.ajoberstar.grgit.auth.username") as? String ?: System.getenv("USERNAME")
                    password = System.getProperty("org.ajoberstar.grgit.auth.password") as? String ?: System.getenv("TOKEN")
                }
            }
        }
    }
}


tasks {
    val publishPluginsToMavenLocal by creating {
        dependsOn(
                ":buildata-ksp-plugin:publishToMavenLocal",
                ":buildata-gradle-plugin:publishToMavenLocal",
                ":buildata-runtime:publishJvmPublicationToMavenLocal"
        )
    }
}

apply(from = "$rootDir/gradle/pom.gradle.kts")

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.2")
    }
}

plugins {
    kotlin("multiplatform") version "1.6.10" apply false
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1" apply false
    id("com.gradle.plugin-publish") version "0.12.0" apply false
    id("nebula.release") version "13.0.0"
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

subprojects {
    if (!this.plugins.hasPlugin("maven-publish")) {
        apply(plugin = "maven-publish")
    }
    val isGradlePlugin = this.name == "buildata-gradle-plugin"
    logger.info("Configuring $name as ${if (!isGradlePlugin) "mavenCentral module" else "gradle plugin"}")
    if (!isGradlePlugin) {
        configureMavenCentralRepository()
    }
}

val configurePOM: ((MavenPublication, Project) -> Unit) by extra

fun Project.configureMavenCentralRepository() {
    configure<PublishingExtension> {
        repositories {
            maven {
                name = "MavenCentralReleaseStaging"
                url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2")
                credentials {
                    username = project.findProperty("nexus.username") as? String
                    password = project.findProperty("nexus.token") as? String
                }
            }
        }
        afterEvaluate {
            publications {
                filterIsInstance<MavenPublication>()
                        .forEach {
                            configurePOM(it, this@afterEvaluate)
                        }
            }

            val isReleasingWithSigning: Boolean = (findProperty("isReleasingWithSigning") as? String)?.toBoolean() ?: false
            if(isReleasingWithSigning) {
                val signingKey = System.getenv("GPG_SECRET_KEY") ?: error("Missing signing.secretKey")
                val signingPassword = System.getenv("GPG_SECRET_PASSWORD") ?: error("Missing signing.password")
                configure<SigningExtension> {
                    useInMemoryPgpKeys(signingKey, signingPassword)
                    sign(publications)
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

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
    if (!this.plugins.hasPlugin("maven-publish")) {
        apply(plugin = "maven-publish")
    }
    val isGradlePlugin = this.name == "buildata-gradle-plugin"
    logger.info("Configuring $name as ${if (!isGradlePlugin) "mavenCentral module" else "gradle plugin"}")
    if (!isGradlePlugin) {
        configureMavenCentralRepository()
    }
}

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
                            it.pom {
                                name.set("${it.groupId}:${it.artifactId}")
                                url.set("https://github.com/Virelion/buildata")
                                description.set(this@afterEvaluate.description)
                                inceptionYear.set("2021")
                                licenses {
                                    license {
                                        name.set("The Apache License, Version 2.0")
                                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                                    }
                                }
                                developers {
                                    developer {
                                        id.set("Virelion")
                                        name.set("Maciej Ziemba")
                                        email.set("m.ziemba95@gmail.com")
                                    }
                                }
                                scm {
                                    val projectPath = "Virelion/buildata"
                                    connection.set("scm:git:git://github.com/${projectPath}.git")
                                    developerConnection.set("scm:git:ssh://github.com:${projectPath}.git")
                                    url.set("https://github.com/${projectPath}/tree/master")
                                }
                            }
                        }
            }

            val isReleasing: Boolean = (findProperty("isReleasing") as? String)?.toBoolean() ?: false
            if(isReleasing) {
                val signingKey = findProperty("signing.secretKey") as? String ?: error("Missing signing.secretKey")
                val signingPassword = findProperty("signing.password") as? String ?: error("Missing signing.password")
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

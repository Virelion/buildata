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
                    username = System.getProperty("org.ajoberstar.grgit.auth.username") ?: System.getenv("USERNAME")
                    password = System.getProperty("org.ajoberstar.grgit.auth.password") ?: System.getenv("TOKEN")
                }
            }
        }
        afterEvaluate {
            publications {
                this.apply {
                    println("#########################################")
                    forEach {
                        println(it::class.java.simpleName)
                    }
                }
                        .filterIsInstance<MavenPublication>()
                        .forEach {
                            println(it.name)
                            println(it.artifactId)
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

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

buildscript {
    repositories {
        mavenCentral()
        google()
    }
}

plugins {
    id("com.android.kotlin.multiplatform.library")
    kotlin("multiplatform")
    `maven-publish`
    id("org.jlleitschuh.gradle.ktlint")
    id("org.jetbrains.dokka") version "2.0.0"
    signing
}

description = "Buildata runtime for generated builders."

val linuxTargetEnabled = project.findProperty("kotlin.native.linux.enabled") ?: "true" == "true"

fun hasAndroidSdk(project: org.gradle.api.Project): Boolean {
    val localProps = project.rootProject.file("local.properties")
    if (localProps.exists()) {
        val props = project.rootProject.extensions.extraProperties
        val sdkDir = localProps.bufferedReader().useLines { lines ->
            lines.find { it.startsWith("sdk.dir=") }?.substringAfter("sdk.dir=")?.trim()
        }
        if (!sdkDir.isNullOrEmpty()) return true
    }
    return System.getenv("ANDROID_HOME") != null
}

val androidEnabled = hasAndroidSdk(project)
    .also { if (!it) logger.error("Android is not enabled for project ANDROID_HOME is empty") }

repositories {
    mavenLocal()
    mavenCentral()
    google()
}

var publicationsFromMainHost = listOf<String>()

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
    }

    jvm {
        this.compilerOptions.jvmTarget = JvmTarget.JVM_11
    }

    js {
        nodejs {
            testTask {
                useMocha()
            }
        }
    }

    mingwX64()
    macosArm64()

    if (linuxTargetEnabled) {
        linuxX64()
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    val mainHostPublications = buildList {
        addAll(listOf(jvm(), js()).map { it.name })
        add("kotlinMultiplatform")
        add("metadata")
        // Android variant publication names are resolved dynamically at publish time.
    }

    publicationsFromMainHost = mainHostPublications

    android {
        namespace = "io.github.virelion"
        compileSdk { version = release(34) }
        withHostTest {}
    }

    sourceSets {
        commonMain {
            dependencies {
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain = sourceSets.getByName("jvmMain") {
            dependencies {
            }
        }

        val jvmTest = sourceSets.getByName("jvmTest") {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }

        val jsTest = sourceSets.getByName("jsTest") {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        if (androidEnabled) {
            // Android sources are automatically picked up by the KMP library plugin.
            // No manual source set wiring needed for commonAndroidMain/commonAndroidTest.
        }
    }
}

publishing {
    publications {
        matching { it.name in publicationsFromMainHost }.all {
            val targetPublication = this@all
            tasks.withType<AbstractPublishToMaven>()
                .matching { it.publication == targetPublication }
                .configureEach { onlyIf { findProperty("isMainHost") == "true" } }
        }
    }
}

tasks {
    withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
        outputDirectory.set(layout.buildDirectory.file("javadoc").get().asFile)
    }

    val javadocJar = register<org.gradle.jvm.tasks.Jar>("javadocJar") {
        archiveClassifier.set("javadoc")
        from(layout.buildDirectory.file("javadoc"))
    }

    artifacts {
        archives(javadocJar)
    }
}

afterEvaluate {
    publishing {
        publications {
            this.getByName<MavenPublication>("jvm") {
                artifact(tasks["javadocJar"])
            }
        }
    }
}

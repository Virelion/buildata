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
    id("org.jetbrains.dokka") version "1.9.20"
    signing
}

description = "Buildata runtime for generated builders."

val linuxTargetEnabled = project.findProperty("kotlin.native.linux.enabled") ?: "true" == "true"

// Resolve Android SDK: prefer ANDROID_HOME env var, fallback to local.properties sdk.dir
fun resolveAndroidSdk(): String? {
    System.getenv("ANDROID_HOME")?.let { return@resolveAndroidSdk it }
    val lp = file("${rootProject.rootDir}/local.properties")
    if (!lp.exists()) return null
    return lp.readLines().mapNotNull { line ->
        line.split("=").takeIf { it.size == 2 }?.let { it[0].trim() to it[1].trim() }
    }.find { (key, _) -> key == "sdk.dir" }?.second
}

val resolvedAndroidHome = resolveAndroidSdk()
val androidEnabled = (resolvedAndroidHome != null)
    .also { if (!it) logger.error("Android is not enabled for project ANDROID_HOME/local.properties sdk.dir is empty") }

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

    if (linuxTargetEnabled) {
        linuxX64()
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    publicationsFromMainHost =
        listOf(jvm(), js())
        .map { it.name } + "kotlinMultiplatform" + "androidDebug" + "androidRelease" + "metadata"

    android {
        namespace = "io.github.virelion"
        compileSdk { version = release(34) }
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
//            val androidMain by getting {
//                dependencies {
//                }
//            }
//
//            val androidUnitTest by getting {
//                dependencies {
//                    implementation(kotlin("test"))
//                    implementation(kotlin("test-junit"))
//                }
//            }
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
        dokkaSourceSets {
            configureEach {
                if (platform.get() == org.jetbrains.dokka.Platform.native) {
                    displayName.set("native")
                }
            }
        }
    }

    val javadocJar = register("javadocJar", org.gradle.jvm.tasks.Jar::class) {
        dependsOn(dokkaHtml)
        archiveClassifier.set("javadoc")
        from(dokkaHtml.get().outputDirectory)
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

            if (androidEnabled) {
                apply(from = "$rootDir/gradle/pom.gradle.kts")
                val configurePOM = extra["configurePOM"] as ((MavenPublication, Project) -> Unit)

//                this.getByName<MavenPublication>("androidRelease") {
//                    configurePOM(this, project)
//                }
//
//                this.getByName<MavenPublication>("androidDebug") {
//                    configurePOM(this, project)
//                }
            }
        }
    }
}

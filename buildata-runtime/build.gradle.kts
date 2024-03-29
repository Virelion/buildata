import com.android.build.gradle.LibraryExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
        google()
    }
}

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    `maven-publish`
    id("org.jlleitschuh.gradle.ktlint")
    id("org.jetbrains.dokka") version "1.9.10"
    signing
}

description = "Buildata runtime for generated builders."

val linuxTargetEnabled = project.findProperty("kotlin.native.linux.enabled") ?: "true" == "true"

val androidEnabled = System.getenv("ANDROID_HOME") != null
if (androidEnabled) {
    configureAndroid()
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
}

kotlin {
    jvm {}
    js(IR) {
        nodejs {
            testTask {
                useMocha()
            }
        }
    }
    mingwX64()
    macosX64()

    if (linuxTargetEnabled) {
        linuxX64()
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()
    val publicationsFromMainHost =
        listOf(jvm(), js())
            .map { it.name } + "kotlinMultiplatform" + "androidDebug" + "androidRelease" + "metadata"
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

    if (androidEnabled) {
        android {
            publishLibraryVariants("release", "debug")
        }
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

        val jvmMain by getting {
            dependencies {
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        if (androidEnabled) {
            val androidMain by getting {
                dependencies {
                }
            }

            val androidUnitTest by getting {
                dependencies {
                    implementation(kotlin("test"))
                    implementation(kotlin("test-junit"))
                }
            }
        }
    }
}

tasks {
    withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + listOf("-opt-in=kotlin.RequiresOptIn")
        }
    }

    withType(org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile::class).all {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + listOf("-opt-in=kotlin.RequiresOptIn")
        }
    }

    withType(org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile::class).all {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + listOf("-opt-in=kotlin.RequiresOptIn")
        }
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
        outputDirectory.set(file("$buildDir/javadoc"))
        dokkaSourceSets {
            configureEach {
                if (platform.get() == org.jetbrains.dokka.Platform.native) {
                    displayName.set("native")
                }
            }
        }
    }

    val javadocJar by registering(org.gradle.jvm.tasks.Jar::class) {
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

            // Android targets require additional config for some reason
            if (androidEnabled) {
                apply(from = "$rootDir/gradle/pom.gradle.kts")
                val configurePOM: ((MavenPublication, Project) -> Unit) by extra

                this.getByName<MavenPublication>("androidRelease") {
                    configurePOM(this, project)
                }

                this.getByName<MavenPublication>("androidDebug") {
                    configurePOM(this, project)
                }
            }
        }
    }
}

fun Project.configureAndroid() {
    configure<LibraryExtension> {
        namespace = "io.github.virelion"
        compileSdkVersion = "android-29"

        defaultConfig {
            minSdk = 21
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        sourceSets.getByName("main").apply {
            java.srcDirs("src/androidMain/kotlin")
            res.srcDirs("src/androidMain/res")
            assets.srcDirs("src/commonMain/resources/assets")
        }

        sourceSets.getByName("androidTest").apply {
            java.srcDirs("src/commonTest/kotlin", "src/jvmTest/kotlin")
            res.srcDirs("src/androidTest/res")
            assets.srcDirs("src/commonMain/resources/assets")
        }

        defaultPublishConfig = "debug"
        testOptions.unitTests.isIncludeAndroidResources = true
    }
}

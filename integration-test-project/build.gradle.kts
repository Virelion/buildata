import com.android.build.gradle.AppExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        gradlePluginPortal()
        jcenter()
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.6.2")
    }
}

plugins {
    kotlin("multiplatform") version "1.4.21"
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
    id("io.github.virelion.buildata") version "0.0.0-SNAPSHOT"
}

val androidEnabled = System.getenv("ANDROID_HOME") != null
if (androidEnabled) {
    configureAndroid()
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    google()
}

val buildataRuntimeVersion = "0.0.0-SNAPSHOT"

kotlin {
    jvm()
    js {
        nodejs {
            testTask {
                useMocha()
            }
        }
    }
    mingwX64()
    macosX64()
    linuxX64()
    ios()
    if (androidEnabled) {
        android()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.github.virelion.buildata:buildata-runtime:$buildataRuntimeVersion")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(kotlin("reflect"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }

        val jsMain by getting {
            dependencies {
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        val nativeMain by creating {
            dependencies {
            }
        }

        val linuxX64Main by getting {
            dependsOn(nativeMain)
            dependencies {
            }
        }

        val macosX64Main by getting {
            dependsOn(nativeMain)
            dependencies {
            }
        }

        val iosX64Main by getting {
            dependsOn(nativeMain)
            dependencies {
            }
        }

        val iosArm64Main by getting {
            dependsOn(nativeMain)
            dependencies {
            }
        }

        val mingwX64Main by getting {
            dependsOn(nativeMain)
            dependencies {
            }
        }
        if (androidEnabled) {
            val androidMain by getting {
                dependencies {
                    implementation(kotlin("stdlib"))
                }
            }

            val androidTest by getting {
                dependencies {
                    implementation(kotlin("test"))
                    implementation(kotlin("test-junit"))
                }
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.6"
}

fun Project.configureAndroid() {
    apply(plugin = "com.android.application")

    configure<AppExtension> {
        buildToolsVersion("29.0.2")
        compileSdkVersion(29)

        defaultConfig {
            targetSdkVersion(29)
            minSdkVersion(21)
            versionCode = 1
            versionName = "1.0"
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        sourceSets.getByName("main").apply {
            java.srcDirs("src/commonMain/kotlin")
            res.srcDirs("src/androidMain/res")
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            assets.srcDirs("src/commonMain/resources/assets")
        }

        sourceSets.getByName("androidTest").apply {
            java.srcDirs("src/commonTest/kotlin")
            res.srcDirs("src/androidTest/res")
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            assets.srcDirs("src/commonMain/resources/assets")
        }

        defaultPublishConfig = "debug"
        testOptions.unitTests.isIncludeAndroidResources = true
    }
}

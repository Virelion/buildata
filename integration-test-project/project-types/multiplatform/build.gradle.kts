plugins {
    kotlin("multiplatform")
    id("com.android.kotlin.multiplatform.library")
    id("com.google.devtools.ksp") version "2.3.11"
}

fun resolveAndroidSdk(): String? {
    System.getenv("ANDROID_HOME")?.let { return@resolveAndroidSdk it }
    val lp = file("${rootProject.rootDir}/local.properties")
    if (!lp.exists()) return null
    return lp.readLines().mapNotNull { line ->
        line.split("=").takeIf { it.size == 2 }?.let { it[0].trim() to it[1].trim() }
    }.find { (key, _) -> key == "sdk.dir" }?.second
}

val androidEnabled = (resolveAndroidSdk() != null)

repositories {
    mavenLocal()
    mavenCentral()
    google()
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
    }
    jvm {
        this.compilerOptions.jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
    js {
        nodejs {
            testTask {
                useMocha()
            }
        }
    }
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    when {
        hostOs == "Mac OS X" -> {
            macosArm64()
            iosX64()
            iosArm64()
            iosSimulatorArm64()
        }
        hostOs == "Linux" -> linuxX64()
        isMingwX64 -> mingwX64()
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    android {
        namespace = "io.github.virelion.integrationtest"
        compileSdk { version = release(34) }
    }

    sourceSets {
        commonMain {
            kotlin {
                srcDir("build/generated/ksp/metadata/commonMain/kotlin")
            }
            dependencies {
                implementation(project(":buildata-runtime"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
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
                implementation(kotlin("reflect"))
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
            // Android host tests require 'withHostTest {}' in android target config.
            // Not configured here as they're optional for integration testing.
        }
    }
}

afterEvaluate {
    // Fix Gradle 9.x strictness: KSP multiplatform tasks don't auto-depends on common metadata task
    tasks.matching { it::class.java.simpleName.startsWith("KspAA") && it.name != "kspCommonMainKotlinMetadata" }.configureEach { dependsOn("kspCommonMainKotlinMetadata") }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>>().all {
        if (name != "kspCommonMainKotlinMetadata") {
            dependsOn("kspCommonMainKotlinMetadata")
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", project(":buildata-ksp-plugin"))
}
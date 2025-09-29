import com.android.build.gradle.AppExtension

plugins {
    kotlin("multiplatform")
    id("com.google.devtools.ksp") version "2.2.20-2.0.3"
}

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
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
    }
    jvm {
        this.compilerOptions.jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
    js(IR) {
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
            macosX64()
            iosX64()
            iosArm64()
            iosSimulatorArm64()
        }
        hostOs == "Linux" -> linuxX64()
        isMingwX64 -> mingwX64()
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    if (androidEnabled) {
        androidTarget()
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

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
        if (androidEnabled) {
            val androidUnitTest by getting {
                dependencies {
                    implementation(kotlin("test"))
                    implementation(kotlin("test-junit"))
                }
            }
        }
    }
}

afterEvaluate {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>>().all {
        if (name != "kspCommonMainKotlinMetadata") {
            dependsOn("kspCommonMainKotlinMetadata")
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", project(":buildata-ksp-plugin"))
}

fun Project.configureAndroid() {
    apply(plugin = "com.android.application")

    configure<AppExtension> {
        namespace = "io.github.virelion.buildata.demo"
        compileSdkVersion = "android-30"

        defaultConfig {
            minSdk = 21
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
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
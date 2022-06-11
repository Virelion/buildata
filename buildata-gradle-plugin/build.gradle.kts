import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("java-gradle-plugin")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.gradle.plugin-publish")
    `maven-publish`
}

description = "Plugin that connects Buildata KSP codegen engine to Kotlin gradle project."

repositories {
    mavenLocal()
    mavenCentral()
}

publishing {
    publications { }
}

gradlePlugin {
    plugins {
        register("BuildataGradlePlugin") {
            displayName = "Buildata Gradle Plugin"
            description = project.description
            id = "io.github.virelion.buildata"
            implementationClass = "io.github.virelion.buildata.gradle.BuildataPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/Virelion/buildata"
    vcsUrl = "https://github.com/Virelion/buildata.git"
    tags = listOf("codegen", "Kotlin", "multiplatform")
}

description = "Plugin that connects Buildata KSP codegen engine to Kotlin gradle project"

val kspVersion: String by project

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("gradle-plugin-api"))
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
    implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:$kspVersion")
    compileOnly("com.google.auto.service:auto-service:1.0.1")
    kapt("com.google.auto.service:auto-service:1.0.1")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Jar>() {
    this.manifest {
        attributes("Implementation-Version" to project.version)
    }
}

(tasks.getByName("processResources") as ProcessResources).apply {
    expand("projectVersion" to project.version)
}

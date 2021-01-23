import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("java-gradle-plugin")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.gradle.plugin-publish")
    `maven-publish`
}

repositories {
    mavenLocal()
    mavenCentral()
}

gradlePlugin {
    plugins {
        register("BuildataGradlePlugin") {
            id = "com.github.virelion.buildata"
            implementationClass = "com.github.virelion.buildata.gradle.BuildataPlugin"
        }
    }
}

val kspVersion: String by project

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("gradle-plugin-api"))
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")
    implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:$kspVersion")
    compileOnly("com.google.auto.service:auto-service:1.0-rc6")
    kapt("com.google.auto.service:auto-service:1.0-rc6")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
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

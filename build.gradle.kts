apply(from = "$rootDir/gradle/pom.gradle.kts")

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.2")
    }
}

plugins {
    kotlin("multiplatform") version "2.2.20" apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1" apply false
    id("nebula.release") version "18.0.4"
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}


val configurePOM: ((MavenPublication, Project) -> Unit) by extra


nexusPublishing {
    repositories {
        sonatype {
            packageGroup.set("io.github.virelion")
            stagingProfileId.set("io.github.virelion")

            // Point directly to the OSSRH compatibility API
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))

            username.set(providers.gradleProperty("sonatype.username"))
            password.set(providers.gradleProperty("sonatype.password"))
        }
    }
}

tasks.register("saveStagingRepositoryID") {
    group = "publishing"
    description = "Saves the staging repository ID to a file"

    // Must run after initialization
    mustRunAfter("initializeSonatypeStagingRepository")

    val outputFile = layout.buildDirectory.file("nexus-staging-plugin/sonatype.properties")
    outputs.file(outputFile)

    doLast {
        // Access the task directly to get the generated ID
        val initTask = tasks.named<io.github.gradlenexus.publishplugin.InitializeNexusStagingRepository>("initializeSonatypeStagingRepository").get()
        val repoId = initTask.registry.get().registry[initTask.repository.get().name].stagingRepositoryId

        initTask.repository.get().name
        // Save to file
        outputFile.get().asFile.writeText("stagingRepositoryId=$repoId")
        println("##[set-output name=REPO_ID;]$repoId") // For CI output
        println(repoId)
    }
}
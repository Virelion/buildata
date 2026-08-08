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
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}


val configurePOM: ((MavenPublication, Project) -> Unit) by extra
val releaseRunID by properties


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

            repositoryDescription.set(repositoryDescription.get()+"#"+releaseRunID)
        }
    }
}

tasks.register("saveBuildInformation") {
    group = "publishing"
    description = "Saves the staging repository ID to a file"

    mustRunAfter("initializeSonatypeStagingRepository")

    val outputFile = layout.buildDirectory.file("build.properties")
    outputs.file(outputFile)

    doLast {
        val initTask = tasks.named<io.github.gradlenexus.publishplugin.InitializeNexusStagingRepository>("initializeSonatypeStagingRepository").get()
        val repoId = initTask.registry.get().registry[initTask.repository.get().name].stagingRepositoryId

        val rawVersion = project.version.toString()

        if("v" in rawVersion) throw GradleException("Incorrect version $rawVersion, version cannot contain 'v'")
        val cleanVersion = rawVersion.removePrefix("v")

        // Ensure releaseTag always retains/adds the 'v' prefix
        val explicitTag = project.findProperty("releaseTag")?.toString()
        val tagWithV = explicitTag ?: "v$cleanVersion"

        val buildProperties = buildString {
            appendLine("stagingRepositoryId=$repoId")
            appendLine("releaseTag=$tagWithV")       // Guaranteed: v1.1.1
            appendLine("releaseVersion=$cleanVersion") // Guaranteed: 1.1.1
        }

        outputFile.get().asFile.writeText(buildProperties)
        println(buildProperties)
    }
}
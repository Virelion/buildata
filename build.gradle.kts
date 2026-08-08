apply(from = "$rootDir/gradle/pom.gradle.kts")

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.5.2")
    }
}

plugins {
    kotlin("multiplatform") version "2.4.10" apply false
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

            repositoryDescription.set(repositoryDescription.get()+"#"+project.findProperty("releaseRunID"))
            logger.info("Sonatype repository description: ${repositoryDescription.get()}")
        }
    }
}

tasks.register("saveBuildInformation") {
    group = "publishing"
    description = "Saves the staging repository ID to a file"

    val outputFile = layout.buildDirectory.file("build.properties")
    outputs.file(outputFile)

    doLast {
        val rawVersion = project.version.toString()

        if("v" in rawVersion) throw GradleException("Incorrect version $rawVersion, version cannot contain 'v'")
        val cleanVersion = rawVersion.removePrefix("v")

        // Ensure releaseTag always retains/adds the 'v' prefix
        val explicitTag = project.findProperty("releaseTag")?.toString()
        val tagWithV = explicitTag ?: "v$cleanVersion"

        val buildProperties = buildString {
            appendLine("releaseTag=$tagWithV")
            appendLine("releaseVersion=$cleanVersion")
        }

        outputFile.get().asFile.writeText(buildProperties)
        println(buildProperties)
    }
}

subprojects {
    pluginManager.withPlugin("maven-publish") {
        if (project.name != "buildata-gradle-plugin") {
            logger.info("Configuring KMP/Maven modules for: ${project.name}")

            configure<PublishingExtension> {
                // withType<MavenPublication> catches JVM, Android, KotlinMultiplatform,
                // and all Native/iOS target publications automatically as they initialize.
                publications.withType<MavenPublication> {
                    configurePOM(this, project)
                }
            }
        }

        configureSigningIfNeeded(project)
    }
}

fun configureSigningIfNeeded(project: Project) {
    val isReleasingWithSigning = (project.findProperty("isReleasingWithSigning") as? String)?.toBoolean() ?: false
    if (isReleasingWithSigning) {
        val signingKey = System.getenv("GPG_SECRET_KEY") ?: error("Missing signing.secretKey")
        val signingPassword = System.getenv("GPG_SECRET_PASSWORD") ?: error("Missing signing.password")

        project.configure<SigningExtension> {
            useInMemoryPgpKeys(signingKey, signingPassword)
            // Signs all publications registered in the project's publishing extension
            sign(project.extensions.getByType<PublishingExtension>().publications)
        }
    }
}

subprojects {
    pluginManager.withPlugin("maven-publish") {
        val isGradlePlugin = this.name == "buildata-gradle-plugin"
        logger.info("Configuring $name as ${if (!isGradlePlugin) "mavenCentral module" else "gradle plugin"}")
        if (!isGradlePlugin) {
            configurePOMAndSigning()
        }
    }
}

fun Project.configurePOMAndSigning() {
    configure<PublishingExtension> {
        afterEvaluate {
            publications {
                filterIsInstance<MavenPublication>()
                    .forEach {
                        configurePOM(it, this@afterEvaluate)
                    }
            }

            val isReleasingWithSigning: Boolean = (findProperty("isReleasingWithSigning") as? String)?.toBoolean() ?: false
            if(isReleasingWithSigning) {
                val signingKey = System.getenv("GPG_SECRET_KEY") ?: error("Missing signing.secretKey")
                val signingPassword = System.getenv("GPG_SECRET_PASSWORD") ?: error("Missing signing.password")
                configure<SigningExtension> {
                    useInMemoryPgpKeys(signingKey, signingPassword)
                    sign(publications)
                }
            }
        }
    }
}
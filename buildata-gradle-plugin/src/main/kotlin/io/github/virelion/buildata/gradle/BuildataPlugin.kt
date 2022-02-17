package io.github.virelion.buildata.gradle

import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.slf4j.LoggerFactory
import java.io.File

class BuildataPlugin : Plugin<Project> {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(BuildataPlugin::class.java)
    }

    lateinit var projectType: ProjectType
        private set

    override fun apply(project: Project) {
        LOGGER.info("Plugin applied")
        project.getProjectType()?.let {
            projectType = it
        }

        if (!this::projectType.isInitialized) {
            LOGGER.error("Supported project type was not found. Disabling Buildata plugin")
            return
        }

        project.plugins.apply("com.google.devtools.ksp")
        LOGGER.info("KSP loaded")

        val kspExtension = project.extensions.getByType(KspExtension::class.java)

        val buildataCodegenDir =
            mutableListOf(project.buildDir.path, "generated", "buildata").apply {
                when (projectType) {
                    ProjectType.MULTIPLATFORM -> this.add("commonMain")
                    ProjectType.JVM -> this.add("main")
                }
            }
                .joinToString(separator = File.separator)

        kspExtension.arg("buildataCodegenDir", buildataCodegenDir)

        LOGGER.info("buildataCodegenDir was set to $buildataCodegenDir")

        LOGGER.info("Plugin 'io.github.virelion:buildata-ksp-plugin:${Version.value}' was added to 'ksp' configuration.")
        project.configurations.getByName("ksp").dependencies.add(
            project.dependencies.create("io.github.virelion:buildata-ksp-plugin:${Version.value}")
        )
        when (projectType) {
            ProjectType.MULTIPLATFORM -> configureMultiplatformPlugin(project, buildataCodegenDir)
            ProjectType.JVM -> configureJVMPlugin(project, buildataCodegenDir)
        }
    }

    fun Project.getProjectType(): ProjectType? {
        return project.plugins.run {
            when {
                this.findPlugin("org.jetbrains.kotlin.multiplatform") != null -> ProjectType.MULTIPLATFORM
                this.findPlugin("org.jetbrains.kotlin.jvm") != null -> ProjectType.JVM
                else -> {
                    LOGGER.error("Unsupported project type. Please use one of following project types: ${supportedProjectTypes()}")
                    null
                }
            }
        }
    }

    fun configureMultiplatformPlugin(project: Project, buildataCodegenDir: String) {
        LOGGER.info("Configuring multiplatform Buildata variant")
        val multiplatformExtension = project.extensions.getByType(KotlinMultiplatformExtension::class.java)

        val commonMain = multiplatformExtension.sourceSets.getByName("commonMain")

        LOGGER.info("Buildata codegen directory: $buildataCodegenDir")
        commonMain.kotlin.srcDir(buildataCodegenDir)

        project.afterEvaluate {
            LOGGER.info("Multiplatform targets: ${multiplatformExtension.targets.joinToString { it.name }}")

            val kspCodegenPlatformTarget =
                multiplatformExtension.targets
                    .firstOrNull { it.platformType == KotlinPlatformType.jvm && it.publishable }
                    ?: multiplatformExtension.targets
                        .firstOrNull { it.platformType == KotlinPlatformType.androidJvm && it.publishable }

            if (kspCodegenPlatformTarget != null) {
                val taskName = "kspKotlin" + kspCodegenPlatformTarget.name.capitalize()

                multiplatformExtension.targets
                    .fold(mutableListOf<KotlinCompilation<*>>()) { acc, next ->
                        acc += next.compilations
                        acc
                    }.forEach {
                        project.tasks.getByName(it.compileKotlinTask.name).dependsOn(taskName)
                    }
            } else {
                LOGGER.error("Buildata cannot generate classes for projects with no jvm or android targets")
            }
        }
    }

    fun configureJVMPlugin(project: Project, buildataCodegenDir: String) {
        LOGGER.info("Configuring JVM Buildata variant")

        val jvmProjectExtension = project.extensions.getByType(KotlinJvmProjectExtension::class.java)

        val main = jvmProjectExtension.sourceSets.getByName("main")

        LOGGER.info("Buildata codegen directory: $buildataCodegenDir")
        main.kotlin.srcDir(buildataCodegenDir)
    }
}

package io.github.virelion.buildata.gradle

import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import java.io.File

class BuildataPlugin : Plugin<Project> {
    lateinit var projectType: ProjectType
        private set

    override fun apply(project: Project) {
        project.plugins.apply("com.google.devtools.ksp")
        val kspExtension = project.extensions.getByType(KspExtension::class.java)
        projectType = project.getProjectType() ?: return

        val buildataCodegenDir =
            mutableListOf(project.buildDir.path, "generated", "buildata").apply {
                when (projectType) {
                    ProjectType.MULTIPLATFORM -> this.add("commonMain")
                    ProjectType.JVM -> this.add("main")
                }
            }
                .joinToString(separator = File.separator)

        kspExtension.arg("buildataCodegenDir", buildataCodegenDir)

        project.afterEvaluate {
            when (projectType) {
                ProjectType.MULTIPLATFORM -> configureMultiplatformPlugin(project, buildataCodegenDir)
                ProjectType.JVM -> configureJVMPlugin(project, buildataCodegenDir)
            }

            project.configurations.getByName("ksp").dependencies.add(
                project.dependencies.create("io.github.virelion:buildata-ksp-plugin:${Version.value}")
            )
        }
    }

    fun Project.getProjectType(): ProjectType? {
        return project.plugins.run {
            when {
                this.findPlugin("org.jetbrains.kotlin.multiplatform") != null -> ProjectType.MULTIPLATFORM
                this.findPlugin("org.jetbrains.kotlin.jvm") != null -> ProjectType.JVM
                else -> {
                    project.logger.error("Unsupported project type. Please use one of following project types: ${supportedProjectTypes()}")
                    null
                }
            }
        }
    }

    fun configureMultiplatformPlugin(project: Project, buildataCodegenDir: String) {
        val multiplatformExtension = project.extensions.getByType(KotlinMultiplatformExtension::class.java)

        val commonMain = multiplatformExtension.sourceSets.getByName("commonMain")

        project.logger.info("Buildata codegen directory: $buildataCodegenDir")
        commonMain.kotlin.srcDir(buildataCodegenDir)
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
            project.logger.error("Buildata cannot generate classes for projects with no jvm or android targets")
        }
    }

    fun configureJVMPlugin(project: Project, buildataCodegenDir: String) {
        val multiplatformExtension = project.extensions.getByType(KotlinJvmProjectExtension::class.java)

        val main = multiplatformExtension.sourceSets.getByName("main")

        project.logger.info("Buildata codegen directory: $buildataCodegenDir")
        main.kotlin.srcDir(buildataCodegenDir)
    }
}

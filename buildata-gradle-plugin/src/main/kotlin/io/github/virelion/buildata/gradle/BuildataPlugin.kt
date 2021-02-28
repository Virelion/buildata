package io.github.virelion.buildata.gradle

import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import java.io.File

class BuildataPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.plugins.apply("com.google.devtools.ksp")
        val kspExtension = project.extensions.getByType(KspExtension::class.java)

        val buildataCodegenDir =
            listOf(project.buildDir.path, "generated", "buildata", "commonMain")
                .joinToString(separator = File.separator)

        kspExtension.arg("buildataCodegenDir", buildataCodegenDir)

        project.afterEvaluate {
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

                project.configurations.getByName("ksp").dependencies.add(
                    project.dependencies.create("io.github.virelion.buildata:buildata-ksp-plugin:${Version.value}")
                )

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
    }
}

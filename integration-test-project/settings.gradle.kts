pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
    }
}

rootProject.name = "integration-test-project"

include("project-types:multiplatform")
include("project-types:jvm")
include("benchmark")

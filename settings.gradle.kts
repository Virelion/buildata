pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
    }
}

rootProject.name = "Buildata"

include(":buildata-ksp-plugin")
include(":buildata-runtime")
include(":integration-test-project:project-types:multiplatform")
include(":integration-test-project:project-types:jvm")

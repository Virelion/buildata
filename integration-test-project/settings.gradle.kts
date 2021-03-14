pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
        jcenter()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }
}

rootProject.name = "integration-test-project"

enableFeaturePreview("GRADLE_METADATA")
include("project-types:multiplatform")
include("project-types:jvm")

![logo](logo.png)

![badge](https://img.shields.io/badge/platform-JVM-orange)
![badge](https://img.shields.io/badge/platform-JS-red)
![badge](https://img.shields.io/badge/platform-Android-brightgreen)
![badge](https://img.shields.io/badge/platform-Linux--64-blue)
![badge](https://img.shields.io/badge/platform-mingw--64-blueviolet)
![badge](https://img.shields.io/badge/platform-iOS--64-yellowgreen)
![badge](https://img.shields.io/badge/platform-MacOS--64-yellow)

![verification](https://github.com/Virelion/buildata/workflows/verification/badge.svg)
![version](https://img.shields.io/github/v/tag/Virelion/buildata)
![last-commit](https://img.shields.io/github/last-commit/Virelion/buildata)

Kotlin multiplatform code-generator for typed tree data class structures.

# [Builder generator](docs/data-tree-building.md)
Generate builders for your immutable data classes.
Annotate class:
```kotlin
@Buildable
data class Root(
    //...
)
```

and use builders:
```kotlin
Root::class.build {
    branch {
        leaf = "My value"
    }
}
```

See more in [data-tree-building.md](docs/data-tree-building.md)

# [Path reflection](docs/path-reflection.md)
Generate builders for your immutable data classes.

Annotate class:
```kotlin
@PathReflection
data class Root(
    //...
)
```

and automatically gather information about the path to the value:
```kotlin
root.withPath().branch.leaf.path().jsonPath // will return "$.branch.leaf"
```

See more in [path-reflection.md](docs/path-reflection.md)

# [Dynamic access](docs/dynamic-access.md)

All `@Buildable` classes can be dynamically accessed.

Annotate class:
```kotlin
@Buildable
data class Item(
    val value: String,
    val list: List<Map<String,String>>
    // ...
)
```

and access data dynamically with generated accessors:
```kotlin
item.dynamicAccessor["value"] // returns item.value
item.dynamicAccessor["$.list[2]['element']"] // returns item.list[2]["element"]
```

See more in [dynamic-access.md](docs/dynamic-access.md)

# How to set up?
0. Have open source repositories connected to project:
```kotlin
buildscript {
    repositories {
        gradlePluginPortal()
        // ...
    }
}

repositories {
    mavenCentral()
    // ...
}
```

1. Add buildata plugin to your build
```kotlin
plugins {
    kotlin("multiplatform") version "1.9.20"
    kotlin("jvm") version "1.9.20" // alternatively
    // ...
    id("io.github.virelion.buildata") version <LIBRARY_VERSION>
}
```

2. Add buildata runtime to your dependencies
```kotlin
kotlin {
    // ...
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.github.virelion.buildata:buildata-runtime:<LIBRARY_VERSION>")
            }
        }

        // ...
    }
}
```
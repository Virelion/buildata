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

Kotlin multiplatform builder generator.

# How to use?
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
    kotlin("multiplatform") version "1.4.21"
    kotlin("jvm") version "1.4.21" // alternatively
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

3. Add annotation to your `data class`
```kotlin

import io.github.virelion.buildata.Buildable
// ...

@Buildable
data class MyDataClass(
    val property: String
)
```

4. Codegen will generate a builder you can use:
```kotlin
val myDataClass = MyDataClass::class.build {
    property = "Example"
}
```
```kotlin
val builder = MyDataClass::class.builder()
val myDataClass = builder {
    property = "Example"
}.build()
```

# Features
## Default values support
Builders will honor default values during the building.
```kotlin
@Buildable 
data class MyDataClass(
    val propertyWithDefault: String = "DEFAULT",
    val property: String
)
```

```kotlin
val myDataClass = MyDataClass::class.build {
    property = "Example"
}

myDataClass.property // returns "Example"
myDataClass.propertyWithDefault // returns "DEFAULT"
```

## Composite builders support
You can mark item as `@Buildable` to allow accessing inner builders.

```kotlin
@Buildable
data class InnerItem(
    val property: String    
) 

@Buildable 
data class ParentDataClass(
    val innerItem: @Buildable InnerItem
)
```

```kotlin
val parentDataClassBuilder = ParentDataClass::class.builder()

parentDataClassBuilder {
    innerItem {
        property = "Example"
    }
}

val parentDataClass = parentDataClassBuilder.build()
parentDataClass.innerItem.property // returns "Example"
```

This feature allows for creating complex builder structures for tree like `data class` 
and make mutation easy during tree building process.

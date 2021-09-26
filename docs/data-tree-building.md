# Building data tree
Mark data classes as `@Buildable` to create a builder DSL.

See example:
```kotlin
@Buildable
data class InnerItem(
    val property: String    
) 

@Buildable 
data class ParentDataClass(
    val innerItem: InnerItem
)
```

```kotlin
val parentDataClassBuilder = ParentDataClass::class.builder()

// ...
parentDataClassBuilder {
    innerItem {
        property = "InnerProperty"
    }
}

// later in the code
parentDataClassBuilder {
    property = "RootProperty"
}

val parentDataClass = parentDataClassBuilder.build()
parentDataClass.innerItem.property // returns "InnerProperty"
parentDataClass.property // returns "RootProperty"
```

This feature allows for creating complex builder structures for tree like `data class`
and make mutation easy during tree building process.

Helps avoid using `copy()` data class, which can be unadvised for large data trees, and still keeps end result immutable.

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
# Dynamic access

All `@Buildable` classes can be dynamically accessed.
## Usage
Let's consider following data tree structure.

```kotlin
@Buildable
data class Root(
    val branch1: Branch,
    @PathElementName("CUSTOM_NAME")
    val branch2: Branch?,
    val list: List<Branch>,
    val map: Map<String, Branch>
)

@Buildable
data class Branch(
    val stringValue: String
)
```

You can access elements in following way

```kotlin
fun accessElement(root: Root) {
    with(root.dynamicAccessor) {
        this["branch1"] // returns root.branch1
        this["$.CUSTOM_NAME"] // returns root.branch2
        this["$.list"] // returns root.list
        this["$.map"] // returns root.map
        
        this["$.branch1.stringValue"] // returns root.branch1.stringValue
        this["$.list[0].stringValue"] // returns root.list[0].stringValue
        this["$.map['someKey'].stringValue"] // returns root.list[0].stringValue
        this["""$.map["someKey"].stringValue"""] // returns root.list[0].stringValue
        this["$.map.someKey.stringValue"] // returns root.list[0].stringValue
    }
}
```

If element is not found `ElementNotFoundException` is thrown.

### Rename annotations support
Dynamic access honors rename annotations.

Annotations:
- `@io.github.virelion.buildata.path.PathElementName("newName")`
- `@kotlinx.serialization.SerialName("newName")`
- `@com.fasterxml.jackson.annotation.JsonAlias("newName")`

are supported out of the box.
# Path reflection
Path reflection is a feature that allows for figuring out what part of tree data structure is being accessed.

## Usage

Let's consider following data tree structure.

```kotlin
@PathReflection
data class Root(
    val branch1: Branch,
    val branch2: Branch?,
    val list: List<Branch>,
    val map: Map<String, Branch>
)

@PathReflection
data class Branch(
    val stringValue: String
)
```

`@PathReflection` annotation will allow codegen engine to create wrappers 
and create `fun Root.withPath()` and `fun Branch.withPath` functions.
Using this function allows for accessing parts of data class and gathering information about location in tree.

```kotlin
fun exampleUse(root: Root) {
    val wrapper = root.withPath().branch1.stringValue // generated wrapper is returned
    val recordedPath: RecordedPath = wrapper.path() // path collection that allows for further analysis
    recordedPath.jsonPath // will return "$.branch1.stringValue" string
    
    wrapper.value() // returns root.branch1.stringValue
}
```

**NOTE!** After using `withPath()` operations are no longer acting on data class, 
but rather on wrapper structures that were generated to mimic access.

## Nullability
Because operation are done on wrapper classes, those are always will be not null.
which means accessing members of `branch2` looks like this.

```kotlin
fun exampleUse(root: Root) {
    val wrapper = root.withPath().branch2.stringValue // no '?' safe call is needed
    wrapper.path().jsonPath // will return "$.branch2.stringValue" string, even if branch2 is null
    
    wrapper.value() // will be null if any tree node is null
}
```

## Collections
Here is how collections such as lists and maps can be accessed.
So far Lists and Maps (with `String` key) are supported.

```kotlin
fun exampleUse(root: Root) {
    val root = Root(
        //...
        list = listOf(Branch("content"))
    )
    with(root.withPath().list[0].stringValue) {
        value() // will return "content"
        path().jsonPath // will return "$.list[0].stringValue"
    }
}
```

Missing elements in collections do not throw any exceptions, but rather are treated as null. Therefore:
```kotlin
fun exampleUse(root: Root) {
    val root = Root(
        //...
        list = listOf() //empty list
    )
    with(root.withPath().list[0].stringValue) { // stringValue is now treated as nullable since it can be missing from the list
        value() // will return null
        path().jsonPath // will return "$.list[0].stringValue" // will still return full path
    }
}
```

## Changing name in path
Using `@PathElementName("newName")` can change the string that will appear in path.

See example:
```kotlin
@PathReflection
data class Data(
    @PathElementName("newName")
    val stringValue: String
)

fun exampleUse(data: Data) {
    val wrapper = data.withPath().stringValue.path().jsonPath // will return "$.newName"
}
```

### Json serialization engines support
Using annotations from popular serialization engines is also supported. 

Annotations:
- `@kotlinx.serialization.SerialName("newName")`
- `@com.fasterxml.jackson.annotation.JsonAlias("newName")`

are supported out of the box.
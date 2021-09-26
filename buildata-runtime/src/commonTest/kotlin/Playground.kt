package playground

import io.github.virelion.buildata.path.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

data class Root(
    @PathElementName("asdf")
    val inner1: Inner1,
    val value: String,
    val nullable: String? = null
)

fun Root.withPath() = Root_PathReflectionWrapper(this, RecordedPath())
// generate nullable on KClass

data class Inner1(
    val inner2: Inner2,
    val innerList: List<Inner2>,
    val innerMap: Map<String, Inner2>,
    val value: String,
    val nullable: String? = null,
    val nullableInner2: Inner2? = null,
    val listOfNullables: List<Inner2?> = listOf(null),
)

data class Inner2(
    val value: String
)

class Root_PathReflectionWrapper(private val __item: Root, private val __path: RecordedPath) :
    PathReflectionWrapper<Root> {

    val value: ScalarPathReflectionWrapper<String> by lazy {
        ScalarPathReflectionWrapper(
            value().value,
            path() + StringNamePathIdentifier("value")
        )
    }

    val nullable: ScalarPathReflectionWrapper<String?> by lazy {
        ScalarPathReflectionWrapper(
            value().value,
            path() + StringNamePathIdentifier("nullable")
        )
    }

    val inner1: Inner1_PathReflectionWrapper by lazy {
        Inner1_PathReflectionWrapper(
            value().inner1,
            path() + StringNamePathIdentifier("inner1")
        )
    }

    override fun value() = __item
    override fun path() = __path
}

class Inner1_PathReflectionWrapper(private val __item: Inner1, private val __path: RecordedPath) :
    PathReflectionWrapper<Inner1> {
    val value: ScalarPathReflectionWrapper<String> by lazy {
        ScalarPathReflectionWrapper(
            value().value,
            path() + StringNamePathIdentifier("value")
        )
    }
    val inner2: Inner2_PathReflectionWrapper by lazy {
        Inner2_PathReflectionWrapper(
            value().inner2,
            path() + StringNamePathIdentifier("inner2")
        )
    }

    val innerList: List<Inner2_NullablePathReflectionWrapper> by lazy {
        PathReflectionList(
            value().innerList,
            path() + StringNamePathIdentifier("innerList"),
            ::Inner2_NullablePathReflectionWrapper
        )
    }

    val innerMap: PathReflectionMap<Inner2, Inner2_NullablePathReflectionWrapper> by lazy {
        PathReflectionMap(
            value().innerMap,
            path() + StringNamePathIdentifier("innerMap"),
            ::Inner2_NullablePathReflectionWrapper
        )
    }
    val nullable: ScalarPathReflectionWrapper<String?> by lazy {
        ScalarPathReflectionWrapper(value().nullable, path())
    }
    val nullableInner2: Inner2_NullablePathReflectionWrapper by lazy {
        Inner2_NullablePathReflectionWrapper(value().nullableInner2, path())
    }
    val listOfNullables: PathReflectionList<Inner2?, Inner2_NullablePathReflectionWrapper> by lazy {
        PathReflectionList(
            value().listOfNullables,
            path() + StringNamePathIdentifier("listOfNullables"),
            ::Inner2_NullablePathReflectionWrapper
        )
    }

    override fun value() = __item
    override fun path() = __path
}

class Inner2_PathReflectionWrapper(private val __item: Inner2, private val __path: RecordedPath) :
    PathReflectionWrapper<Inner2> {

    val value: ScalarPathReflectionWrapper<String> by lazy {
        ScalarPathReflectionWrapper(
            value().value,
            path() + StringNamePathIdentifier("value")
        )
    }

    override fun value(): Inner2 = __item
    override fun path(): RecordedPath = __path
}

class Inner2_NullablePathReflectionWrapper(
    private val __value: Inner2?,
    private val __path: RecordedPath
) : PathReflectionWrapper<Inner2?> {
    val value: ScalarPathReflectionWrapper<String?> by lazy {
        ScalarPathReflectionWrapper(
            value()?.value,
            path() + StringNamePathIdentifier("value")
        )
    }

    override fun value() = __value
    override fun path() = __path
}

class Playground {
    val root = Root(
        Inner1(
            Inner2("inner2"),
            listOf(Inner2("listInner2"), Inner2("listInner2")),
            mapOf("key" to Inner2("mapInner2"), "key2" to Inner2("mapInner2")),
            "inner1",
            null,
            null,
        ),
        "root",
        null
    )

    @Test
    fun testSimpleValueWithPath() {
        with(root.withPath().value) {
            assertEquals("$.value", path().jsonPath)
            assertEquals("root", value())
        }
        with(root.withPath().inner1) {
            assertEquals("$.inner1", path().jsonPath)
            assertSame(root.inner1, value())
        }
        with(root.withPath().inner1.innerList[0]) {
            assertEquals("$.inner1.innerList[0]", path().jsonPath)
            assertSame(root.inner1.innerList[0], value())
        }
        with(root.withPath().inner1.innerList[0].value) {
            assertEquals("$.inner1.innerList[0].value", path().jsonPath)
            assertSame(root.inner1.innerList[0].value, value())
        }
        with(root.withPath().inner1.innerMap["key"]) {
            assertEquals("$.inner1.innerMap['key']", path().jsonPath)
            assertSame(root.inner1.innerMap["key"], value())
        }
        with(root.withPath().inner1.innerMap["key"].value) {
            assertEquals("$.inner1.innerMap['key'].value", path().jsonPath)
            assertSame(root.inner1.innerMap["key"]?.value, value())
        }
        with(root.withPath().inner1.innerList[0].value) {
            assertEquals("$.inner1.innerList[0].value", path().jsonPath)
            assertSame(root.inner1.innerList[0].value, value())
        }
        with(root.withPath().inner1.value) {
            assertEquals("$.inner1.value", path().jsonPath)
            assertEquals(root.inner1.value, value())
        }
        with(root.withPath().inner1.inner2) {
            assertEquals("$.inner1.inner2", path().jsonPath)
            assertEquals(root.inner1.inner2, value())
        }
        with(root.withPath().inner1.inner2.value) {
            assertEquals("$.inner1.inner2.value", path().jsonPath)
            assertEquals(root.inner1.inner2.value, value())
        }
    }

    @Test
    fun testList() {
        val listWrapper =root.withPath().inner1.innerList
        with(listWrapper) {
            with(this[0].value) {
                assertEquals("$.inner1.innerList[0].value", this.path().jsonPath)
                assertSame(root.inner1.innerList[0].value, value())
            }
            with(this[1].value) {
                assertEquals("$.inner1.innerList[1].value", this.path().jsonPath)
                assertSame(root.inner1.innerList[1].value, value())
            }
            with(this[100000].value) {
                assertEquals("$.inner1.innerList[100000].value", this.path().jsonPath)
                assertNull(value())
            }
        }
    }

    @Test
    fun testMapConversion() {
        with(root.withPath().inner1.innerMap) {
            with(assertNotNull(this["key"]).value) {
                assertEquals("$.inner1.innerMap['key'].value", this.path().jsonPath)
                assertSame(root.inner1.innerMap["key"]!!.value, value())
            }
            with(assertNotNull(this["key2"]).value) {
                assertEquals("$.inner1.innerMap['key2'].value", this.path().jsonPath)
                assertSame(root.inner1.innerMap["key2"]!!.value, value())
            }
            with(assertNotNull(this["nothing"]).value) {
                assertEquals("$.inner1.innerMap['nothing'].value", this.path().jsonPath)
                assertNull(value())
            }
        }
    }
}
package playground

import io.github.virelion.buildata.path.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertSame

data class Root(
    val inner1: Inner1,
    val value: String,
    val nullable: String? = null
)

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

class Root_PathPropertyWrapper(override val item: Root, override val pathRecorder: PathRecorder) :
    PathPropertyWrapper<Root> {
    val value: ScalarPathPropertyWrapper<String> by PathRecorderProperty(pathRecorder) {
        ScalarPathPropertyWrapper(
            item.value,
            pathRecorder
        )
    }
    val nullable: ScalarPathPropertyWrapper<String?> by PathRecorderProperty(pathRecorder) {
        ScalarPathPropertyWrapper(
            item.nullable,
            pathRecorder
        )
    }
    val inner1: Inner1_PathPropertyWrapper by PathRecorderProperty(pathRecorder) {
        Inner1_PathPropertyWrapper(
            item.inner1,
            pathRecorder
        )
    }

    override fun clone(): Root_PathPropertyWrapper {
        return Root_PathPropertyWrapper(item, pathRecorder.clone())
    }
}

fun <T, Wrapper : PathPropertyWrapper<T>> Root.valueWithPath(
    recording: Root_PathPropertyWrapper.() -> Wrapper
) = Root_PathPropertyWrapper(this, PathRecorder()).valueWithPath(recording)

fun <T, Wrapper : PathPropertyWrapper<T>> Root.path(
    recording: Root_PathPropertyWrapper.() -> Wrapper
) = Root_PathPropertyWrapper(this, PathRecorder()).path(recording)

class Inner1_PathPropertyWrapper(override val item: Inner1, override val pathRecorder: PathRecorder) :
    PathPropertyWrapper<Inner1> {
    val value: ScalarPathPropertyWrapper<String> by PathRecorderProperty(pathRecorder) {
        ScalarPathPropertyWrapper(
            item.value,
            pathRecorder
        )
    }
    val inner2: Inner2_PathPropertyWrapper by PathRecorderProperty(pathRecorder) {
        Inner2_PathPropertyWrapper(
            item.inner2,
            pathRecorder
        )
    }
    val innerList: ListPathRecorder<Inner2, Inner2_PathPropertyWrapper> by PathRecorderProperty(pathRecorder) {
        ListPathRecorder(item.innerList, pathRecorder) { recorder, it ->
            Inner2_PathPropertyWrapper(it!!, recorder)
        }
    }
    val innerMap: MapPathRecorder<Inner2, Inner2_PathPropertyWrapper> by PathRecorderProperty(pathRecorder) {
        MapPathRecorder(item.innerMap, pathRecorder) { recorder, it ->
            Inner2_PathPropertyWrapper(it!!, recorder)
        }
    }
    val nullable: ScalarPathPropertyWrapper<String?> by PathRecorderProperty(pathRecorder) {
        ScalarPathPropertyWrapper(
            item.nullable,
            pathRecorder
        )
    }
    val nullableInner2: Inner2_NullablePathPropertyWrapper by PathRecorderProperty(pathRecorder) {
        Inner2_NullablePathPropertyWrapper(item.nullableInner2, pathRecorder)
    }
    val listOfNullables: ListPathRecorder<Inner2?, Inner2_NullablePathPropertyWrapper> by PathRecorderProperty(
        pathRecorder
    ) {
        ListPathRecorder(item.listOfNullables, pathRecorder) { recorder, it ->
            Inner2_NullablePathPropertyWrapper(it, recorder)
        }
    }

    override fun clone(): Inner1_PathPropertyWrapper {
        return Inner1_PathPropertyWrapper(item, pathRecorder.clone())
    }
}

class Inner2_PathPropertyWrapper(override val item: Inner2, override val pathRecorder: PathRecorder) :
    PathPropertyWrapper<Inner2> {
    val value: ScalarPathPropertyWrapper<String> by PathRecorderProperty(pathRecorder) {
        ScalarPathPropertyWrapper(
            item.value,
            pathRecorder
        )
    }

    override fun clone(): Inner2_PathPropertyWrapper {
        return Inner2_PathPropertyWrapper(item, pathRecorder.clone())
    }
}

class Inner2_NullablePathPropertyWrapper(override val item: Inner2?, override val pathRecorder: PathRecorder) :
    PathPropertyWrapper<Inner2?> {
    val value: ScalarPathPropertyWrapper<String?> by PathRecorderProperty(pathRecorder) {
        ScalarPathPropertyWrapper(
            item?.value,
            pathRecorder
        )
    }

    override fun clone(): Inner2_NullablePathPropertyWrapper {
        return Inner2_NullablePathPropertyWrapper(item, pathRecorder.clone())
    }
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
        with(root.valueWithPath { value }) {
            assertEquals("$.value", jsonPath)
            assertEquals("root", this.value)
        }
        with(root.valueWithPath { inner1 }) {
            assertEquals("$.inner1", jsonPath)
            assertSame(root.inner1, this.value)
        }
        with(root.valueWithPath { inner1.innerList[0] }) {
            assertEquals("$.inner1.innerList[0]", jsonPath)
            assertSame(root.inner1.innerList[0], this.value)
        }
        with(root.valueWithPath { inner1.innerList[0].value }) {
            assertEquals("$.inner1.innerList[0].value", jsonPath)
            assertSame(root.inner1.innerList[0].value, this.value)
        }
        with(root.valueWithPath { inner1.innerMap["key"] }) {
            assertEquals("$.inner1.innerMap['key']", jsonPath)
            assertSame(root.inner1.innerMap["key"], this.value)
        }
        with(root.valueWithPath { inner1.innerMap["key"].value }) {
            assertEquals("$.inner1.innerMap['key'].value", jsonPath)
            assertSame(root.inner1.innerMap["key"]?.value, this.value)
        }
        with(root.valueWithPath { inner1.innerList[0].value }) {
            assertEquals("$.inner1.innerList[0].value", jsonPath)
            assertSame(root.inner1.innerList[0].value, this.value)
        }
        with(root.valueWithPath { inner1.value }) {
            assertEquals("$.inner1.value", jsonPath)
            assertEquals(root.inner1.value, this.value)
        }
        with(root.valueWithPath { inner1.inner2 }) {
            assertEquals("$.inner1.inner2", jsonPath)
            assertEquals(root.inner1.inner2, value)
        }
        with(root.valueWithPath { inner1.inner2.value }) {
            assertEquals("$.inner1.inner2.value", jsonPath)
            assertEquals(root.inner1.inner2.value, this.value)
        }
    }

    @Test
    fun testSplittedPathResolution() {
        val wrapper = root.path { inner1 }.path { inner2 }

        with(wrapper.path { value }) {
            assertEquals("$.inner1.inner2.value", path.jsonPath)
        }


        with(wrapper.valueWithPath { value }) {
            assertEquals("$.inner1.inner2.value", jsonPath)
            assertEquals("inner2", value)
        }

        val wrapper2 = root.path { inner1 }.path { nullableInner2 }

        with(wrapper2.path { value }) {
            assertEquals("$.inner1.nullableInner2.value", path.jsonPath)
        }

        with(wrapper2.valueWithPath { value }) {
            assertEquals("$.inner1.nullableInner2.value", jsonPath)
            assertEquals(null, value)
        }
    }

    @Test
    fun testNullables() {
        with(root.valueWithPath { nullable }) {
            assertEquals("$.nullable", jsonPath)
            assertEquals(null, this.value)
        }
        with(root.valueWithPath { inner1.nullable }) {
            assertEquals("$.inner1.nullable", jsonPath)
            assertEquals(null, this.value)
        }
        with(root.valueWithPath { inner1.nullableInner2.value }) {
            assertEquals("$.inner1.nullableInner2.value", jsonPath)
            assertEquals(null, this.value)
        }
        with(root.valueWithPath { inner1.listOfNullables[0].value }) {
            assertEquals("$.inner1.listOfNullables[0].value", jsonPath)
            assertEquals(null, this.value)
        }
    }

    @Test
    fun testListConversion() {
        val listWrapper = root.path { inner1.innerList }
        with(listWrapper.toList()) {
            with(this[0].value) {
                assertEquals("$.inner1.innerList[0].value", this.path.jsonPath)
                assertSame(root.inner1.innerList[0].value, this.item)
            }
            with(this[1].value) {
                assertEquals("$.inner1.innerList[1].value", this.path.jsonPath)
                assertSame(root.inner1.innerList[1].value, this.item)
            }
        }
    }

    @Test
    fun testMapConversion() {
        val listWrapper = root.path { inner1.innerMap }
        with(listWrapper.toMap()) {
            with(assertNotNull(this["key"]).value) {
                assertEquals("$.inner1.innerMap['key'].value", this.path.jsonPath)
                assertSame(root.inner1.innerMap["key"]!!.value, this.item)
            }
            with(assertNotNull(this["key2"]).value) {
                assertEquals("$.inner1.innerMap['key2'].value", this.path.jsonPath)
                assertSame(root.inner1.innerMap["key2"]!!.value, this.item)
            }
        }
    }
}
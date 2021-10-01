package io.github.virelion.buildata.integration.access

import io.github.virelion.buildata.access.MissingPropertyException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

class DynamicAccessTest {
    private val root = DynamicAccessRoot(
        mapOf("root" to "Root"),
        listOf("root"),
        DynamicAccessInner1(
            mapOf("inner1" to "Inner1"),
            listOf("inner1"),
        ),
        null
    )

    @Test
    fun isValueAccessPossible() {
        with(root.dynamicAccessor) {
            assertSame(root.map, this["map"])
            assertSame(root.list, this["list"])
            assertSame(root.element, this["element"])
            assertNull(this["nullable_element"])
            with(this.get<DynamicAccessInner1>("element").dynamicAccessor) {
                assertSame(root.element.map, this["map"])
                assertSame(root.element.list, this["list"])
            }
        }
    }

    @Test
    fun exceptionIsThrownWhenElementIsNotFound() {
        with(root.dynamicAccessor) {
            var exception: MissingPropertyException? = null
            try {
                this["InvalidElement"]
            } catch (e: MissingPropertyException) {
                exception = e
            }
            assertNotNull(exception)
            with(exception) {
                assertEquals("InvalidElement", this.propertyName)
                assertEquals("Property 'InvalidElement' could not be found in io.github.virelion.buildata.integration.access.DynamicAccessRoot", this.message)
            }
        }
    }

    @Test
    fun isPropertyAccessPossible() {
        with(root.dynamicAccessor) {
            assertSame(root::map.get(), this.getProperty("map")?.get())
            assertSame(root::list.get(), this.getProperty("list")?.get())
            assertSame(root::element.get(), this.getProperty("element")?.get())
            assertNull(this["nullable_element"])
            with(this.get<DynamicAccessInner1>("element").dynamicAccessor) {
                assertSame(root.element::map.get(), this.getProperty("map")?.get())
                assertSame(root.element::list.get(), this.getProperty("list")?.get())
            }
        }
    }

    @Test
    fun exceptionIsThrownWhenPropertyIsNotFound() {
        with(root.dynamicAccessor) {
            var exception: MissingPropertyException? = null
            try {
                this.getProperty("InvalidElement")
            } catch (e: MissingPropertyException) {
                exception = e
            }
            assertNotNull(exception)
            with(exception) {
                assertEquals("InvalidElement", this.propertyName)
                assertEquals("Property 'InvalidElement' could not be found in io.github.virelion.buildata.integration.access.DynamicAccessRoot", this.message)
            }
        }
    }
}

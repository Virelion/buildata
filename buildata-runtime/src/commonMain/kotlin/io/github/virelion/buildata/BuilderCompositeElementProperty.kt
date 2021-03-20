package io.github.virelion.buildata

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Property delegate used in generated code for non-nullable composite [Buildable] elements
 *
 * @param builderProvider builder provider
 */
class BuilderCompositeElementProperty<T : Any, B : Builder<T>>(
    val builderProvider: () -> B
) : ReadWriteProperty<Any?, T> {
    var initialized = false
        private set
    var builder: B = builderProvider()

    /**
     * Runs builder lambda and sets property state to initialized
     */
    fun useBuilder(block: B.() -> Unit) {
        initialized = true
        builder.block()
    }

    /**
     * Sets property value.
     */
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        initialized = true
        builder.populateWith(value)
    }

    /**
     * @return value of the property
     * @throws UninitializedPropertyException when property is accessed before it is set
     */
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        requireInitialized(initialized, property.name)

        return builder.build()
    }
}

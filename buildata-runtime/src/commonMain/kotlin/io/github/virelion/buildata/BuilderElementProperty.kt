package io.github.virelion.buildata

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Property delegate used in generated code for regular elements
 */
class BuilderElementProperty<T> : ReadWriteProperty<Any?, T> {
    var initialized = false
        private set
    private var container: T? = null

    /**
     * Sets property value.
     */
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        initialized = true
        container = value
    }

    /**
     * @return value of the property
     * @throws UninitializedPropertyException when property is accessed before it is set
     */
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        requireInitialized(initialized, property.name)

        @Suppress("unchecked_cast")
        return container as T
    }
}

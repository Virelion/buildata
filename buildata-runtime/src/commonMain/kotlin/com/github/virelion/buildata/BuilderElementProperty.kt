package com.github.virelion.buildata

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class BuilderElementProperty<T> : ReadWriteProperty<Any?, T> {
    var initialized = false
        private set
    private var container: T? = null

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        initialized = true
        container = value
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        require(initialized) { "Property ${property.name} was not initialized" }

        @Suppress("unchecked_cast")
        return container as T
    }
}

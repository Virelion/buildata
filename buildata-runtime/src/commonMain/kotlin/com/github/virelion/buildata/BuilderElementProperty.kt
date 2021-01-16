package com.github.virelion.buildata

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class BuilderElementProperty<T>(
        val defaultValueProvider: (() -> T)?,
        val optional: Boolean
) : ReadWriteProperty<Any?, T> {
    private var initialized = false
    private var container: T? = defaultValueProvider?.let {
        initialized = true
        it.invoke()
    }

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
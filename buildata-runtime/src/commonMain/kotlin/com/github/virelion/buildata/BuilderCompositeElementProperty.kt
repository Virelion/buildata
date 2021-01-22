package com.github.virelion.buildata

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class BuilderCompositeElementProperty<T: Any, B: Builder<T>>(
        val builderProvider: () -> B
) : ReadWriteProperty<Any?, T> {
    var initialized = false
        private set
    var builder: B = builderProvider()

    fun useBuilder(block: B.() -> Unit ) {
        initialized = true
        builder.block()
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        initialized = true
        builder.populateWith(value)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        require(initialized) { "Property ${property.name} was not initialized" }

        return builder.build()
    }
}
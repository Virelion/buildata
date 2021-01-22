package com.github.virelion.buildata

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class BuilderNullableCompositeElementProperty<T: Any, B: Builder<T>>(
        val builderProvider: () -> B
) : ReadWriteProperty<Any?, T?> {
    private var setToNull = false
    var initialized = false
        private set
    var builder: B = builderProvider()

    fun useBuilder(block: B.() -> Unit ) {
        setToNull = false
        initialized = true
        builder.block()
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        initialized = true
        setToNull = value == null
        if(value != null) {
            builder.populateWith(value)
        } else {
            builder = builderProvider()
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        require(initialized) { "Property ${property.name} was not initialized" }

        return if(setToNull) {
            null
        } else {
            builder.build()
        }
    }
}
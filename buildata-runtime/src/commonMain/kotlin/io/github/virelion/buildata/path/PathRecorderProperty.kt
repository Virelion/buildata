package io.github.virelion.buildata.path

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PathRecorderProperty<Type>(
    val recorder: PathRecorder,
    val getter: () -> Type
) : ReadWriteProperty<Any?, Type> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Type {
        recorder.push(StringNamePathIdentifier(property.name))
        return getter()
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Type) {
        // do nothing
    }
}
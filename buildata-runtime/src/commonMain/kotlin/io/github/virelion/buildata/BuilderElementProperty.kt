/*
 * Copyright 2021 Maciej Ziemba
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.virelion.buildata

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Property delegate used in generated code for regular elements
 */
class BuilderElementProperty<T> : ReadWriteProperty<Any?, T> {
    var initialized = false
        private set
    var container: T? = null

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

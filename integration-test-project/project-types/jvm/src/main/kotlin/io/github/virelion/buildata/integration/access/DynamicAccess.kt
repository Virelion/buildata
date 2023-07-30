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
package io.github.virelion.buildata.integration.access

import io.github.virelion.buildata.Buildable
import io.github.virelion.buildata.access.IntAccessible
import io.github.virelion.buildata.access.StringAccessible
import io.github.virelion.buildata.path.PathElementName

@Buildable
data class DynamicAccessRoot(
    val map: Map<String, String>,
    val intMap: Map<Int, String> = mapOf(),
    val list: List<String>,
    val array: Array<String>,
    val element: DynamicAccessInner1,
    val nullable_element: DynamicAccessInner1?,

    @PathElementName("CUSTOM_NAME")
    val customName: String = ""
)

@Buildable
data class DynamicAccessInner1(
    val map: Map<String, String>,
    val list: List<String>,
    val intMap: Map<Int, String>,
    val array: Array<String>,
    val customStringAccessible: CustomStringAccessible = CustomStringAccessible,
    val customIntAccessible: CustomIntAccessible = CustomIntAccessible
)

@Buildable
data class ComplexData(
    val data: Map<String, List<DynamicAccessRoot>>
)

object CustomStringAccessible : StringAccessible {
    override fun accessElement(key: String): Any? {
        if (key == "custom") {
            return "custom"
        } else {
            throw IndexOutOfBoundsException()
        }
    }
}

object CustomIntAccessible : IntAccessible {
    override fun accessElement(key: Int): Any? {
        if (key == 42) {
            return "custom"
        } else {
            throw IndexOutOfBoundsException()
        }
    }
}

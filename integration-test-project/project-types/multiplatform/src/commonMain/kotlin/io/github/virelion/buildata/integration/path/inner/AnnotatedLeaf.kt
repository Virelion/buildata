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
package io.github.virelion.buildata.integration.path.inner

import com.fasterxml.jackson.annotation.JsonAlias
import io.github.virelion.buildata.path.PathElementName
import io.github.virelion.buildata.path.PathReflection
import kotlinx.serialization.SerialName

@PathReflection
data class AnnotatedLeaf(
    @PathElementName("PATH_ELEMENT_NAME")
    val pathElementNameAnnotated: String,

    @SerialName("KOTLINX_SERIALIZATION_SERIAL_NAME")
    val kotlinxSerializationAnnotated: String
)

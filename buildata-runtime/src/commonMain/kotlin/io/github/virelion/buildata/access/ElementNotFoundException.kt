/*
 * Copyright 2022 Maciej Ziemba
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

package io.github.virelion.buildata.access

import io.github.virelion.buildata.path.PathIdentifier
import io.github.virelion.buildata.path.RecordedPath

class ElementNotFoundException(
    /**
     * Path processed before element was not found
     */
    val pathProcessed: RecordedPath,

    /**
     * Last element processed
     */
    val lastItemProcessed: Any?,

    /**
     * Path identifier of element that was not found
     */
    val lastProcessedPathIdentifier: PathIdentifier
) : RuntimeException(
    "On $pathProcessed cannot $lastProcessedPathIdentifier access on $lastItemProcessed"
)
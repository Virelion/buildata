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
package io.github.virelion.buildata.path

/**
 * List implementation for seamless path wrapped elements access
 *
 * Accessing element that is out of bound will return nullable wrapper.
 */
class PathReflectionList<Type, Wrapper : PathReflectionWrapper<Type?>> internal constructor(
    private val delegate: List<Wrapper>,
    private val nullWrapperProvider: (Int) -> Wrapper
) : List<Wrapper> by delegate {
    constructor(
        originalList: List<Type>,
        pathToList: RecordedPath,
        wrapperProvider: (Type?, RecordedPath) -> Wrapper
    ) : this(
        delegate = originalList.mapIndexed { index, item ->
            wrapperProvider(
                item,
                pathToList + IntIndexPathIdentifier(index)
            )
        },
        nullWrapperProvider = { index -> wrapperProvider(null, pathToList + IntIndexPathIdentifier(index)) }
    )

    override fun get(index: Int): Wrapper {
        if (index >= size) {
            return nullWrapperProvider(index)
        }

        return delegate[index]
    }
}

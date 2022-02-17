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

import kotlin.jvm.JvmInline

/**
 * List of [PathIdentifier].
 *
 * Elements are ordered as they were accessed: first element accessed is first element of collection, etc.
 */
@JvmInline
value class RecordedPath(private val item: List<PathIdentifier> = listOf()) {
    operator fun plus(identifier: PathIdentifier): RecordedPath {
        return RecordedPath(item + identifier)
    }

    val jsonPath: String get() {
        val builder = StringBuilder()
        builder.append("$")
        item.forEach {
            when (it) {
                is IntIndexPathIdentifier -> builder.append("[${it.index}]")
                is StringIndexPathIdentifier -> builder.append("['${it.index}']")
                is StringNamePathIdentifier -> builder.append(".${it.name}")
            }
        }

        return builder.toString()
    }
}

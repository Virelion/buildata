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
package io.github.virelion.buildata.access

import io.github.virelion.buildata.Builder
import io.github.virelion.buildata.path.IntIndexPathIdentifier
import io.github.virelion.buildata.path.PathIdentifier
import io.github.virelion.buildata.path.RecordedPath
import io.github.virelion.buildata.path.StringIndexPathIdentifier
import io.github.virelion.buildata.path.StringNamePathIdentifier

/**
 * Marker for code-generated class dynamic elements access.
 */
class DynamicAccessor(private val builder: Builder<*>) {
    @Throws(ElementNotFoundException::class)
    operator fun <R> get(path: String): R? {
        return get(*RecordedPath.Parser.parse(path).path.toTypedArray())
    }

    @Throws(ElementNotFoundException::class)
    operator fun <R> get(vararg path: PathIdentifier): R? {
        if (path.isEmpty()) return builder.build() as R

        val element: PathAccessProcessingAccumulator = path
            .fold(PathAccessProcessingAccumulator(builder, listOf())) { acc, pathIdentifier ->
                resolvePathIdentifier(pathIdentifier, acc)
            }

        if (element.item is Builder<*>) {
            return element.item.build() as R
        }

        return element.item as R
    }

    private data class PathAccessProcessingAccumulator(
        val item: Any?,
        val pathProcessed: List<PathIdentifier>
    )

    private fun resolvePathIdentifier(
        pathElement: PathIdentifier,
        acc: PathAccessProcessingAccumulator
    ): PathAccessProcessingAccumulator {
        return try {
            when (pathElement) {
                is IntIndexPathIdentifier -> resolveIntIndexPathIdentifier(pathElement, acc)
                is StringIndexPathIdentifier -> resolveStringIndexPathIdentifier(pathElement, acc)
                is StringNamePathIdentifier -> resolveStringNamePathIdentifier(pathElement, acc)
            }
        } catch (e: Exception) { handleException(e, pathElement, acc) }
    }

    private fun resolveIntIndexPathIdentifier(
        pathElement: IntIndexPathIdentifier,
        acc: PathAccessProcessingAccumulator
    ): PathAccessProcessingAccumulator {
        return when (acc.item) {
            is List<*> -> {
                // additional check for KotlinJS
                if (pathElement.index >= acc.item.size) throw IndexOutOfBoundsException()
                acc.item[pathElement.index]
            }
            is Array<*> -> {
                // additional check for KotlinJS
                if (pathElement.index >= acc.item.size) throw IndexOutOfBoundsException()
                acc.item[pathElement.index]
            }
            is Map<*, *> -> {
                if (pathElement.index !in acc.item) throw IndexOutOfBoundsException()
                acc.item[pathElement.index]
            }
            is IntAccessible -> acc.item.accessElement(pathElement.index)
            else -> throw ElementNotFoundException(
                pathProcessed = RecordedPath(acc.pathProcessed),
                lastItemProcessed = acc.item,
                lastProcessedPathIdentifier = pathElement
            )
        }.let { PathAccessProcessingAccumulator(it, acc.pathProcessed + pathElement) }
    }

    private fun resolveStringIndexPathIdentifier(
        pathElement: StringIndexPathIdentifier,
        acc: PathAccessProcessingAccumulator
    ): PathAccessProcessingAccumulator {
        return when (acc.item) {
            is Map<*, *> -> {
                if (pathElement.index !in acc.item) throw IndexOutOfBoundsException()
                acc.item[pathElement.index]
            }
            is StringAccessible -> acc.item.accessElement(pathElement.index)
            else -> throw ElementNotFoundException(
                pathProcessed = RecordedPath(acc.pathProcessed),
                lastItemProcessed = acc.item,
                lastProcessedPathIdentifier = pathElement
            )
        }.let { PathAccessProcessingAccumulator(it, acc.pathProcessed + pathElement) }
    }

    private fun resolveStringNamePathIdentifier(
        pathElement: StringNamePathIdentifier,
        acc: PathAccessProcessingAccumulator
    ): PathAccessProcessingAccumulator {
        if (acc.item !is StringAccessible) {
            throw ElementNotFoundException(
                pathProcessed = RecordedPath(acc.pathProcessed),
                lastItemProcessed = acc.item,
                lastProcessedPathIdentifier = pathElement
            )
        }
        return acc.item.accessElement(pathElement.name)
            .let { PathAccessProcessingAccumulator(it, acc.pathProcessed + pathElement) }
    }

    private fun handleException(
        e: Exception,
        pathElement: PathIdentifier,
        acc: PathAccessProcessingAccumulator
    ): Nothing {
        when (e) {
            is MissingPropertyException, is IndexOutOfBoundsException -> {
                val item = if (acc.item is Builder<*>) {
                    acc.item.build()
                } else {
                    acc.item
                }

                throw ElementNotFoundException(
                    pathProcessed = RecordedPath(acc.pathProcessed),
                    lastItemProcessed = item,
                    lastProcessedPathIdentifier = pathElement
                )
            }
            else -> throw e
        }
    }
}

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
value class RecordedPath(val path: List<PathIdentifier> = listOf()) {
    object Parser {
        private val NO_TOKEN = 0.toChar()

        private fun createPathIdentifierOutOfToken(token: Char, identifier: String): PathIdentifier {
            return when (token) {
                '.' -> StringNamePathIdentifier(identifier)
                '[' -> {
                    identifier.toIntOrNull()
                        ?.let { IntIndexPathIdentifier(it) }
                        ?: identifier
                            .removeSurrounding("'")
                            .removeSurrounding("\"")
                            .let { StringIndexPathIdentifier(it) }
                }
                else -> throw IllegalArgumentException("Unrecognized token $token")
            }
        }

        fun parse(path: String): RecordedPath {
            if (path.trim() == "" || path.trim() == "$") return RecordedPath(listOf())

            val preparedPath = path.removePrefix("$").let {
                if (!(it.startsWith(".") || it.startsWith("["))) {
                    ".$it"
                } else {
                    it
                }
            }.trim()
            val iterator = preparedPath.iterator()

            val identifiers = mutableListOf<PathIdentifier>()
            var tokenType = NO_TOKEN
            var identifier = ""

            while (iterator.hasNext()) {
                when (val nextCharacter = iterator.nextChar()) {
                    '.', '[' -> {
                        if (tokenType != NO_TOKEN) {
                            identifiers += createPathIdentifierOutOfToken(tokenType, identifier)
                            identifier = ""
                        }
                        tokenType = nextCharacter
                    }
                    ']' -> {
                        require(tokenType == '[') { "Found closing bracket ], but there is no opening bracket [" }
                    }
                    else -> {
                        identifier += nextCharacter
                    }
                }
            }

            if (tokenType != NO_TOKEN) {
                identifiers += createPathIdentifierOutOfToken(tokenType, identifier)
            }

            return RecordedPath(identifiers)
        }
    }

    operator fun plus(identifier: PathIdentifier): RecordedPath {
        return RecordedPath(path + identifier)
    }

    val jsonPath: String get() {
        val builder = StringBuilder()
        builder.append("$")
        path.forEach {
            when (it) {
                is IntIndexPathIdentifier -> builder.append("[${it.index}]")
                is StringIndexPathIdentifier -> builder.append("['${it.index}']")
                is StringNamePathIdentifier -> builder.append(".${it.name}")
            }
        }

        return builder.toString()
    }
}

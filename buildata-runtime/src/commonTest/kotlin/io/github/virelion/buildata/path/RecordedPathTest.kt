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

package io.github.virelion.buildata.path

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RecordedPathTest {
    private val parser = RecordedPath.Parser
    private fun assertPathEquals(
        expected: String,
        actual: RecordedPath
    ) {
        assertEquals(expected, actual.jsonPath)
    }

    @Test
    fun emptyPathFromString() {
        assertTrue(parser.parse("").path.isEmpty())
        assertTrue(parser.parse("$").path.isEmpty())
    }

    @Test
    fun noDotPrefixAccess() {
        assertPathEquals("$.el1", parser.parse("el1"))
        assertPathEquals("$.el1.el2", parser.parse("el1.el2"))
    }

    @Test
    fun singleStringNameIdentifier() {
        assertPathEquals("$.StringNamedElement", parser.parse("$.StringNamedElement"))
    }

    @Test
    fun doubleStringNameIdentifier() {
        assertPathEquals(
            "$.StringNamedElement1.StringNamedElement2",
            parser.parse("$.StringNamedElement1.StringNamedElement2")
        )
    }

    @Test
    fun singleStringIndexIdentifier() {
        assertPathEquals("$['StringNamedElement1']", parser.parse("$['StringNamedElement1']"))
        assertPathEquals("$['StringNamedElement1']", parser.parse("$[\"StringNamedElement1\"]"))
        assertPathEquals("$['StringNamedElement1']", parser.parse("$[StringNamedElement1]"))
    }

    @Test
    fun doubleStringIndexIdentifier() {
        assertPathEquals("$['id1']['id2']", parser.parse("$['id1']['id2']"))
    }

    @Test
    fun singleIntIndexIdentifier() {
        assertPathEquals("$[1]", parser.parse("$[1]"))
    }

    @Test
    fun doubleIntIndexIdentifier() {
        assertPathEquals("$[1][2]", parser.parse("$[1][2]"))
        assertPathEquals("$[1][2]", parser.parse("[1][2]"))
    }

    @Test
    fun mixedPaths() {
        assertPathEquals("$.abc['StringNamedElement1']", parser.parse("$.abc[StringNamedElement1]"))
        assertPathEquals("$.abc[1][2]['StringNamedElement1']", parser.parse("$.abc[1][2][StringNamedElement1]"))
        assertPathEquals("$[1].abc[2].efg['StringNamedElement1'].h", parser.parse("$[1].abc[2].efg['StringNamedElement1'].h"))
    }
}

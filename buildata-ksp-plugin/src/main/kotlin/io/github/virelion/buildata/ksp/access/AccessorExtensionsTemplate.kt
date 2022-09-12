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
package io.github.virelion.buildata.ksp.access

import io.github.virelion.buildata.ksp.GeneratedFileTemplate
import io.github.virelion.buildata.ksp.utils.CodeBuilder

class AccessorExtensionsTemplate(
    override val pkg: String,
    val originalName: String
) : GeneratedFileTemplate {
    override val name: String get() = "${originalName}_AccessorExtension"

    override fun generateCode(codeBuilder: CodeBuilder): String {
        return codeBuilder.build {
            appendln("package $pkg")
            emptyLine()
            imports.forEach {
                appendln("import $it // ktlint-disable")
            }
            emptyLine()
            createAccessorExtensionProperty()
        }
    }

    private fun CodeBuilder.createAccessorExtensionProperty() {
        appendDocumentation(
            """
                Accessor that allows for dynamic access (via property name or path) to properties and values.
            """.trimIndent()
        )
        appendln("val $originalName.dynamicAccessor: DynamicAccessor get() =")
        indent {
            appendln("DynamicAccessor($originalName::class.builder().also { it.populateWith(this) })")
        }
    }

    companion object {
        val imports: List<String> = listOf(
            "io.github.virelion.buildata.access.*"
        ).sorted()
    }
}

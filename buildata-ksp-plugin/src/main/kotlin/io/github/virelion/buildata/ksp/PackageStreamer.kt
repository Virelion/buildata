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
package io.github.virelion.buildata.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import io.github.virelion.buildata.ksp.utils.CodeBuilder
import java.io.OutputStreamWriter

internal class PackageStreamer(
    private val codeGenerator: CodeGenerator
) {
    fun consume(template: GeneratedFileTemplate) {
        codeGenerator.createNewFile(Dependencies(aggregating = true), template.pkg, template.name, "kt").use { output ->
            OutputStreamWriter(output).use { writer ->
                writer.append(template.generateCode(CodeBuilder()))
            }
        }
    }
}

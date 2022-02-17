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

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import io.github.virelion.buildata.ksp.Constants.BUILDABLE_FQNAME
import io.github.virelion.buildata.ksp.Constants.DYNAMICALLY_ACCESSIBLE_FQNAME
import io.github.virelion.buildata.ksp.Constants.PATH_REFLECTION_FQNAME
import io.github.virelion.buildata.ksp.extensions.printableFqName

class BuildataSymbolProcessor(
    val logger: KSPLogger,
    val buildataCodegenDir: String
) : SymbolProcessor {

    override fun finish() {
        logger.info("BuildataSymbolProcessor codegeneration finished")
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.info("BuildataSymbolProcessor processing started")

        val streamer = PackageStreamer(buildataCodegenDir)

        // Stream Buildable code-generated classes
        val buildableAnnotated = resolver
            .getSymbolsWithAnnotation(BUILDABLE_FQNAME)
            .filterIsInstance<KSClassDeclaration>()
            .toList()

        val pathReflectionAnnotated = resolver
            .getSymbolsWithAnnotation(PATH_REFLECTION_FQNAME)
            .filterIsInstance<KSClassDeclaration>()
            .toList()

        val annotatedClasses = AnnotatedClasses(
            buildableAnnotated.map { it.printableFqName }.toSet(),
            pathReflectionAnnotated.map { it.printableFqName }.toSet()
        )

        val classProcessor = KSClassDeclarationProcessor(logger, annotatedClasses)

        buildableAnnotated.forEach {
            streamer.consume(classProcessor.processBuilderClasses(it))
        }

        pathReflectionAnnotated.forEach {
            streamer.consume(classProcessor.processPathWrapperClasses(it))
        }

        // Stream Dynamically accessible code-generated classes
        resolver
            .getSymbolsWithAnnotation(DYNAMICALLY_ACCESSIBLE_FQNAME)
            .apply {
                filterIsInstance<KSClassDeclaration>()
                    .map {
                        classProcessor.processAccessorClasses(it)
                    }
                    .forEach(streamer::consume)
            }

        return listOf()
    }
}

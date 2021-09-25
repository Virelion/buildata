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
        val dynamicAccessorAnnotated = resolver
            .getSymbolsWithAnnotation(DYNAMICALLY_ACCESSIBLE_FQNAME)
            .apply {
                filterIsInstance<KSClassDeclaration>()
                    .map {
                        classProcessor.processAccessorClasses(it)
                    }
                    .forEach(streamer::consume)
            }

        return buildableAnnotated.toList() + dynamicAccessorAnnotated.toList()
    }
}

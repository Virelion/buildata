package io.github.virelion.buildata.ksp

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import io.github.virelion.buildata.ksp.Constants.BUILDABLE_FQNAME

class BuildataSymbolProcessor(
    val logger: KSPLogger,
    val buildataCodegenDir: String
) : SymbolProcessor {

    override fun finish() {
        logger.info("BuildataSymbolProcessor codegeneration finished")
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.info("BuildataSymbolProcessor processing started")

        val classProcessor = KSClassDeclarationProcessor(logger)

        val annotated = resolver
            .getSymbolsWithAnnotation(BUILDABLE_FQNAME)
        val classes = annotated
            .asSequence()
            .filterIsInstance<KSClassDeclaration>()
            .map {
                classProcessor.process(it)
            }

        PackageStreamer(buildataCodegenDir).stream(classes)
        return annotated.toList()
    }
}

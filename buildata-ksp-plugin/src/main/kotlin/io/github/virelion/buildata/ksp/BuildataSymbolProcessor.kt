package io.github.virelion.buildata.ksp

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSClassDeclaration
import io.github.virelion.buildata.ksp.Constants.BUILDABLE_FQNAME

@AutoService(SymbolProcessor::class)
class BuildataSymbolProcessor : SymbolProcessor {
    lateinit var logger: KSPLogger
    lateinit var buildataCodegenDir: String

    override fun finish() {
        logger.info("BuildataSymbolProcessor codegeneration finished")
    }

    override fun init(options: Map<String, String>, kotlinVersion: KotlinVersion, codeGenerator: CodeGenerator, logger: KSPLogger) {
        this.buildataCodegenDir = requireNotNull(options["buildataCodegenDir"]) {
            "buildataCodegenDir ksp option should have correct path".apply { logger.error(this) }
        }
        this.logger = logger
        logger.info("BuildataSymbolProcessor initiated")
    }

    override fun process(resolver: Resolver) {
        logger.info("BuildataSymbolProcessor processing started")

        val classProcessor = KSClassDeclarationProcessor(logger)

        val classes = resolver
            .getSymbolsWithAnnotation(BUILDABLE_FQNAME)
            .asSequence()
            .filterIsInstance<KSClassDeclaration>()
            .map {
                classProcessor.process(it)
            }

        PackageStreamer(buildataCodegenDir).stream(classes)
    }
}

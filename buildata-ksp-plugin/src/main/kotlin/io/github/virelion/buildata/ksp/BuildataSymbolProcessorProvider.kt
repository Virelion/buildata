package io.github.virelion.buildata.ksp

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

lateinit var LOGGER: KSPLogger
    private set

@AutoService(SymbolProcessorProvider::class)
class BuildataSymbolProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        LOGGER = environment.logger
        return BuildataSymbolProcessor(
            environment.logger,
            requireNotNull(environment.options["buildataCodegenDir"]) {
                "buildataCodegenDir ksp option should have correct path".apply { environment.logger.error(this) }
            }
        )
    }
}

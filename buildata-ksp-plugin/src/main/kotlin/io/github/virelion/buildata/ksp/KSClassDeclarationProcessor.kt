package io.github.virelion.buildata.ksp

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.*
import io.github.virelion.buildata.ksp.access.AccessorExtensionsTemplate
import io.github.virelion.buildata.ksp.extensions.classFQName
import io.github.virelion.buildata.ksp.extensions.className
import io.github.virelion.buildata.ksp.extensions.printableFqName
import io.github.virelion.buildata.ksp.path.PathPropertyWrapperTemplate

internal class KSClassDeclarationProcessor(
    val logger: KSPLogger,
    val buildableAnnotated: Set<String>
) {
    fun processAccessorClasses(ksClassDeclaration: KSClassDeclaration): AccessorExtensionsTemplate {
        ksClassDeclaration.apply {
            return AccessorExtensionsTemplate(
                pkg = this.packageName.asString(),
                originalName = this.simpleName.getShortName(),
                properties = ksClassDeclaration.getDeclaredProperties().map { it.simpleName.asString() }.toList()
            )
        }
    }

    fun processBuilderClasses(
        ksClassDeclaration: KSClassDeclaration
    ): BuilderClassTemplate {
        ksClassDeclaration.apply {
            if (Modifier.DATA !in this.modifiers) {
                error("Cannot add create builder for non data class", this)
            }
            return BuilderClassTemplate(
                pkg = this.packageName.asString(),
                originalName = this.simpleName.getShortName(),
                properties = getClassProperties()
            )
        }
    }

    fun processPathWrapperClasses(
        ksClassDeclaration: KSClassDeclaration
    ): PathPropertyWrapperTemplate {
        ksClassDeclaration.apply {
            if (Modifier.DATA !in this.modifiers) {
                error("Cannot add create builder for non data class", this)
            }
            return PathPropertyWrapperTemplate(
                pkg = this.packageName.asString(),
                originalName = this.simpleName.getShortName(),
                properties = getClassProperties()
            )
        }
    }

    fun KSClassDeclaration.getClassProperties(): List<ClassProperty> {
        return requireNotNull(primaryConstructor, this) { "$printableFqName needs to have primary constructor to be @Buildable" }
            .parameters
            .filter { it.isVar || it.isVal }
            .map { parameter ->
                val type = parameter.type.resolve()
                ClassProperty(
                    name = requireNotNull(parameter.name?.getShortName(), parameter) { "$printableFqName contains nameless property" },
                    type = type,
                    hasDefaultValue = parameter.hasDefault,
                    nullable = type.nullability == Nullability.NULLABLE,
                    buildable = (type.classFQName() in buildableAnnotated)
                )
            }
    }

    fun error(message: String, ksNode: KSNode): Nothing {
        logger.error(message, ksNode)
        throw BuildataCodegenException(message)
    }

    fun <T : Any> requireNotNull(value: T?, ksNode: KSNode, message: () -> String): T {
        return value ?: error(message.invoke(), ksNode)
    }
}

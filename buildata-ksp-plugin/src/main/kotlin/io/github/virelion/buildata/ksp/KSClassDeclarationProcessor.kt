package io.github.virelion.buildata.ksp

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.Modifier
import com.google.devtools.ksp.symbol.Nullability
import io.github.virelion.buildata.ksp.access.AccessorExtensionsTemplate
import io.github.virelion.buildata.ksp.extensions.printableFqName
import io.github.virelion.buildata.ksp.extensions.typeFQName

internal class KSClassDeclarationProcessor(
    val logger: KSPLogger
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

    fun processBuilderClasses(ksClassDeclaration: KSClassDeclaration): BuilderClassTemplate {
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
                    buildable = type.annotations
                        .any { annotation ->
                            annotation.annotationType.resolve().typeFQName() == Constants.BUILDABLE_FQNAME
                        }
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

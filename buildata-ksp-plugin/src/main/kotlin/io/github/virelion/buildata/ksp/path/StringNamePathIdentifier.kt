package io.github.virelion.buildata.ksp.path

import com.google.devtools.ksp.symbol.KSAnnotation
import io.github.virelion.buildata.ksp.ClassProperty
import io.github.virelion.buildata.ksp.extensions.classFQName
import org.jetbrains.kotlin.utils.addToStdlib.safeAs

@JvmInline
value class StringNamePathIdentifier(
    private val classProperty: ClassProperty,
) {
    companion object {
        val PATH_ELEMENT_NAME_FQNAME = "io.github.virelion.buildata.path.PathElementName"
        val KOTLINX_SERIALIZATION_SERIAL_NAME = "kotlinx.serialization.SerialName"
        val JACKSON_ALIAS = "com.fasterxml.jackson.annotation.JsonAlias"
    }

    private fun getPropertyName(): String {
        return getAnnotatedName() ?: classProperty.name
    }

    private fun getAnnotatedName(): String? {
        val propertyAnnotations = classProperty.annotations
            .map { it.annotationType.resolve().classFQName() to it }
            .toMap()

        propertyAnnotations[PATH_ELEMENT_NAME_FQNAME]?.apply { return getFirstParamOfAnnotationAsString() }
        propertyAnnotations[KOTLINX_SERIALIZATION_SERIAL_NAME]?.apply { return getFirstParamOfAnnotationAsString() }
        propertyAnnotations[JACKSON_ALIAS]?.apply { return getFirstListElementOfAnnotationAsString() }

        return null
    }

    private fun KSAnnotation.getFirstParamOfAnnotationAsString(): String? {
        return arguments.firstOrNull()?.value?.safeAs<String>()
    }

    private fun KSAnnotation.getFirstListElementOfAnnotationAsString(): String? {
        return arguments.firstOrNull()?.value?.safeAs<List<String>>()?.firstOrNull()
    }

    override fun toString(): String {
        return """StringNamePathIdentifier("${getPropertyName()}")"""
    }
}

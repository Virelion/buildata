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
package io.github.virelion.buildata.ksp.path

import com.google.devtools.ksp.symbol.KSAnnotation
import io.github.virelion.buildata.ksp.ClassProperty
import io.github.virelion.buildata.ksp.extensions.classFQName

@JvmInline
value class StringNamePathIdentifier(
    private val classProperty: ClassProperty
) {
    companion object {
        val PATH_ELEMENT_NAME_FQNAME = "io.github.virelion.buildata.path.PathElementName"
        val KOTLINX_SERIALIZATION_SERIAL_NAME = "kotlinx.serialization.SerialName"
        val JACKSON_ALIAS = "com.fasterxml.jackson.annotation.JsonAlias"
    }

    fun getPropertyName(): String {
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
        return arguments.firstOrNull()?.value as? String
    }

    private fun KSAnnotation.getFirstListElementOfAnnotationAsString(): String? {
        return (arguments.firstOrNull()?.value as? List<String>)?.firstOrNull()
    }

    override fun toString(): String {
        return """StringNamePathIdentifier("${getPropertyName()}")"""
    }
}

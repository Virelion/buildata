package io.github.virelion.buildata.path

/**
 * Use this annotation to mark data class to have path reflection capabilities
 *
 * This will make codegen create path solving capabilities.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class PathReflection

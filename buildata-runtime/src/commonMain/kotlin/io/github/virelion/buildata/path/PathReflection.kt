package io.github.virelion.buildata.path

/**
 * Use this annotation to mark data class to have path reflection capabilities
 *
 * This will make codegen create path solving capabilities.
 *
 * Annotating data class
 * ```kotlin
 * @PathReflection
 * data class Data(val item: String)
 * ```
 *
 * will create functions:
 * - ```fun Data.withPath()```
 * - ```fun KClass<Data>.path()```
 *
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class PathReflection

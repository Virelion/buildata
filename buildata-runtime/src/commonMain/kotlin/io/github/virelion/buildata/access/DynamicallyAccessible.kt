package io.github.virelion.buildata.access

/**
 * Use this annotation to mark data class as target for builder generation.
 *
 * ```kotlin
 * @DynamicallyAccessible
 * data class Item(
 *     val item: String
 *     // ...
 * )
 * ```
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class DynamicallyAccessible

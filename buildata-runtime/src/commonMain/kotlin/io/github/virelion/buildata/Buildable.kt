package io.github.virelion.buildata

/**
 * Use this annotation to mark data class as target for builder generation.
 *
 * ```kotlin
 * @Buildable
 * data class Item(
 *     // ...
 * )
 * ```
 *
 * Or if that you want to connect builders into composite builder.
 * ```kotlin
 * @Buildable
 * data class Item(
 *     val innerItem: @Buildable InnerItem
 *     // ...
 * )
 * ```
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Buildable

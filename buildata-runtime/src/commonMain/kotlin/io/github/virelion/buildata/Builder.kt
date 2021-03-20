package io.github.virelion.buildata

/**
 * Abstraction of generated builders
 */
interface Builder<T : Any> {
    fun build(): T
    fun populateWith(source: T)
}

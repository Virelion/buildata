package io.github.virelion.buildata

interface Builder<T : Any> {
    fun build(): T
    fun populateWith(source: T)
}

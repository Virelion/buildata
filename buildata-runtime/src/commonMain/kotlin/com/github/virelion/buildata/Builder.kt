package com.github.virelion.buildata

interface Builder<T: Any> {
    fun build(): T
    fun populate(source: T)
}

package com.github.virelion.buildata

interface Builder<T : Any> {
    fun build(): T
    fun populateWith(source: T)
}

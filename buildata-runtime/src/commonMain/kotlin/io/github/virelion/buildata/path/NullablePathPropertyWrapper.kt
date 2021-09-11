package io.github.virelion.buildata.path

interface NullablePathPropertyWrapper<out T: Any> {
    val item: T?
}